package cn.gxkj.model.system;

import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

/**
 * @description 存入到redis需要实现AuthCachePrincipal接口
 * @author:Liang
 * @CREATE:2022--07--12 22:51:00
 */
@Getter
@Setter
public class ProfileResult implements Serializable, AuthCachePrincipal {

    private String mobile;
    private String username;
    private String companyName;
    private String companyId;
    private String userId;
    private Map<String, Object> roles;

    public ProfileResult(User user, List<Permission> list) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.companyName = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        //角色数据
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        Map<String, Object> rolesMap = new HashMap<>();
        //遍历出所有的角色
        for (Permission perm : list) {
            String code = perm.getCode();
            //判断权限类别
            if (perm.getType() == 1) {
                menus.add(code);
            } else if (perm.getType() == 2) {
                points.add(code);
            } else {
                apis.add(code);
            }
        }
        rolesMap.put("menus",menus);
        rolesMap.put("points",points);
        rolesMap.put("apis",apis);
        this.roles = rolesMap;
    }
    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.companyName = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.userId = user.getId();
        //角色数据
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        Map<String, Object> rolesMap = new HashMap<>();
        for (Role role : user.getRoles()) {
            //遍历出所有的角色
            for (Permission perm : role.getPermissions()) {
                String code = perm.getCode();
                //判断权限类别
                if (perm.getType() == 1) {
                    menus.add(code);
                } else if (perm.getType() == 2) {
                    points.add(code);
                } else {
                    apis.add(code);
                }
            }
        }
        rolesMap.put("menus",menus);
        rolesMap.put("points",points);
        rolesMap.put("apis",apis);
        this.roles = rolesMap;
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
