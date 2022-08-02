package cn.gxkj.system.service;

import cn.gxkj.IdWorker;
import cn.gxkj.model.Department;
import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.Role;
import cn.gxkj.model.system.User;
import cn.gxkj.system.client.DepartmentFeignClient;
import cn.gxkj.system.client.EmployeeFeignClient;
import cn.gxkj.system.dao.RoleDao;
import cn.gxkj.system.dao.UserDao;
import cn.gxkj.utils.QiuNiuUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.*;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--05 10:10:00
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    @Autowired
    private EmployeeFeignClient employeeFeignClient;




    /**
     * 修改用户
     *
     * @param user
     */
    public void save(User user) {
        //设置主键
        String id = idWorker.nextId() + "";
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        //设置初始化密码
        user.setLevel("user");
        user.setPassword(password);

        //设置账号为启动状态
        user.setEnableState(1);
        user.setId(id);
        userDao.save(user);
    }


    public void update(User user) {
        User target = userDao.findById(user.getId()).get();
        //设置更新属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        //更新部门
    }

    /**
     * 查找用户
     *
     * @param id
     * @return
     */
    public User findById(String id) {

        return userDao.findById(id).get();
    }

    /**
     * 分页查询用户
     *
     * @param map
     * @param page
     * @param size
     * @return
     */
    public Page findAll(Map<String, Object> map, int page, int size) {
        //1.需要查询的条件
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), (String) map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), (String) map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断， 是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if ("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //2.分页
        return userDao.findAll(spec, new PageRequest(page - 1, size));
    }

    public void deleteById(String id) {
        userDao.deleteById(id);
    }


    public void assignRoles(String userId, List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户与角色集合的关系
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }

    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }

    public User findByMobileAndPassword(String mobile, String password) {
        return userDao.findOne(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                list.add(criteriaBuilder.equal(root.get("mobile").as(String.class), mobile));
                list.add(criteriaBuilder.equal(root.get("password").as(String.class), password));
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        }).get();
    }

    @Transactional
    public void saveAll(List<User> users) {
        //进行批量添加
        for (User user : users) {
            //填充对应的数据
            user.setLevel("user");
            user.setId(idWorker.nextId() + "");
            user.setEnableState(1);
            String password = new Md5Hash("123456", user.getMobile(), 3).toString();
            user.setPassword(password);
            //填充部门信息
            Department department = departmentFeignClient.queryById(user.getDepartmentId(), user.getCompanyId());
            if (department != null){
                user.setDepartmentName(department.getName());
                user.setDepartmentId(department.getId());
            }
            userDao.save(user);
        }
    }

    public String upload(String id, MultipartFile file) throws IOException {
        User user = userDao.findById(id).get();
        //base64上传
//        String base64 = "data:image/jpg;base64," +Base64.encode(file.getBytes());
//        user.setStaffPhoto(base64);
        //七牛服务器上传
        String uploadUrl = new QiuNiuUtils().upload(user.getId(), file.getBytes());
        user.setStaffPhoto(uploadUrl);
        userDao.save(user);
        return uploadUrl;
    }
}
