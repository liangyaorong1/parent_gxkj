package cn.gxkj.model.response;

import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--14 13:50:00
 */
@Setter
@Getter
public class RoleResult {

    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 说明
     */
    private String description;
    /**
     * 企业id
     */
    private String companyId;

    private List<String> permIds = new ArrayList<>();

    public RoleResult(Role role){
        BeanUtils.copyProperties(role,this);

        //获取这个角色对应权限，并将其遍历取出其权限id
        for (Permission permission:role.getPermissions()){
            this.permIds.add(permission.getId());
        }
    }

}
