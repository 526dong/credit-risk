package com.ccx.credit.risk.mapper.asset;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.User;
import org.springframework.stereotype.Component;

import com.ccx.credit.risk.model.asset.Asset;

@Component
public interface AssetMapper {
    void insert(Asset asset);
    
    void deleteById(Integer id);

    /*通过资产id删除资产包关联信息*/
    void deleteAssetPackageAssetId(Integer id);
    
    void update(Asset asset);
    
    Asset findById(Integer id);

    int findByName(String applyCode);

    int findByCode(String applyCode);

    int findByApplyCode(String applyCode);
    
    List<Asset> findAllAsset();

    List<Asset> findAll(Map<String,Object> params);
    //查询创建人列表
    List<User> findAllUser();

    List<Map<String, Object>> findByIdList(List<Integer> idList);
}
