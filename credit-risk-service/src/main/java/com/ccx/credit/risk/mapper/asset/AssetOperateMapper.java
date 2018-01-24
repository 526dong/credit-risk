package com.ccx.credit.risk.mapper.asset;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ccx.credit.risk.model.asset.AssetOperate;

@Component
public interface AssetOperateMapper {
    void insert(AssetOperate assetOperate);
    
    List<AssetOperate> findByAssetId(Integer id);
}
