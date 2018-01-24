package com.ccx.credit.risk.mapper.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerSetup;
import org.apache.ibatis.annotations.Param;

public interface LayerSetupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LayerSetup record);

    int insertSelective(LayerSetup record);

    LayerSetup selectByPrimaryKey(Integer id);

    LayerSetup selectByLayerId(@Param("layerId") Integer layerId);

    int updateByPrimaryKeySelective(LayerSetup record);

    int updateByPrimaryKey(LayerSetup record);

    LayerSetup getByLayerId(Integer layerId);
}