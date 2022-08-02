package cn.gxkj.company.dao;

import cn.gxkj.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--01--28 13:45:00
 */
public interface CompanyDao extends JpaRepository<Company,String> , JpaSpecificationExecutor<Company> {
}
