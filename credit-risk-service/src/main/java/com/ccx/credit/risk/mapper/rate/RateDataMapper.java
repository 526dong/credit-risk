package com.ccx.credit.risk.mapper.rate;

import com.ccx.credit.risk.model.rate.RateData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
public interface RateDataMapper {
    //主体最新一次的评级结果
    RateData selectLastRateData(String name);

    RateData selectByPrimaryKey(Integer id);

    //通过评级数据查评级机构的优先级
    Integer selectInstitutionPriorityByRateDataId(Integer id);

    //通过评分等级查id
    Integer selectResultIdByName(String name);

    /*更新影子评级标识：标识为使用影子评级*/
    void updateShadow(@Param("id") Integer id);
}