package cn.gxkj.exception;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--01--28 17:13:00
 */
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 全局异常处理
 */
@ControllerAdvice
public class BaseExceptionHandler {
   @ResponseBody
   @ExceptionHandler(value = Exception.class)
   public Result error(HttpServletRequest request, HttpServletResponse response,
                      Exception e) throws IOException {
      e.printStackTrace();
      if (e.getClass() == CommonException.class) {
         CommonException ce = (CommonException) e;
         return new Result(ce.getCode());
      } else {
         return Result.ERROR();
      }
   }

   @ExceptionHandler(value = AuthorizationException.class)
   @ResponseBody
   public Result error(HttpServletRequest request, HttpServletResponse response,AuthorizationException e) {
      return new Result(ResultCode.UNAUTHORISE);
   }
}