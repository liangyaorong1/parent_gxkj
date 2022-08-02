package cn.gxkj.att.dao;

import cn.gxkj.model.atte.entity.CompanySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--29 15:11:00
 */
public interface AtteCompanySettings extends JpaRepository<CompanySettings, String>, JpaSpecificationExecutor<CompanySettings> {
}
