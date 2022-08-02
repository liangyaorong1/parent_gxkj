package cn.gxkj.company.dao;

import cn.gxkj.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 14:31:00
 */
public interface DepartmentDao extends JpaRepository<Department,String>, JpaSpecificationExecutor<Department> {
     Department findByCodeAndCompanyId(String code, String companyId);
}
