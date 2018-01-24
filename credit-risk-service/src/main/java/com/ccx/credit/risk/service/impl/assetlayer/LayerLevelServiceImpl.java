package com.ccx.credit.risk.service.impl.assetlayer;

import com.ccx.credit.risk.api.assetlayer.LayerLevelApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerLevelMapper;
import com.ccx.credit.risk.model.assetlayer.LayerLeve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayerLevelServiceImpl implements LayerLevelApi {

    @Autowired
    private LayerLevelMapper layerLevelMapper;

    @Override
    public List<LayerLeve> findLevelList() {
        return layerLevelMapper.findLevelList();
    }
}
