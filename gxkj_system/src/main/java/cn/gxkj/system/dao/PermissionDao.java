package cn.gxkj.system.dao;

import cn.gxkj.model.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 20:42:00
 */
public interface PermissionDao extends JpaRepository<Permission,String>, JpaSpecificationExecutor<Permission> {

   List<Permission> findByTypeAndPid(int type,String pid);
}
