package cn.gxkj.system;


import cn.gxkj.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--14 19:53:00
 */
//@Configuration
public class SystemConfig extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**").excludePathPatterns("/sys/login");
    }
}
