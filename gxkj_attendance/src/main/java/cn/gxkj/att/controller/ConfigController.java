package cn.gxkj.att.controller;

import cn.gxkj.att.service.ConfigurationService;
import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.model.atte.entity.AttendanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--27 12:58:00
 */
@RestController
@RequestMapping("/cfg")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigurationService configurationService;
    /**
     * 获取部门的考勤设置
     */
    @RequestMapping(value = "/atte/item",method = RequestMethod.POST)
    public Result getAtteCfgItem(@RequestParam("departmentId")String id){
       AttendanceConfig attendanceConfig = configurationService.getAtteCfgItem(companyId,id);
       if (attendanceConfig == null){
           return new Result(ResultCode.setFALL);
       }
       return new Result(ResultCode.SUCCESS,attendanceConfig);
    }

    @RequestMapping(value = "/atte",method = RequestMethod.PUT)
    public Result addAtte(@RequestBody AttendanceConfig attendanceConfig){
        configurationService.addAtte(attendanceConfig);
        return new Result(ResultCode.SUCCESS);
    }
}
