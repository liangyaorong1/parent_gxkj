package cn.gxkj.employee.dao;

import cn.gxkj.model.employee.UserCompanyPersonal;
import cn.gxkj.model.employee.response.EmployeeReportResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 数据访问接口
 */
public interface UserCompanyPersonalDao extends JpaRepository<UserCompanyPersonal, String>, JpaSpecificationExecutor<UserCompanyPersonal> {

    UserCompanyPersonal findByUserId(String userId);

    @Query(value = "select new cn.gxkj.model.employee.response.EmployeeReportResult(a,b)"+
            "from UserCompanyPersonal a " +
            "LEFT JOIN EmployeeResignation b on a.userId=b.userId where a.companyId=?1 and a.timeOfEntry like?2 or (" +
            "b.resignationTime like ?2)")
    List<EmployeeReportResult> findByReport(String companyId, String month);
}