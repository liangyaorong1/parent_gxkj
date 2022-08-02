package cn.gxkj.social.controller;

import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.model.social_security.*;
import cn.gxkj.social.client.UserFeignClient;
import cn.gxkj.social.service.ArchiveService;
import cn.gxkj.social.service.CompanySettingsService;
import cn.gxkj.social.service.PaymentItemService;
import cn.gxkj.social.service.UserSocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/social_securitys")
public class SocialSecurityController extends BaseController {

	@Autowired
	private CompanySettingsService companySettingsService;

	@Autowired
	private UserSocialService userSocialService;


	@Autowired
	private PaymentItemService paymentItemService;

	@Autowired
	private ArchiveService archiveService;

	@Autowired
	private UserFeignClient userFeignClient;

	@RequestMapping("/settings")
	public Result setting(){
		CompanySettings companySettings = companySettingsService.findById(companyId);
		return new Result(ResultCode.SUCCESS,companySettings);
	}

	@RequestMapping(value = "/list",method = RequestMethod.POST)
	public Result  findAll(@RequestParam(name = "page",defaultValue = "1")Integer page,@RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
		PageResult result = userSocialService.findAll(page, pageSize, companyId);
		return new Result(ResultCode.SUCCESS,result);
	}

	/**
	 * 获取个人社保信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/{id}")
	public Result queryById(@PathVariable(name = "id") String id){
		UserSocialSecurity userSocialSecurity  = userSocialService.findById(id);
		Object user =  userFeignClient.findById(id).getData();
		//将数据汇总
		Map<String,Object> map =new HashMap<String , Object>();
		map.put("user",user);
		map.put("userSocialSecurity",userSocialSecurity);
		return new Result(ResultCode.SUCCESS,map);
	}

	/**
	 * 保存或更新用户社保
	 * @param id
	 * @param userSocialSecurity
	 * @return
	 */
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT)
	public Result save(@PathVariable(name = "id") String id,@RequestBody UserSocialSecurity userSocialSecurity){
		 userSocialService.save(userSocialSecurity);
		 return new Result(ResultCode.SUCCESS);
	}

	/**
	 * 查询对应的城市的缴费项目
	 * @param id
	 * @return
	 */
	@RequestMapping("/payment_item/{id}")
	public Result paymentItem(@PathVariable(name = "id")String id){
		List<CityPaymentItem> allByCity = paymentItemService.findAllByCityId(id);
		return new Result(ResultCode.SUCCESS,allByCity);
	}

	/**
	 * 查询月份数据报表
	 * @param yearMonth
	 * @param opType
	 * @return
	 */
	@RequestMapping(value = "/historys/{yearMonth}",method = RequestMethod.GET)
	public Result historys(@PathVariable(value = "yearMonth")String yearMonth,@RequestParam(value = "opType" )Integer opType){
		ArrayList<ArchiveDetail> reportVoList = new ArrayList<>();
		if (opType == 1){
			//查询当月的,即未归档
			reportVoList.addAll(archiveService.getReports(yearMonth,companyId));
		}else {
           //历史归档数据，即已归档
			Archive archive = archiveService.findArchive(companyId, yearMonth);
			//2.如果归档历史存在，查询归档明细
			if (archive != null){
				reportVoList.addAll(archiveService.findAllDetailByArchiveId(archive.getId()));
			}
		}
		return new Result(ResultCode.SUCCESS,reportVoList);
	}

	/**
	 * 创建新的报表
	 * @param yearMonth
	 * @return
	 */
	@RequestMapping(value = "/historys/{yearMonth}/newReport",method = RequestMethod.PUT)
	public Result newReport(@PathVariable(value = "yearMonth")String yearMonth){
    companySettingsService.newReport(companyId,yearMonth);
    return new Result(ResultCode.SUCCESS);
	}


	/**
	 * 数据归档
	 * @param yearMonth
	 * @return
	 */
	@RequestMapping(value = "/historys/{yearMonth}/archive",method = RequestMethod.POST)
	public Result historyDetail(@PathVariable String yearMonth){
		archiveService.archive(yearMonth,companyId);
		return new Result(ResultCode.SUCCESS);
	}

	/**
	 * 查询历史归档列表
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "/historys/{year}/list",method = RequestMethod.GET)
	public Result historyList(@PathVariable String year){
		List<Archive> list = archiveService.findArchiveByYear(companyId, year);
		return new Result(ResultCode.SUCCESS,list);
	}

	@RequestMapping(value = "/historys/data",method = RequestMethod.GET)
    public ArchiveDetail historysData(String userId,String yearMonth){
		return archiveService.findUserArchiveDetail(userId,yearMonth);
	}

}
