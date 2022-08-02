package cn.gxkj.system.client;

import cn.gxkj.common.entity.Result;
import cn.gxkj.model.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--19 13:03:00
 */
//@FeignClient注解用于指定从哪个服务中调用功能，注意里面的名称与被调用的服务名称一致
@Component
@FeignClient(value = "ihrm-company")
public interface DepartmentFeignClient {

//    @RequestMapping用于被调用的微服务的地址映射
    @RequestMapping(value = "/company/departments/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(name = "id") String id) throws Exception;

    @RequestMapping(value = "/company/queryByCode",method = RequestMethod.POST)
    public Department queryById(@RequestParam(value = "code") String code, @RequestParam(value = "companyId") String companyId);


}
