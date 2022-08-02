package cn.gxkj.audit.controller;

import cn.gxkj.audit.entity.ProcInstance;
import cn.gxkj.audit.service.AuditService;
import cn.gxkj.audit.service.ProcessService;
import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 流程控制的controller
 */
@CrossOrigin
@RestController
@RequestMapping(value="/user/process")
public class ProcessController extends BaseController{

	@Autowired
	private ProcessService processService;

	@Autowired
	private AuditService auditService;

	/**
	 * 部署新流程
	 *     前端将绘制好的流程模型图(bpmn)文件上传到方法中
	 *     参数 : 上传的文件
	 *          MultipartFile
	 */
	@RequestMapping(value = "/deploy",method = RequestMethod.POST)
	public Result deployProcess(@RequestParam("file") MultipartFile file) throws IOException {
		processService.deployProcess(file,companyId);
		return new Result(ResultCode.SUCCESS);
	}

	@RequestMapping(value = "/definition",method = RequestMethod.GET)
	public Result processList(){
     //调用service查询
		List list = processService.getProcessList(companyId);
		return new Result(ResultCode.SUCCESS,list);
	}

	@RequestMapping(value = "/{processKey}",method = RequestMethod.GET)
	public Result suspendOrActivate(@PathVariable (name = "processKey")String processKey){
		processService.suspendOrActivate(processKey,companyId);
		return new Result(ResultCode.SUCCESS);
	}


	@RequestMapping(value = "/instance/{page}/{pagesize}",method = RequestMethod.PUT)
	public Result getProcessList(@RequestBody ProcInstance procInstance,@PathVariable(name = "page")int page,@PathVariable(name = "pagesize")int pagesize){
		Page page1 = auditService.getProcessList(procInstance,page,pagesize);
		PageResult tPageResult = new PageResult(page1.getTotalElements(), page1.getContent());
		return new Result(ResultCode.SUCCESS,tPageResult);
	}
}
