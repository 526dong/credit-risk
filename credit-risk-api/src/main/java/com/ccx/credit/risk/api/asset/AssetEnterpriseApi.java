package com.ccx.credit.risk.api.asset;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.asset.AssetEnterprise;

public interface AssetEnterpriseApi {
	void insert(AssetEnterprise assetEnterprise);
    
    void batchInsertEnterprise(List<AssetEnterprise> assetEnterpriseList);

    void deleteById(Integer id);
    
    void deleteEnterpriseByAssetId(Map<String,Object> params);
    
    void update(AssetEnterprise assetEnterprise);
    
    void updateByAsset(AssetEnterprise assetEnterprise);
    
    AssetEnterprise findById(Integer id);
    
    List<AssetEnterprise> findEnterpriseByAssetId(Map<String,Object> params);
    
    AssetEnterprise findByEnterpriseId(Map<String,Object> params);
    
}
