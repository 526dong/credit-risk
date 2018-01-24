package com.ccx.credit.risk.mapper.asset;

import com.ccx.credit.risk.model.asset.AbsAssetAssistInfo;

public interface AbsAssetAssistInfoMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 通过资产id删除资产辅助信息
     * @param assetId
     * @return
     */
    int deleteByAssetId(Integer assetId);

    int insert(AbsAssetAssistInfo record);

    AbsAssetAssistInfo selectByPrimaryKey(Integer id);

    AbsAssetAssistInfo getByAssetId(Integer assetId);

    int updateByPrimaryKey(AbsAssetAssistInfo record);
}