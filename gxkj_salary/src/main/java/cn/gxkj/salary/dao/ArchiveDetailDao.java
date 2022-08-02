package cn.gxkj.salary.dao;

import cn.gxkj.model.salarys.SalaryArchiveDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface ArchiveDetailDao extends JpaRepository<SalaryArchiveDetail, String>, JpaSpecificationExecutor<SalaryArchiveDetail> {
}
