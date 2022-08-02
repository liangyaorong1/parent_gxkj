package cn.gxkj.model.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户实体类
 */
@Entity
@Table(name = "bs_user")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 4297464181093070302L;
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 启用状态 0为禁用 1为启用
     */
    private Integer enableState;
    /**
     * 创建时间
     */
    private Date createTime;

    private String companyId;

    private String companyName;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 入职时间
     */
    private Date timeOfEntry;

    /**
     * 聘用形式
     */
    private Integer formOfEmployment;

    /**
     * 工号
     */
    private String workNumber;

    /**
     * 管理形式
     */
    private String formOfManagement;

    /**
     * 工作城市
     */
    private String workingCity;

    /**
     * 转正时间
     */
    private Date correctionTime;

    /**
     * 在职状态 1.在职  2.离职
     */
    private Integer inServiceStatus;

    private String departmentName;

    private String level;

    private String staffPhoto;

   //objs数据位置和excel上传位置一致
    public User(Object[] objects,String companyId,String companyName){
        //默认手机Excel读取为字符串会存在科学计数方问题，转化处理
        this.mobile = objects[2].toString();
        this.username = objects[1].toString();
        this.createTime = new Date();
        this.timeOfEntry = (Date) objects[5];
        this.formOfEmployment = ((Double)objects[4]).intValue();
        this.workNumber = new DecimalFormat("#").format(objects[3]).toString();
        this.departmentId = objects[6].toString(); //这里的objects[6]是部门编码，这个要到服务层进行处理
        this.companyId = companyId;
        this.companyName = companyName;
    }

    /**
     *  JsonIgnore: 忽略json转化
     *  JoinTable: 多对多关系，name指定中间表名称
     *             joinColumns ：指定当前对象哪个列referencedColumnName，和中间表哪个列的name
     *             inverseJoinColumns: 指定依赖对象，哪个列referencedColumnName，和中间表哪个列的name
     *
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name="pe_user_role",
            joinColumns={@JoinColumn(name="user_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="id")}
    )
    private Set<Role> roles = new HashSet<Role>();//用户与角色   多对多

}
