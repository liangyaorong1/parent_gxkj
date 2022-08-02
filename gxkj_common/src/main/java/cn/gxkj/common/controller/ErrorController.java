package cn.gxkj.common.controller;

import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--16 18:04:00
 */
@RestController
@CrossOrigin
public class ErrorController {

    //公共错误跳转
    @RequestMapping(value = "autherror")
    public Result autherror(int code){
        return  code ==1 ? new Result(ResultCode.UNAUTHENTICATED):new Result(ResultCode.UNAUTHORISE);
    }
}
