package cn.gxkj.system.controller;

import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.model.Department;
import cn.gxkj.model.employee.response.EmployeeReportResult;
import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.ProfileResult;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.exception.CommonException;
import cn.gxkj.model.system.Role;
import cn.gxkj.model.system.User;
import cn.gxkj.model.system.vo.UserVo;
import cn.gxkj.system.client.AttendancesFeignClient;
import cn.gxkj.system.client.DepartmentFeignClient;
import cn.gxkj.system.service.PermissionService;
import cn.gxkj.system.service.UserService;
import cn.gxkj.utils.JwtUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--05 11:01:00
 */
//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @Autowired
    private AttendancesFeignClient attendancesFeignClient;

    //上传用户头像
    @RequestMapping(value = "/user/upload/{id}",method = RequestMethod.POST)
    public Result upload(@PathVariable("id")String id,@RequestParam(name = "file") MultipartFile file) throws IOException {


       String base64 = userService.upload(id,file);
       return new Result(ResultCode.SUCCESS,base64);
    }

    //测试通过系统微服务调用企业微服务方法
    @RequestMapping(value = "/test/{id}")
    public void findDeptById(@PathVariable String id) throws Exception {
        Result dept = departmentFeignClient.findById(id);
        System.out.println(dept);
    }

    @RequestMapping(value = "/user/import", method = RequestMethod.POST)
    public Result importExcel(@RequestParam(name = "file") MultipartFile multipartFile,@RequestParam(name = "name")String name) throws Exception {
            //根据上传流信息创建工作薄
            Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
            //获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            //创建user的集合对象用来存储
            List<User> users = new ArrayList<>();
            //从第二行开始获取数据
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                //创建一个Object类型的数组，用来存储一个对象的所有值
                Object[] objects = new Object[row.getLastCellNum()];
                //从第二列获取数据
                for (int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    objects[cellNum] = getValue(cell);
                }
                //构造每一个对象
                User user = new User(objects, companyId, companyName);
                users.add(user);
            }
            //进行批量添加
            userService.saveAll(users);

        return new Result(ResultCode.SUCCESS);
    }


    //根据不同类型获取不同的数据
    private static Object getValue(Cell cell) {
        System.out.println(cell.getCellType());
        Object value = null;
       String type = String.valueOf(cell.getCellType());
        if (type.equals("STRING")) {
            value = cell.getStringCellValue();
        } else if (type.equals("BOOLEAN")) {
            value = cell.getBooleanCellValue();
        } else if (type.equals("NUMERIC")) {
            //数据类型（包含日期和普通数字
            if (DateUtil.isCellDateFormatted(cell)) {
                value = cell.getDateCellValue();
            } else {
                value = cell.getNumericCellValue();
            }
        } else if (type.equals("FORMULA")) {
            value = cell.getCellFormula();
        }
        return value;
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST, name = "POINT-USER-ADD")
    public Result save(@RequestBody User user) {
        //1.设置保存的企业id
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //2.调用service完成保存企业
        userService.save(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam Map map) {
        //1.获取当前的企业id
        map.put("companyId", companyId);
        //2.完成查询
        Page<User> pageUser = userService.findAll(map, page, size);
        //3.构造返回结果
        PageResult<User> pageResult = new PageResult(pageUser.getTotalElements(), pageUser.getContent());
        return new Result(ResultCode.SUCCESS, pageResult);
    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        User user = userService.findById(id);
        UserVo userVo = new UserVo();
        List<String> roleIds = new ArrayList<String>();
        for (Role role : user.getRoles()) {
            roleIds.add(role.getId());
        }
        BeanUtils.copyProperties(user, userVo);
        userVo.setRoleIds(roleIds);
        return new Result(ResultCode.SUCCESS, userVo);
    }

    /**
     * 修改User
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT, name = "POINT-USER-UPDATE")
    public Result update(@PathVariable(value = "id") String id, @RequestBody User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequiresPermissions("API-USER-DELETE")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, name = "API-USER-DELETE")
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        //1.获取被分配的用户id
        String userId = (String) map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 用户登陆
     *
     * @param loginMap
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Result login(@RequestBody Map<String, Object> loginMap) {
        String mobile = (String) loginMap.get("mobile");
        String password = (String) loginMap.get("password");
        try {
            //1.构造登陆令牌UsernamePasswordToken
            //加密密码
            password = new Md5Hash(password, mobile, 3).toString();//1.密码，盐，加密次数
            UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
            //2.获取subject
            Subject subject = SecurityUtils.getSubject();
            //3.调用login方法，进入realm完成认证
            subject.login(upToken);
            //4.获取sessionId
            String sessionId = (String) subject.getSession().getId();
            //构造返回结果
            return new Result(ResultCode.SUCCESS, sessionId);
        } catch (Exception e) {
            return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }


//        User user = userService.findByMobile(mobile);
//        if (StringUtils.isEmpty(user) || !user.getPassword().equals(password)) {
//            //登陆失败
//            return new Result(1001, "手机号或者密码错误", false);
//        }
//        //登陆成功
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("companyId", user.getCompanyId());
//        map.put("companyName", user.getCompanyName());
//        //获取到当前用户的可访问API权限字符串
//        StringBuilder apis = new StringBuilder();
//        Set<Role> roles = user.getRoles();
//        for (Role role:roles){
//            Set<Permission> permissions = role.getPermissions();
//            for (Permission permission:permissions){
//                if (permission.getType() == PermissionConstants.PY_API){
//                    apis.append(permission.getCode()).append(",");
//                }
//
//            }
//        }
//        //可访问的api资源
//        map.put("apis",apis.toString());
//        String jwt = jwtUtil.createJWT(user.getId(), user.getUsername(), map);
//        return new Result(ResultCode.SUCCESS,jwt);
    }

    /**
     * 获取个人信息
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profile(HttpServletRequest request) throws Exception {

        //获取session中安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);

        //以下是token的方式
//        if (StringUtils.isEmpty(claims)){
//            throw new CommonException(ResultCode.UNAUTHENTICATED);
//        }
//        String userId = claims.getId();
//        //根据id查询出用户
//        User user = userService.findById(userId);
//       ProfileResult profileResult = null;
//       if ("user".equals(user.getLevel())){
//           profileResult = new ProfileResult(user);
//       }else{
//           //这里是saas管理员和企业管理员
//          Map<String,Object> map =  new HashMap<String,Object>();
//         if ("coAdmin".equals(user.getLevel())){
//             //enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
//             map.put("enVisible","1");
//         }
//           List<Permission> list = permissionService.findAll(map);
//           profileResult = new ProfileResult(user,list);
//       }
//        return new Result(ResultCode.SUCCESS,profileResult);
    }


}
