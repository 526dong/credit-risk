package com.ccx.credit.risk.mapper.rate;


import com.ccx.credit.risk.model.rate.RateResult;

import java.util.List;

public interface RateResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RateResult record);

    int insertSelective(RateResult record);

    RateResult selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RateResult record);

    int updateByPrimaryKey(RateResult record);

    //批量插入
    int insertList(List<RateResult> list);
}