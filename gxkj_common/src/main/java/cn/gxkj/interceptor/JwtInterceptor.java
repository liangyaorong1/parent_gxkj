package cn.gxkj.interceptor;

import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.exception.CommonException;
import cn.gxkj.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--14 18:53:00
 */
//@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {
            String token = authorization.replace("Bearer ","");
            Claims claims = jwtUtil.parseJWT(token);
            if (!StringUtils.isEmpty(claims)) {
                //通过claims获取到当前用户的可访问API权限字符串
                String apis = (String)claims.get("apis");

                //通过handlers
                HandlerMethod h = (HandlerMethod)handler;
                //获取接口上的reqeustmapping注解
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前请求接口中的name属性
                String name = annotation.name();
                //获取当前用户是否具有响应的请求权限
                if (apis.contains(name)){
                    request.setAttribute("claims", claims);
                    return true;
                }else {
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }
}
