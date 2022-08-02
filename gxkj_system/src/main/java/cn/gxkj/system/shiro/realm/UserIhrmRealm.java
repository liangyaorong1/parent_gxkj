package cn.gxkj.system.shiro.realm;

import cn.gxkj.common.shiro.realm.IhrmRealm;
import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.ProfileResult;
import cn.gxkj.model.system.User;
import cn.gxkj.system.service.PermissionService;
import cn.gxkj.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--16 19:06:00
 */
@Component
public class UserIhrmRealm extends IhrmRealm {

    @Autowired
    private PermissionService permissionService;

    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    @Autowired
    private UserService userService;

    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户的手机号和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken)authenticationToken;
        String mobile = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //2.根据手机号查询用户
        User user = userService.findByMobile(mobile);
        //3.判断用户是否存在，用户密码是否和输入密码一致
        if (user != null && user.getPassword().equals(password)){
            //4.构造安全数据并返回（安全数据:用户基本数据，权限信息，profileResult)
            ProfileResult result = null;
            if ("user".equals(user.getLevel())){
                result = new ProfileResult(user);
            }else {
                HashMap map = new HashMap();
                if ("coAdmin".equals(user.getLevel())){
                    //企业管理员
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user,list);
            }
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
            return info;
        }
        //返回null，会抛出异常，标识用户名和密码不匹配
        return null;

    }
}
