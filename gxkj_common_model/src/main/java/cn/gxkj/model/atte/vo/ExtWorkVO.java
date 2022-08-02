package cn.gxkj.model.atte.vo;

import cn.gxkj.model.atte.entity.DayOffConfig;
import cn.gxkj.model.atte.entity.ExtraDutyConfig;
import cn.gxkj.model.atte.entity.ExtraDutyRule;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExtWorkVO implements Serializable{

    /**
     * 加班配置
     */
    private ExtraDutyConfig extraDutyConfig;

    /**
     * 加班规则
     */
    private List<ExtraDutyRule> extraDutyRuleList;

    /**
     * 调休配置
     */
    private DayOffConfig dayOffConfigs;


}
