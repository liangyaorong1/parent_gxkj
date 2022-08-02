package cn.gxkj.company.service;
import cn.gxkj.IdWorker;
import cn.gxkj.common.service.BaseService;
import cn.gxkj.company.dao.DepartmentDao;

import cn.gxkj.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 14:32:00
 */
@Service
public class DepartmentService extends BaseService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private IdWorker idWorker;

    public Department add(Department department) {
        //设置主键id
        department.setId(idWorker.nextId() + "");
        department.setCreateTime(new Date());
        return departmentDao.save(department);
    }

    public Department update(Department department) {
        Department sourceDepartment = departmentDao.findById(department.getId()).get();
        sourceDepartment.setName(department.getName());
        sourceDepartment.setPid(department.getPid());
        sourceDepartment.setManagerId(department.getManagerId());
        sourceDepartment.setIntroduce(department.getIntroduce());
        sourceDepartment.setManager(department.getManager());
        return departmentDao.save(department);
    }

    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }

    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }

    public List<Department> findAll(String companyId) {
        return departmentDao.findAll(getSpecification(companyId));
    }

    public Department findByCodeAndCompanyId(String code, String companyId) {
       return departmentDao.findByCodeAndCompanyId(code,companyId);
    }
}
