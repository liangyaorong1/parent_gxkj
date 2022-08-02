package cn.gxkj.common.service;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 14:51:00
 */
public class BaseService<T> {

    protected Specification<T> getSpecification(String companyId) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class), companyId);
            }
        };

    }
}
