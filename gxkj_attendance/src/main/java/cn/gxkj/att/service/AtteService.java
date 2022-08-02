package cn.gxkj.att.service;

import cn.gxkj.IdWorker;
import cn.gxkj.att.dao.*;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.model.atte.bo.AtteItemBO;
import cn.gxkj.model.atte.bo.AtteReportMonthlyBO;
import cn.gxkj.model.atte.entity.ArchiveMonthly;
import cn.gxkj.model.atte.entity.ArchiveMonthlyInfo;
import cn.gxkj.model.atte.entity.Attendance;
import cn.gxkj.model.atte.entity.CompanySettings;
import cn.gxkj.model.system.User;
import cn.gxkj.utils.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AtteService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private AttendanceDao attendanceDao;

    @Autowired
    private DeductionDictDao deductionDictDao;

    @Autowired
    private AtteCompanySettings atteCompanySettings;

    @Autowired
    private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;

    @Autowired
    private ArchiveMonthlyDao archiveMonthlyDao;


    @Autowired
    private UserDao userDao;

    @Autowired
    private AttendanceConfigDao attendanceConfigDao;

    /**
     * 查询对应报表的详情信息
     * @param id
     * @param companyId
     * @return
     */
    public  List<ArchiveMonthlyInfo> reportsInfo(String id, String companyId) {
        List<ArchiveMonthlyInfo> infos = archiveMonthlyInfoDao.findByAtteArchiveMonthlyId(id);
        return infos;
    }

    /**
     * 查询指定月份的报表
     * @param companyId
     * @param atteDate
     * @return
     */
    public  List<ArchiveMonthlyInfo> reportsList(String companyId, String atteDate) {
        //1.查询所有的用户
        List<User> allUser = userDao.findAll();
        List<ArchiveMonthlyInfo> list = new ArrayList<ArchiveMonthlyInfo>();
        //2.遍历所有的用户
        for (User user : allUser) {
            //3.查询对应的考勤记录
            ArchiveMonthlyInfo atteReportMonthlyBO = new ArchiveMonthlyInfo(user);
            Map<String,Object> map = attendanceDao.queryAtte(companyId,atteDate+"%",user.getId());
            atteReportMonthlyBO.setStatisData(map);
            list.add(atteReportMonthlyBO);
        }
        //4.返回
        return  list;
    }


    /**
     * 查询考勤
     *
     * @param map
     * @param page
     * @param pagesize
     * @return
     */
    public Map<String, Object> getAtteList(Map<String, Object> map, Integer page, Integer pagesize) throws ParseException {
       Map<String,Object> map1 = new HashMap<String,Object>();
        String companyId = (String) map.get("companyId");
        //分页查询用户
        Page<User> pageUser = this.findAll(map, page, pagesize);
        List<User> content = pageUser.getContent();
        List<AtteItemBO> bo = new ArrayList<AtteItemBO>();
        //获取当前这个月有多少天
        CompanySettings companySettings = atteCompanySettings.findById(companyId).get();
        //遍历所有的用户
        for (User user : content) {
            //创建当前用户的考勤信息的集合
            List<Attendance> attendanceList = new ArrayList<>();
            AtteItemBO atteItemBO = new AtteItemBO();
            //获取每个用户的考勤信息
            BeanUtils.copyProperties(user, atteItemBO);
            String[] daysByYearMonth = DateUtil.getDaysByYearMonth(companySettings.getDataMonth());
            for (String day : daysByYearMonth) {
                Attendance userIdAndDay = attendanceDao.findByUserIdAndDay(user.getId(), day);
                //判断是否为空，为空则是矿工
                if (userIdAndDay == null) {
                    userIdAndDay = new Attendance();
                    userIdAndDay.setId(idWorker.nextId() + "");
                    userIdAndDay.setCompanyId(companyId);
                    userIdAndDay.setAdtStatu(2);
                    userIdAndDay.setDay(day);
                    userIdAndDay.setUserId(user.getId());
                    attendanceDao.save(userIdAndDay);
                }
                attendanceList.add(userIdAndDay);
            }
            atteItemBO.setAttendanceRecord(attendanceList);
            bo.add(atteItemBO);
        }
        PageResult pageResult = new PageResult(pageUser.getTotalElements(), bo);
        map1.put("data",pageResult);
        map1.put("tobeTaskCount",0);
        map1.put("monthOfReport",companySettings.getDataMonth().substring(4));
        map1.put("year",companySettings.getDataMonth().substring(0,4));
        return map1;
    }

    public static void main(String[] args) throws ParseException {
        String[] daysByYearMonth = DateUtil.getDaysByYearMonth("202207");
        for (int i = 0; i < daysByYearMonth.length; i++) {
            System.out.println(daysByYearMonth[i]);
        }
        System.out.println("202207".substring(0,4));
    }

    /**
     * 分页查找用户
     *
     * @param map
     * @param page
     * @param size
     * @return
     */
    public Page findAll(Map<String, Object> map, int page, int size) {
        //1.需要查询的条件
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), (String) map.get("companyId")));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //2.分页
        return userDao.findAll(spec, new PageRequest(page - 1, size));
    }

    /**
     * 修改
     */
    public void editor(String id,Map<String,Object> map) {
        Integer adtStatu =  Integer.parseInt(map.get("adtStatu")+"");
        String day = (String) map.get("day");
        String userId = (String) map.get("userId");
        Attendance attendance = attendanceDao.findByUserIdAndDay(userId, day);
        attendance.setAdtStatu(adtStatu);
        attendanceDao.save(attendance);
    }

    /**
     * 归档
     * @param companyId
     * @param atteDate
     */
    public void archives(String companyId, String atteDate) {
       ArchiveMonthly archiveMonthly =  archiveMonthlyDao.findByArchiveMonthAndArchiveYearAndCompanyId(atteDate.substring(4),atteDate.substring(0,4),companyId);
        //判断是否已经创建过了
        if (archiveMonthly == null){
            archiveMonthly = new ArchiveMonthly();
            archiveMonthly.setArchiveMonth(atteDate.substring(4));
            archiveMonthly.setArchiveYear(atteDate.substring(0,4));
            archiveMonthly.setCompanyId(companyId);
            archiveMonthly.setId(idWorker.nextId()+"");
        }

        //1.创建归档总表

        archiveMonthly.setTotalPeopleNum(8);//总人数，这里为了方便就用8代替
        //2.查询所有的用户
        List<User> all = userDao.findAll();
        //3.遍历所有用户，并创建归档详细表，将其保存
       for (User user : all){
           //判断是否已经存在过归档
           ArchiveMonthlyInfo archiveMonthlyInfo =archiveMonthlyInfoDao.findByAtteArchiveMonthlyIdAndUserId(archiveMonthly.getId(),user.getId());
          if (archiveMonthlyInfo == null){
              archiveMonthlyInfo = new ArchiveMonthlyInfo(user);
              archiveMonthlyInfo.setId(idWorker.nextId()+"");
          }

           Map<String, Object> map = attendanceDao.queryAtte(companyId, atteDate+"%", user.getId());
           archiveMonthlyInfo.setStatisData(map);
           archiveMonthlyInfo.setAtteArchiveMonthlyId(archiveMonthly.getId());
           archiveMonthlyInfoDao.save(archiveMonthlyInfo);
       }
        archiveMonthlyDao.save(archiveMonthly);
    }


    /**
     * 新建报表
     * @param companyId
     * @param atteDate
     */
    public void newReports(String companyId, String atteDate) {
        CompanySettings companySettings = atteCompanySettings.findById(companyId).get();
        companySettings.setDataMonth(atteDate);
        atteCompanySettings.save(companySettings);
    }

    /**
     * 查询历史报表
     * @param companyId
     * @param year
     * @return
     */
    public List<ArchiveMonthly> historyReports(String companyId, String year) {
        List<ArchiveMonthly> archiveMonthlies = archiveMonthlyDao.findByCompanyIdAndArchiveYear(companyId,year);
        return archiveMonthlies;
    }
}
