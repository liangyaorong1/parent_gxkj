package cn.gxkj.exception;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--01--28 17:11:00
 */
import cn.gxkj.common.entity.ResultCode;
import lombok.Getter;
@Getter
public class CommonException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   private ResultCode code = ResultCode.SERVER_ERROR;
   public CommonException(){}
   public CommonException(ResultCode resultCode) {
       super(resultCode.message());
       this.code = resultCode;
  }
}
