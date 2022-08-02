package cn.gxkj.system.dao;

import cn.gxkj.model.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--05 10:11:00
 */
public interface RoleDao extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
}
