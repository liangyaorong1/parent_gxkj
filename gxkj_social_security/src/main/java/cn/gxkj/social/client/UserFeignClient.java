package cn.gxkj.social.client;

import cn.gxkj.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--24 17:22:00
 */
@Component
@FeignClient(value = "ihrm-system")
public interface UserFeignClient {

    @RequestMapping(value = "/sys/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id);
}
