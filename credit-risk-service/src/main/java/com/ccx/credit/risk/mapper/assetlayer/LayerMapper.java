package com.ccx.credit.risk.mapper.assetlayer;

import com.ccx.credit.risk.model.assetlayer.Layer;
import com.ccx.credit.risk.model.assetlayer.LayerSetup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LayerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Layer record);

    int insertSelective(Layer record);

    Layer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Layer record);

    int updateByPrimaryKey(Layer record);

    List<Layer> getPageList(Map<String, Object> params);

    List<Layer> getLayerHistory(Integer packageId);

    Date findMaxDateByPackageId(Integer assetPackageId);

    List<Map<String,Object>> getCapitalPricipal(LayerSetup setup);

    Date findMinDateByPackageId(Integer assetPackageId);
}