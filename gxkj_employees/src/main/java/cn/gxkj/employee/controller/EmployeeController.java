package cn.gxkj.employee.controller;

import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.common.entity.Result;

import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.employee.service.*;
import cn.gxkj.model.employee.*;
import cn.gxkj.model.employee.response.EmployeeReportResult;
import cn.gxkj.utils.BeanMapUtils;
import cn.gxkj.utils.DownloadUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import net.sf.jasperreports.engine.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;


    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/{id}/pdf")
    public void pdf(@PathVariable("id")String id) throws IOException {
        //1.引入jasper文件
        Resource resource = new ClassPathResource("templates/UserDetails.jasper");
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());

        //根据id查询对应的数据
        UserCompanyPersonal userCompanyPersonal = userCompanyPersonalService.findById(id);
        UserCompanyJobs userCompanyJobs = userCompanyJobsService.findById(id);

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = BeanMapUtils.beanToMap(userCompanyJobs);
        Map<String, Object> map1 = BeanMapUtils.beanToMap(userCompanyPersonal);
        params.putAll(map);
        params.putAll(map1);
        //构建输出流
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(fileInputStream, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
        } catch (JRException e) {
            e.printStackTrace();
        }finally {
            outputStream.flush();
        }

    }

    @RequestMapping(value = "/saveStaffPhoto/{id}",method = RequestMethod.POST)
    public Result saveStaffPhoto(@PathVariable("id")String id,@RequestParam(name = "file") MultipartFile file) throws IOException {
        UserCompanyPersonal userCompanyPersonal = userCompanyPersonalService.findById(id);
        String base64 = "data:image/jpg;base64," +Base64.encode(file.getBytes());
        userCompanyPersonal.setStaffPhoto(base64);
        userCompanyPersonalService.save(userCompanyPersonal);
        return new Result(ResultCode.SUCCESS,base64);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if (info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(uid);
        if (info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS, info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if (resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, resignation);
    }

    //    @RequestMapping(value = "/export/{month}", method = RequestMethod.GET)
//    public void export(@PathVariable(name = "month") String month) throws Exception {
//        //1.构造数据
//        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId, month + "%");
//        //2.创建工作薄
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        //3.构造sheet
//        String[] titles = {"编号", "姓名", "手机", "最高学历", "国家地区", "护照号", "籍贯",
//                "生日", "属相", "入职时间", "离职类型", "离职原因", "离职时间"};
//        Sheet sheet = workbook.createSheet();
//        Row row = sheet.createRow(0);
//        AtomicInteger atomicInteger = new AtomicInteger();
//        for (String title: titles){
//            Cell cell = row.createCell(atomicInteger.getAndIncrement());
//            cell.setCellValue(title);
//        }
//        AtomicInteger atomicInteger1 = new AtomicInteger(1);
//        Cell cell = null;
//        for (EmployeeReportResult report:list){
//            Row dataRow = sheet.createRow(atomicInteger1.getAndIncrement());
//            //编号
//            //编号
//         cell = dataRow.createCell(0);
//         cell.setCellValue(report.getUserId());
//         //姓名
//         cell = dataRow.createCell(1);
//         cell.setCellValue(report.getUsername());
//         //手机
//         cell = dataRow.createCell(2);
//         cell.setCellValue(report.getMobile());
//         //最高学历
//         cell = dataRow.createCell(3);
//         cell.setCellValue(report.getTheHighestDegreeOfEducation());
//         //国家地区
//         cell = dataRow.createCell(4);
//         cell.setCellValue(report.getNationalArea());
//         //护照号
//         cell = dataRow.createCell(5);
//         cell.setCellValue(report.getPassportNo());
//         //籍贯
//         cell = dataRow.createCell(6);
//         cell.setCellValue(report.getNativePlace());
//         //生日
//         cell = dataRow.createCell(7);
//         cell.setCellValue(report.getBirthday());
//         //属相
//         cell = dataRow.createCell(8);
//         cell.setCellValue(report.getZodiac());
//         //入职时间
//         cell = dataRow.createCell(9);
//         cell.setCellValue(report.getTimeOfEntry());
//         //离职类型
//         cell = dataRow.createCell(10);
//         cell.setCellValue(report.getTypeOfTurnover());
//         //离职原因
//         cell = dataRow.createCell(11);
//         cell.setCellValue(report.getReasonsForLeaving());
//         //离职时间
//         cell = dataRow.createCell(12);
//         cell.setCellValue(report.getResignationTime());
//        }
//
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        workbook.write(os);
//        new DownloadUtils().download(os,response,month+"人事报表.xlsx");
//    }

    /**
     * 根据模板读取Excel数据
     *
     * @param month
     * @throws Exception
     */
    @RequestMapping(value = "/export/{month}", method = RequestMethod.GET)
    public void export(@PathVariable(name = "month") String month) throws Exception {
        //1.构造数据
        List<EmployeeReportResult> list = userCompanyPersonalService.findByReport(companyId, month + "%");

        //2.加载模板流数据
        Resource resource = new ClassPathResource("excel-template/hr-demo.xlsx");
        FileInputStream fis = new FileInputStream(resource.getFile());

        //3.根据文件流，加载指定的工作薄
        XSSFWorkbook wb = new XSSFWorkbook(fis);

        //4.读取工作表
        Sheet sheet = wb.getSheetAt(0);

        //5.抽取公共样式
        Row styleRow = sheet.getRow(2);
        /**
         * 用数组存储对应单元格的样式
         */
        CellStyle[] cellStyles = new CellStyle[styleRow.getLastCellNum()];
        for (int i = 0; i < styleRow.getLastCellNum(); i++) {
            cellStyles[i] = styleRow.getCell(i).getCellStyle();
        }
        //6.构造每行和单元格数据
        AtomicInteger atomicInteger1 = new AtomicInteger(2);
        Cell cell = null;
        for (EmployeeReportResult report : list) {
            Row dataRow = sheet.createRow(atomicInteger1.getAndIncrement());
            //编号
            //编号
            cell = dataRow.createCell(0);
            cell.setCellStyle(cellStyles[0]);
            cell.setCellValue(report.getUserId());
            //姓名
            cell = dataRow.createCell(1);
            cell.setCellStyle(cellStyles[1]);
            cell.setCellValue(report.getUsername());
            //手机
            cell = dataRow.createCell(2);
            cell.setCellStyle(cellStyles[2]);
            cell.setCellValue(report.getMobile());
            //最高学历
            cell = dataRow.createCell(3);
            cell.setCellStyle(cellStyles[3]);
            cell.setCellValue(report.getTheHighestDegreeOfEducation());
            //国家地区
            cell = dataRow.createCell(4);
            cell.setCellStyle(cellStyles[4]);
            cell.setCellValue(report.getNationalArea());
            //护照号
            cell = dataRow.createCell(5);
            cell.setCellStyle(cellStyles[5]);
            cell.setCellValue(report.getPassportNo());
            //籍贯
            cell = dataRow.createCell(6);
            cell.setCellStyle(cellStyles[6]);
            cell.setCellValue(report.getNativePlace());
            //生日
            cell = dataRow.createCell(7);
            cell.setCellStyle(cellStyles[7]);
            cell.setCellValue(report.getBirthday());
            //属相
            cell = dataRow.createCell(8);
            cell.setCellStyle(cellStyles[8]);
            cell.setCellValue(report.getZodiac());
            //入职时间
            cell = dataRow.createCell(9);
            cell.setCellStyle(cellStyles[9]);
            cell.setCellValue(report.getTimeOfEntry());
            //离职类型
            cell = dataRow.createCell(10);
            cell.setCellStyle(cellStyles[10]);
            cell.setCellValue(report.getTypeOfTurnover());
            //离职原因
            cell = dataRow.createCell(11);
            cell.setCellStyle(cellStyles[11]);
            cell.setCellValue(report.getReasonsForLeaving());
            //离职时间
            cell = dataRow.createCell(12);
            cell.setCellStyle(cellStyles[12]);
            cell.setCellValue(report.getResignationTime());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        new DownloadUtils().download(os, response, month + "人事报表.xlsx");
    }

    /**
     * 导入员工
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if (jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if (positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS, positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year", year);
        map.put("companyId", companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(), searchPage.getContent());
        return new Result(ResultCode.SUCCESS, pr);
    }


}
