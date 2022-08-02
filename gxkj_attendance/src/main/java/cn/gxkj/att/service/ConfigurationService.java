package cn.gxkj.att.service;

import cn.gxkj.IdWorker;
import cn.gxkj.att.dao.*;
import cn.gxkj.model.atte.entity.AttendanceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ConfigurationService{

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private LeaveConfigDao leaveConfigDao;

    @Autowired
    private DeductionDictDao deductionDictDao;

    @Autowired
    private ExtraDutyConfigDao extraDutyConfigDao;

    @Autowired
    private ExtraDutyRuleDao extraDutyRuleDao;

    @Autowired
    private DayOffConfigDao dayOffConfigDao;

    @Autowired
    private IdWorker idWorker;

    public AttendanceConfig getAtteCfgItem(String companyId, String id) {
       return attendanceConfigDao.findByCompanyIdAndDepartmentId(companyId,id);
    }

    public void addAtte(AttendanceConfig attendanceConfig) {
        AttendanceConfig atteCfgItem = this.getAtteCfgItem(attendanceConfig.getCompanyId(), attendanceConfig.getDepartmentId());
        if (atteCfgItem == null){
            attendanceConfig.setId(idWorker.nextId()+"");
            attendanceConfig.setCreateDate(new Date());
        }
        attendanceConfigDao.save(attendanceConfig);
    }
}
