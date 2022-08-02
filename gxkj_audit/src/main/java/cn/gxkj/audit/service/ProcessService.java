package cn.gxkj.audit.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProcessService {

	@Autowired
	private RepositoryService repositoryService;

	/**
	 * 流程部署
	 * @param file  上传bpmn文件
	 * @param companyId  企业id
	 */
	public void deployProcess(MultipartFile file, String companyId) throws IOException {

		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment().addBytes(file.getOriginalFilename(), file.getBytes()).tenantId(companyId);
		Deployment deploy = deploymentBuilder.deploy();
		System.out.println("部署id："+deploy.getId());

	}

	/**
	 * 查询流程
	 * @param companyId
	 * @return
	 */
	public List getProcessList(String companyId) {
		//创建查询对象
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		return processDefinitionQuery.processDefinitionTenantId(companyId).latestVersion().list();
	}

	/**
	 * 流程挂起或激活
	 * @param processKey
	 * @param companyId
	 */
	public void suspendOrActivate(String processKey, String companyId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
		boolean suspended = processDefinition.isSuspended();
		if (suspended){
			//设置成激活
			repositoryService.activateProcessDefinitionByKey(processKey);
		}else{
			//设置成挂起
			repositoryService.suspendProcessDefinitionByKey(processKey);
		}
	}
}
