package com.ccx.credit.risk.mapper.rate;

import com.ccx.credit.risk.model.index.IndexModel;
import com.ccx.credit.risk.model.index.IndexModelRule;
import org.apache.ibatis.annotations.Param;

public interface IndexModelMapper {
    IndexModel selectByPrimaryKey(Integer id);

    //通过评分和模型id获取评分等级
    IndexModelRule getResultByModelIdAndValue(@Param("value") double value, @Param("modelId") Integer modelId);
}