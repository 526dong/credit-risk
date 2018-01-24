package com.ccx.credit.risk.mapper.asset;

import com.ccx.credit.risk.model.asset.AbsAssetEnhanceCredit;

import java.util.List;

public interface AbsAssetEnhanceCreditMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsAssetEnhanceCredit record);

    AbsAssetEnhanceCredit selectByPrimaryKey(Integer id);

    /**
     * 通过父节点查询
     * @param pid
     * @return
     */
    List<AbsAssetEnhanceCredit> findAllByPid(Integer pid);

    int updateByPrimaryKey(AbsAssetEnhanceCredit record);
}