package cn.gxkj.audit.service;

import cn.gxkj.audit.dao.ProcInstanceDao;
import cn.gxkj.audit.entity.ProcInstance;
import cn.gxkj.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--08--01 12:38:00
 */
@Service
public class AuditService {
    @Autowired
    private ProcInstanceDao procInstanceDao;
    /**
     * 查询流程
     * @param
     * @param page
     * @param pagesize
     * @return
     */
    public Page getProcessList(ProcInstance instance, int page, int pagesize) {
        //1.使用Specification查询,构造Specification
        Specification<ProcInstance> spec = new Specification<ProcInstance>() {
            //2.构造查询条件 (根据传入参数判断,构造)
            @Override
            public Predicate toPredicate(Root<ProcInstance> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                //审批类型      -- processKey
                if(!StringUtils.isEmpty(instance.getProcessKey())) {
//                    list.add(cb.equal(root.get("processKey").as(String.class),instance.getProcessKey()));
                    Expression<String> exp = root.<String>get("processKey");
                    list.add(exp.in(instance.getProcessKey().split(",")));
                }
                //审批状态(多个,每个状态之间使用","隔开)        --processState
                if(!StringUtils.isEmpty(instance.getProcessState())) {
                    Expression<String> exp = root.<String>get("processState");
                    list.add(exp.in(instance.getProcessState().split(",")));
                }
                //当前节点的待处理人     --procCurrNodeUserId
                if(!StringUtils.isEmpty(instance.getProcCurrNodeUserId())) {
                    list.add(cb.like(root.get("procCurrNodeUserId").as(String.class),"%"+instance.getProcCurrNodeUserId()+"%"));
                }
                //发起人 -- userId
                if(!StringUtils.isEmpty(instance.getUserId())) {
                    list.add(cb.equal(root.get("userId").as(String.class),instance.getUserId()));
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //3.调用dao进行Specification查询
        return procInstanceDao.findAll(spec,new PageRequest(page-1,pagesize));//
    }
}
