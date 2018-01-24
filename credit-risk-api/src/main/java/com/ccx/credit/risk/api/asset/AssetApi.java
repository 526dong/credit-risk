package com.ccx.credit.risk.api.asset;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.Asset;
import com.github.pagehelper.PageInfo;

public interface AssetApi {
	void insert(Asset asset);

	void deleteById(Integer id);
	
    void update(Asset asset);
    
    Asset findById(Integer id);

    int findByName(String applyCode);

    int findByCode(String applyCode);

    int findByApplyCode(String applyCode);
    
    List<Asset> findAllAsset();
    
    PageInfo<Asset> findAll(Map<String,Object> params);

    //查询创建人列表
    List<User> findAllUser();

    /**
     * 根据ids查资产
     * @param assetIds
     * @return
     */
    PageInfo<Map<String, Object>> findByIds(String assetIds);
}
