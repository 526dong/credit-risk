package com.ccx.credit.risk.service.impl.assetlayer;
	

import com.ccx.credit.risk.api.assetlayer.LayerAssetLevelApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerAssetLevelMapper;
import com.ccx.credit.risk.model.assetlayer.LayerAssetLevel;
import org.springframework.beans.factory.annotation.Autowired;	
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service	
public class LayerAssetLevelServiceImpl implements LayerAssetLevelApi {
		
	@Autowired	
    private LayerAssetLevelMapper layerAssetLevelMapper;	
		
	//主键获取	
	@Override	
	public LayerAssetLevel getById(Integer id) {	
		return layerAssetLevelMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<LayerAssetLevel> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<LayerAssetLevel> getList(LayerAssetLevel model) {	
		return null;
	}	
	
		
	//通过条件获取	
	@Override	
	public LayerAssetLevel getByModel(LayerAssetLevel model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(LayerAssetLevel model) {	
		return layerAssetLevelMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(LayerAssetLevel model) {	
		return layerAssetLevelMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return layerAssetLevelMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	@Override
	public List<LayerAssetLevel> getListByLayerId(Integer layerId) {
		return layerAssetLevelMapper.getListByLayerId(layerId);
	}
}	
