package cn.gxkj.system.client;

import cn.gxkj.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--29 13:28:00
 */
@Component
@FeignClient(value = "ihrm-attendance")
public interface AttendancesFeignClient {

    @RequestMapping(value = "/attendances/import",method = RequestMethod.POST)
    public Result importAttendances(@RequestParam("file") MultipartFile multipartFile) throws Exception;
}
