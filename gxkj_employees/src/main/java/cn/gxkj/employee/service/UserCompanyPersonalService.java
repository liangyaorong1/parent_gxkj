package cn.gxkj.employee.service;

import cn.gxkj.employee.dao.UserCompanyPersonalDao;
import cn.gxkj.model.employee.UserCompanyPersonal;
import cn.gxkj.model.employee.response.EmployeeReportResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/10/19 9:52
 * Description:
 */
@Service
public class UserCompanyPersonalService {
    @Autowired
    private UserCompanyPersonalDao userCompanyPersonalDao;

    public void save(UserCompanyPersonal personalInfo) {
        userCompanyPersonalDao.save(personalInfo);
    }

    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }

    //根据企业id和年月查询
    public List<EmployeeReportResult> findByReport(String companyId, String month) {
        return userCompanyPersonalDao.findByReport(companyId,month);
    }
}
