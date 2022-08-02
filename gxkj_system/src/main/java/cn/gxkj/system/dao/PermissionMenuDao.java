package cn.gxkj.system.dao;

import cn.gxkj.model.system.PermissionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 20:43:00
 */
public interface PermissionMenuDao extends JpaRepository<PermissionMenu,String>, JpaSpecificationExecutor<PermissionMenu> {
}
