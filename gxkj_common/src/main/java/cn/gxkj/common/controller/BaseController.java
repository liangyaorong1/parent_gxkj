package cn.gxkj.common.controller;

import cn.gxkj.model.system.ProfileResult;
import io.jsonwebtoken.Claims;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 14:45:00
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected String companyId;
    protected String companyName;
    protected String userId;

    protected Claims claims;

    /**
     * 这个注解使得比控制层的方法执行之前
     *
     * @param response
     * @param request
     */
    @ModelAttribute
    public void setReqAndResp(HttpServletResponse response, HttpServletRequest request) {

        this.request = request;
        this.response = response;
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        if (principals != null && !principals.isEmpty()){
           ProfileResult result =(ProfileResult) principals.getPrimaryPrincipal();
           this.companyId = result.getCompanyId();
           this.userId = result.getUserId();
           this.companyName=result.getCompanyName();
        }


        //以下是token方式
//        //获取token中的claims
//        Object obj = request.getAttribute("claims");
//        if (obj != null) {
//            this.claims = (Claims) obj;
//            //根据claims获取当前登陆人的信息
//            this.companyId = (String) this.claims.get("companyId");
//            this.companyName = (String) this.claims.get("companyName");
//        }

    }


}
