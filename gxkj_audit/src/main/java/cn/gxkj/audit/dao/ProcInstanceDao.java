package cn.gxkj.audit.dao;

import cn.gxkj.audit.entity.ProcInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author
 */
public interface ProcInstanceDao extends JpaRepository<ProcInstance,String>,
		JpaSpecificationExecutor<ProcInstance> {
}
