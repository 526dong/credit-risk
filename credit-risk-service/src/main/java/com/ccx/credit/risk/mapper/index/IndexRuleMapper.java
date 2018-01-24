package com.ccx.credit.risk.mapper.index;


import com.ccx.credit.risk.model.index.IndexRule;
import org.apache.ibatis.annotations.Param;

public interface IndexRuleMapper {

    IndexRule selectByPrimaryKey(Integer id);

    //通过答案获取定性指标的分值
    Integer getScoreByAns(@Param("indexId")Integer indexId, @Param("answer") String answer);

    //通过答案获取定量指标的分值
    IndexRule getByRangeAns(@Param("indexId") Integer indexId, @Param("answer")Double answer);

}