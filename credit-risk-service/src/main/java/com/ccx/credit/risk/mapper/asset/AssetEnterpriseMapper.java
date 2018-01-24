package com.ccx.credit.risk.mapper.asset;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ccx.credit.risk.model.asset.AssetEnterprise;

@Component
public interface AssetEnterpriseMapper {
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
