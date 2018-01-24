package com.ccx.credit.risk.mapper.asset;


import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.asset.AssetCount;

public interface AbsAssetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbsAsset record);

    AbsAsset selectByPrimaryKey(Integer id);

    int findByApplyCode(String applyCode);

    int validateName(String name);

    int validateCode(String code);

    List<AbsAsset> findAll(Map<String, Object> params);

    /**
     * 查询所有的评级企业
     */
    List<Map<String, Object>> findAllRateEnt();

    /**
     * 查询所有的评级结果
     */
    List<Map<String, Object>> findAllRateResult();

    /**
     * 查询所有资产业务类型
     * @return
     */
    List<Map<String, Object>> findAllBusinessType(@Param("type") Integer assetType);

    int updateByPrimaryKey(AbsAsset record);
    
    //通过统计维度统计分布情况
  	List<AssetCount> analyseDimCount(@Param("AssetCount")AssetCount assetCount);
  	
  	List<AssetCount> assetMoneyCount(@Param("AssetCount")AssetCount assetCount);
  	
  	
}