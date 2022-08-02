package cn.gxkj.system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--21 13:35:00
 */
@Component
@FeignClient(value = "ihrm-employee")
public interface EmployeeFeignClient {

    @RequestMapping(value = "/employees/saveStaffPhoto",method = RequestMethod.POST)
    public void saveStaffPhoto(@RequestParam("id")String id, @RequestParam("baseUrl") String baseUrl);
}
