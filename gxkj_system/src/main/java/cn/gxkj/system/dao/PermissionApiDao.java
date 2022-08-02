package cn.gxkj.system.dao;

import cn.gxkj.model.system.PermissionApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 20:46:00
 */
public interface PermissionApiDao extends JpaRepository<PermissionApi,String>, JpaSpecificationExecutor<PermissionApi> {
}
