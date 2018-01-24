package com.ccx.credit.risk.mapper.assetlayer;


import com.ccx.credit.risk.model.assetlayer.LayerLeve;

import java.util.List;

public interface LayerLevelMapper {

    List<LayerLeve> findLevelList();
}