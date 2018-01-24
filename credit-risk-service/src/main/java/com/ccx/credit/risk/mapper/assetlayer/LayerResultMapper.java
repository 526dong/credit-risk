package com.ccx.credit.risk.mapper.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerResult;

import java.util.List;

public interface LayerResultMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(LayerResult record);

    int insertSelective(LayerResult record);

    LayerResult selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LayerResult record);

    int updateByPrimaryKey(LayerResult record);

    List<LayerResult> selectListByLayerId(Integer layerId);
}