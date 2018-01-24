package com.ccx.credit.risk.mapper.asset;

import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;

import java.util.List;

public interface AbsAssetsRepaymentMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByAssetId(Integer assetId);

    int insert(AbsAssetsRepayment record);

    int insertBatch(List<AbsAssetsRepayment> list);

    AbsAssetsRepayment selectByPrimaryKey(Integer id);

    /**
     * 通过资产id查询现金流
     * @param assetId
     * @return
     */
    List<AbsAssetsRepayment> findCashFlowByAssetId(Integer assetId);

    int updateByPrimaryKey(AbsAssetsRepayment record);
}