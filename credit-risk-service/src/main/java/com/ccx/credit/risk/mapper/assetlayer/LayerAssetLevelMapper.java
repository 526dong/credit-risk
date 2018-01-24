package com.ccx.credit.risk.mapper.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerAssetLevel;

import java.util.List;

public interface LayerAssetLevelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LayerAssetLevel record);

    int insertSelective(LayerAssetLevel record);

    LayerAssetLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LayerAssetLevel record);

    int updateByPrimaryKey(LayerAssetLevel record);

    void deleteByLayerId(Integer layerId);

    void insertList(List<LayerAssetLevel> list);

    List<LayerAssetLevel> getListByLayerId(Integer layerId);
}