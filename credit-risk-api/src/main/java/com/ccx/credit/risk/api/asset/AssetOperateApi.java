package com.ccx.credit.risk.api.asset;

import java.util.List;

import com.ccx.credit.risk.model.asset.AssetOperate;

public interface AssetOperateApi {
	void insert(AssetOperate assetOperate);
    
	List<AssetOperate> findByAssetId(Integer id);
    
}
