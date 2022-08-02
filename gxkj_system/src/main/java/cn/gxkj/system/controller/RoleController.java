package cn.gxkj.system.controller;

import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.model.response.RoleResult;
import cn.gxkj.model.system.Role;
import cn.gxkj.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 20:27:00
 */
@RestController
@RequestMapping("/sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    //添加角色
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Result add(@RequestBody Role role) throws Exception {
        String companyId = "1";
        role.setCompanyId(companyId);
        roleService.save(role);
        return Result.SUCCESS();
    }

    //更新角色
    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id") String id, @RequestBody Role role)
            throws Exception {
        roleService.update(role);
        return Result.SUCCESS();
    }

    //删除角色
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
        roleService.delete(id);
        return Result.SUCCESS();
    }

    /**
     * 根据ID获取角色信息
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS, new RoleResult(role));
    }

    /**
     * 分页查询角色
     */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(int page, int pagesize, Role role) throws Exception {
        Page<Role> searchPage = roleService.findSearch(companyId, page, pagesize);
        PageResult<Role> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }

    @RequestMapping(value="/role/list" ,method=RequestMethod.GET)
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }

    @RequestMapping(value = "/role/assignPrem",method = RequestMethod.PUT)
    public Result assignPrem(@RequestBody Map<String,Object> map){

        //1.获取被分配角色的id
        String id =(String) map.get("id");
        //2.获取到角色的权限列表
        List<String> permIds = (List<String>) map.get("permIds");
        //3.调用service层，进行角色权限分配
        roleService.assignPerms(id,permIds);
        return new Result(ResultCode.SUCCESS);
    }
}
