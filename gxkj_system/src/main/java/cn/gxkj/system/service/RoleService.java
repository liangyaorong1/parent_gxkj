package cn.gxkj.system.service;

import cn.gxkj.IdWorker;
import cn.gxkj.common.service.BaseService;
import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.Role;
import cn.gxkj.system.dao.PermissionDao;
import cn.gxkj.system.dao.RoleDao;
import cn.gxkj.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--06 19:59:00
 */
@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PermissionDao permissionDao;


    /**
     * 保存角色
     */
    public void save(Role role) {
        role.setId(idWorker.nextId() + "");
        roleDao.save(role);
    }

    /**
     * 修改角色
     *
     * @param role
     */
    public void update(Role role) {
        Role target = roleDao.findById(role.getId()).get();
        target.setDescription(role.getDescription());
        target.setName(role.getName());
        //保存
        roleDao.save(target);
    }

    /**
     * 删除角色
     *
     * @param id
     */
    public void delete(String id) {
        roleDao.deleteById(id);
    }


    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }

    /**
     * 根据条件查找公司
     *
     * @param companyId
     * @param page
     * @param size
     * @return
     */
    public Page<Role> findSearch(String companyId, int page, int size) {
        //创建查询条件
        Specification<Role> roleSpecification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return criteriaBuilder.equal(root.get("companyId").as(String.class), companyId);
            }
        };
        return roleDao.findAll(roleSpecification, PageRequest.of(page - 1, size));
    }

    public List<Role> findAll(String companyId) {

        return roleDao.findAll(getSpecification(companyId));
    }

    /**
     * @param id
     * @param permIds
     */
    @Transactional
    public void assignPerms(String id, List<String> permIds) {
        //查询对应的
        Role role = roleDao.findById(id).get();
//        role.se
        //建立对应的权限的set
        Set<Permission> perms = new HashSet<>();
        for (String perId : permIds) {
            //查询出对应的权限id
            Permission permission = permissionDao.findById(perId).get();
            //需要根据父id和类型查询API权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PERMISSION_API, permission.getPid());
            perms.addAll(apiList);//自定赋予API权限
            perms.add(permission);//当前菜单或按钮的权限
        }
        //重新设置角色与权限的关系
        role.setPermissions(perms);
        //重新添加
        roleDao.save(role);
    }
}
