package cn.gxkj.common.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--18 17:43:00
 */
public class CustomSessionManager extends DefaultWebSessionManager {
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //获取请求头Authorzation中的数据
        String id = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(id)){
            return super.getSessionId(request, response);
        }else{
            //请求头信息： Bearer sessionId
            id = id.replace("Bearer ","");
            //返回sessionId
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }

    }
}
