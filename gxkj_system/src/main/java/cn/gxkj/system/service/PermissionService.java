package cn.gxkj.system.service;

import cn.gxkj.IdWorker;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.exception.CommonException;
import cn.gxkj.model.system.Permission;
import cn.gxkj.model.system.PermissionApi;
import cn.gxkj.model.system.PermissionMenu;
import cn.gxkj.model.system.PermissionPoint;
import cn.gxkj.system.dao.PermissionApiDao;
import cn.gxkj.system.dao.PermissionDao;
import cn.gxkj.system.dao.PermissionMenuDao;
import cn.gxkj.system.dao.PermissionPointDao;
import cn.gxkj.utils.BeanMapUtils;
import cn.gxkj.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--03--07 08:32:00
 */
@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionApiDao permissionApiDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PermissionMenuDao permissionMenuDao;

    @Autowired
    private PermissionPointDao permissionPointDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存权限
     *
     * @param map
     * @throws Exception
     */
    public void save(Map<String, Object> map) throws Exception {
        String id = idWorker.nextId() + "";
        //1通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        permission.setId(id);
        //2.根据类型构造不同的资源对象（ 菜单 按钮 api）
        int type = permission.getType();
        if (type == PermissionConstants.PERMISSION_MENU) {
            PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
            permissionMenu.setId(id);
            permissionMenuDao.save(permissionMenu);

        } else if (type == PermissionConstants.PERMISSION_API) {
            PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
            permissionApi.setId(id);
            permissionApiDao.save(permissionApi);
        } else if (type == PermissionConstants.PERMISSION_POINT) {
            PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
            permissionPoint.setId(id);
            permissionPointDao.save(permissionPoint);
        } else {
            //都不满足抛出异常
            throw new CommonException(ResultCode.FAIL);
        }
        //3.保存
        permissionDao.save(permission);
    }

    public void update(Map<String, Object> map) throws Exception {
        Permission permission = BeanMapUtils.mapToBean(map, Permission.class);
        Permission target = permissionDao.findById(permission.getId()).get();
        target.setCode(permission.getCode());
        target.setName(permission.getName());
        target.setDescription(permission.getDescription());
        target.setEnVisible(permission.getEnVisible());
        //2.根据类型构造不同的资源对象（ 菜单 按钮 api）
        int type = permission.getType();
        if (type == PermissionConstants.PERMISSION_MENU) {
            PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map, PermissionMenu.class);
            permissionMenu.setId(target.getId());
            permissionMenuDao.save(permissionMenu);

        } else if (type == PermissionConstants.PERMISSION_API) {
            PermissionApi permissionApi = BeanMapUtils.mapToBean(map, PermissionApi.class);
            permissionApi.setId(target.getId());
            permissionApiDao.save(permissionApi);
        } else if (type == PermissionConstants.PERMISSION_POINT) {
            PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map, PermissionPoint.class);
            permissionPoint.setId(target.getId());
            permissionPointDao.save(permissionPoint);
        } else {
            //都不满足抛出异常
            throw new CommonException(ResultCode.FAIL);
        }
        //3.保存
        permissionDao.save(permission);
    }

    /**
     * 3.根据id查询
     * //1.查询权限
     * //2.根据权限的类型查询资源
     * //3.构造map集合
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Map<String, Object> findById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();
        Object object = null;
        if (type == PermissionConstants.PERMISSION_MENU) {
            object = permissionMenuDao.findById(permission.getId()).get();

        } else if (type == PermissionConstants.PERMISSION_POINT) {
            object = permissionPointDao.findById(permission.getId()).get();
        } else if (type == PermissionConstants.PERMISSION_API) {
            object = permissionApiDao.findById(permission.getId()).get();
        } else {
            throw new CommonException(ResultCode.FAIL);
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name", permission.getName());
        map.put("type", permission.getType());
        map.put("code", permission.getCode());
        map.put("description", permission.getDescription());
        map.put("pid", permission.getId());
        map.put("enVisible", permission.getEnVisible());
        return map;
    }

    /**
     * 4.查询全部
     * type       : 查询全部权限列表type：0：菜单 + 按钮（权限点） 1：菜单2：按钮（权限点）3：API接
     * 口
     * enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
     * pid ：父id
     */
    public List<Permission> findAll(Map<String, Object> map) {
        //1.需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
                /**
                 * 动态拼接查询条件
                 * @return
                 */
                @Override
                public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?>
                criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> list = new ArrayList<>();
                    //根据父id查询
                    if (!StringUtils.isEmpty(map.get("pid"))) {
                        list.add(criteriaBuilder.equal(root.get("pid").as(String.class),
                                (String) map.get("pid")));
                    }
                    //根据enVisible查询
                    if (!StringUtils.isEmpty(map.get("enVisible"))) {

                        list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),
                                (String) map.get("enVisible")));
                    }
                    //根据类型 type
                    if (!StringUtils.isEmpty(map.get("type"))) {
                        String ty = (String) map.get("type");
                        CriteriaBuilder.In<Object> in =
                                criteriaBuilder.in(root.get("type"));
                        if ("0".equals(ty)) {
                            in.value(1).value(2);
                        } else {
                            in.value(Integer.parseInt(ty));
                        }
                        list.add(in);
                    }
                    return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
                }
        };
        return permissionDao.findAll(spec);
    }

    /**
     * 根据id删除
     * @param id
     * @throws Exception
     */
    public void deleteById(String id) throws Exception{
        //1.通过传递的权限id查询权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //2.根据类型构造不同的资源
        int type = permission.getType();
        if (type == PermissionConstants.PERMISSION_API){
            permissionApiDao.deleteById(id);
        }else if(type == PermissionConstants.PERMISSION_POINT){
            permissionPointDao.deleteById(id);
        }else if(type == PermissionConstants.PERMISSION_MENU){
            permissionMenuDao.deleteById(id);
        }else{
            throw new CommonException(ResultCode.FAIL);
        }
    }
}
