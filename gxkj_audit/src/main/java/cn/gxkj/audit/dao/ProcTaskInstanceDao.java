package cn.gxkj.audit.dao;

import cn.gxkj.audit.entity.ProcTaskInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author itcast
 */
public interface ProcTaskInstanceDao extends JpaRepository<ProcTaskInstance,String>, JpaSpecificationExecutor<ProcTaskInstance> {


	ProcTaskInstance findByProcessIdAndTaskKey(String processId, String taskKey) ;

	List<ProcTaskInstance> findByProcessId(String processId);
}
