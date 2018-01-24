package com.ccx.credit.risk.api.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerLeve;

import java.util.List;

public interface LayerLevelApi {
    List<LayerLeve> findLevelList();
}
