package cn.gxkj.company.controller;

import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.company.service.CompanyService;
import cn.gxkj.company.service.DepartmentService;
import cn.gxkj.model.Department;
import cn.gxkj.model.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 15:03:00
 */
@RestController
@RequestMapping("/company")
@CrossOrigin
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/department", method = RequestMethod.POST)
    public Result add(@RequestBody Department department) throws Exception {
        department.setCompanyId(companyId);
        departmentService.add(department);
        return Result.SUCCESS();
    }
    /**
     * 根据id更新企业信息
     */
    @RequestMapping(value = "/department/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable(name = "id") String id, @RequestBody Department
            department) throws Exception {
        department.setCompanyId(companyId);
        department.setId(id);
        departmentService.update(department);
        return Result.SUCCESS();
    }
    /**
     * 根据id删除企业信息
     */
    @RequestMapping(value = "/department/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable(name = "id") String id) throws Exception {
       departmentService.deleteById(id);
        return Result.SUCCESS();
    }
    /**
     * 根据ID获取公司信息
     */
    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id") String id) throws Exception {
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }
    /**
     * 获取企业列表
     */
    @RequestMapping(value = "/department", method = RequestMethod.GET)
    public Result findAll() throws Exception {
        System.out.println(departmentService.findAll(companyId));
        //封装返回的对象数据
        DeptListResult deptListResult = new DeptListResult(companyService.findById(companyId), departmentService.findAll(companyId));
        return new Result(ResultCode.SUCCESS,deptListResult);
    }

    @RequestMapping(value = "/queryByCode",method = RequestMethod.POST)
    public Department queryById(@RequestParam(value = "code") String code,@RequestParam(value = "companyId") String companyId){

       Department department = departmentService.findByCodeAndCompanyId(code,companyId);
       return department;
    }
}
