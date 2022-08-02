package cn.gxkj.social.client;

import cn.gxkj.common.entity.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--26 12:58:00
 */
@Component
@FeignClient("ihrm-employee")
public interface UserCompanyPersonalFeignClient {

    @RequestMapping(value = "/employees/{id}/personalInfo", method = RequestMethod.GET)
     Result findPersonalInfo(@PathVariable(name = "id") String uid);
}
