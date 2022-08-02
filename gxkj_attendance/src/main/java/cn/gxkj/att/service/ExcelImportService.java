package cn.gxkj.att.service;

import cn.gxkj.IdWorker;
import cn.gxkj.att.dao.AttendanceConfigDao;
import cn.gxkj.att.dao.AttendanceDao;
import cn.gxkj.att.dao.UserDao;
import cn.gxkj.model.atte.entity.Attendance;
import cn.gxkj.model.atte.entity.AttendanceConfig;
import cn.gxkj.model.atte.vo.AtteUploadVo;
import cn.gxkj.model.system.User;
import cn.gxkj.poi.ExcelImportUtil;
import cn.gxkj.utils.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Log4j2
@Service
public class ExcelImportService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    @Autowired
    private IdWorker idWorker;

    @Value("attendance.workingDays")
    private String workingDays;

	@Value("attendance.holidays")
	private String holidays;

    public void importAttendances(String companyId, MultipartFile multipartFile) throws Exception {
        //1.读取文件
        List<AtteUploadVo> atteUploadVos = new ExcelImportUtil<AtteUploadVo>(AtteUploadVo.class).readExcel(multipartFile.getInputStream(), 1, 0);
        //遍历集合
        for (AtteUploadVo atteUploadVo : atteUploadVos) {
            //查询用户
            User user = userDao.findByMobile(atteUploadVo.getMobile());
            Attendance attendance = new Attendance(atteUploadVo, user);
            String yyyyMMdd = DateUtil.parseDate2String(atteUploadVo.getInTime(), "yyyyMMdd");
            attendance.setDay(yyyyMMdd);
            //判断是否是休息
            if (holidays.contains(holidays)){
               attendance.setAdtStatu(23);
            }else if (DateUtil.isWeekend(yyyyMMdd) || !workingDays.contains(yyyyMMdd)){
              //是周末，并且不用上班
                attendance.setAdtStatu(23);
            }else {
                //获取当前用户部门的上班时间，和下班时间
                AttendanceConfig depConfig = attendanceConfigDao.findByCompanyIdAndDepartmentId(companyId, user.getDepartmentId());
                if (!DateUtil.comparingDate(depConfig.getMorningStartTime(),attendance.getAdtInTime())){
                    //迟到
                    attendance.setAdtStatu(3);
                }else if(DateUtil.comparingDate(depConfig.getAfternoonStartTime(),attendance.getAdtOutTime())){
                    //早退
                    attendance.setAdtStatu(4);
                }else {
                    attendance.setAdtStatu(1);
                }
            }

            //判断是否已经添加过来，（以第一次导入为准）
          Attendance attendance1 =  attendanceDao.findByUserIdAndDay(user.getId(),attendance.getDay());
            if (attendance1 == null){
                attendance.setId(idWorker.nextId()+"");
                attendanceDao.save(attendance);
            }

        }


    }
}
