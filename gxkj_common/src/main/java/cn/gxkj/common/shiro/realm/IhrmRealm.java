package cn.gxkj.common.shiro.realm;

import cn.gxkj.model.system.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @description 此realm只处理授权数据即可，认证方法需要在登陆模块中补全
 * @author:Liang
 * @CREATE:2022--07--16 18:09:00
 */
public class IhrmRealm extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("ihrmRealm");
    }

    //授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取安全数据
        ProfileResult result = (ProfileResult)  principalCollection.getPrimaryPrincipal();
        //2.获取权限信息
        Set<String> apisPerms = (Set<String>) result.getRoles().get("apis");
        //3.构造权限数据，返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(apisPerms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
