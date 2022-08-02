package cn.gxkj.system.dao;

import cn.gxkj.model.system.PermissionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 20:44:00
 */
public interface PermissionPointDao extends JpaRepository<PermissionPoint,String>, JpaSpecificationExecutor<PermissionPoint> {
}
