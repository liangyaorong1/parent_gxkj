package cn.gxkj.model.response;

import cn.gxkj.model.Company;
import cn.gxkj.model.Department;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--02--11 15:06:00
 */
@Getter
@Setter
public class DeptListResult {
    private String companyId;
    private String companyName;
    private String companyManage;
    private List<Department> depts;

    public DeptListResult(Company company,List<Department> depts){
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();//公司联系人
        this.depts = depts;
    }
}
