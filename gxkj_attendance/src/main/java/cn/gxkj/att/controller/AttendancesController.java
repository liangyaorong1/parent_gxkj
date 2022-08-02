package cn.gxkj.att.controller;

import cn.gxkj.att.service.AtteService;
import cn.gxkj.att.service.ExcelImportService;
import cn.gxkj.common.controller.BaseController;
import cn.gxkj.common.entity.PageResult;
import cn.gxkj.common.entity.Result;
import cn.gxkj.common.entity.ResultCode;
import cn.gxkj.model.atte.bo.AtteReportMonthlyBO;
import cn.gxkj.model.atte.entity.ArchiveMonthly;
import cn.gxkj.model.atte.entity.ArchiveMonthlyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--27 14:46:00
 */
@RestController
@RequestMapping("/attendances")
public class AttendancesController extends BaseController {

    @Autowired
    private AtteService atteService;

    @Autowired
    private ExcelImportService excelImportService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result atteStatisMonthly(@RequestBody(required = false) Map<String, Object> map, @RequestParam("page") Integer page, @RequestParam("pagesize") Integer pagesize) throws ParseException {
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        map.put("companyId", companyId);
        Map<String, Object> pageResult = atteService.getAtteList(map, page, pagesize);
        return new Result(ResultCode.SUCCESS, pageResult);
    }

    /**
     * 导入
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importAttendances(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        excelImportService.importAttendances(companyId, multipartFile);
        return new Result(ResultCode.SUCCESS);
    }


    /**
     * 修改考勤
     *
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result eidtorAtten(@PathVariable("id") String id, @RequestBody Map<String, Object> map) {
        atteService.editor(id, map);
        return new Result(ResultCode.SUCCESS);
    }


    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public Result reports(String atteDate) {
        List<ArchiveMonthlyInfo> list = atteService.reportsList(companyId, atteDate);
        return new Result(ResultCode.SUCCESS, list);
    }

    /**
     * 进行归档处理
     * @param archiveDate
     * @return
     */
    @RequestMapping(value = "/archive/item", method = RequestMethod.GET)
    public Result archives(String archiveDate) {
        atteService.archives(companyId, archiveDate);
        return new Result(ResultCode.SUCCESS);
    }


    @RequestMapping(value = "/newReports", method = RequestMethod.GET)
    public Result newReports(String atteDate) {
        atteService.newReports(companyId, atteDate);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询历史归档报表
     *
     * @param year
     * @return
     */
    @RequestMapping(value = "/reports/year", method = RequestMethod.GET)
    public Result historyReports(String year) {
        List<ArchiveMonthly> archiveMonthlies = atteService.historyReports(companyId, year);
        return new Result(ResultCode.SUCCESS, archiveMonthlies);
    }

    @RequestMapping(value = "/reports/{id}",method = RequestMethod.POST)
    public Result reportsInfo(@PathVariable(name = "id") String id){
         List<ArchiveMonthlyInfo> archiveMonthlyInfos = atteService.reportsInfo(id,companyId);
        return new Result(ResultCode.SUCCESS, archiveMonthlyInfos);
    }


}
