package com.ccx.credit.risk.mapper.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerAssetsRelevant;

import java.util.List;
import java.util.Map;

public interface LayerAssetsRelevantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LayerAssetsRelevant record);

    int insertSelective(LayerAssetsRelevant record);

    LayerAssetsRelevant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LayerAssetsRelevant record);

    int updateByPrimaryKey(LayerAssetsRelevant record);

    void insetList(List<LayerAssetsRelevant> list);

    void deleteByIdList(List<Integer> delIdList);

    List<LayerAssetsRelevant> getAssetList(Map<String, Object> params);

    List<LayerAssetsRelevant> getListByLayerId(Integer layerId);

    void deleteByLayerId(Integer layerId);
}