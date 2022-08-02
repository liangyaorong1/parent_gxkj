package cn.gxkj.social;

import cn.gxkj.common.shiro.realm.IhrmRealm;
import cn.gxkj.common.shiro.session.CustomSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--16 19:59:00
 */
@Configuration
public class ShiroConfiguration {


    private String host="127.0.0.1";

    private int port = 6379;

    @Bean
    public IhrmRealm getRealm(){
        return new IhrmRealm();
    }

    //配置安全管理器
    @Bean
    public SecurityManager securityManager(){
        //使用默认的安全管理器
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        //自定义缓存实现，使用redis
        securityManager.setCacheManager(cacheManager());
        //将自定义的realm交给安全管理统一调度管理
        securityManager.setRealm(getRealm());
        return securityManager;
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //1.创建shiro过滤工厂
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        filterFactoryBean.setSecurityManager(securityManager);
        //3.通过配置（配置登陆页面，登陆成功页面，验证未成功页面）
        filterFactoryBean.setLoginUrl("/autherror?code=1");//设置登陆页面
        filterFactoryBean.setUnauthorizedUrl("/autherror?code=2");//授权失败跳转页面
        //4.配置过滤器集合
        /**
         * key :访问连接
         *      支持通配符的形式
         * value:过滤类型
         *       shiro常用过滤器
         *           anno     :匿名访问(表示此链接所有人可以访问）
         *           authc    :认证后访问（表明此链接需登陆认证成功后可以访问）
         */
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        //配置请求链接过滤器配置
        //匿名访问（所有人员可以使用）
        filterMap.put("/sys/login","anon");
        filterMap.put("/autherror","anon");

        //需要认证
        filterMap.put("/**","authc");
        //5.设置过滤器
        filterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return filterFactoryBean;

    }

    //配置shiro注解支持
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * shiro session的管理
     */
    public DefaultWebSessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    private SessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    //配置shiro redisManager
    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        return redisManager;
    }

    //cacheManager缓存redis实现
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
}
