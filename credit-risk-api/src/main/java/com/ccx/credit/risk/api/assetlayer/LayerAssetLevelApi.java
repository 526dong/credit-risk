package com.ccx.credit.risk.api.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerAssetLevel;
import java.util.List;
import java.util.Map;
	
/**
 * @author zhaotm
 */
public interface LayerAssetLevelApi {
		
	//主键获取	
	LayerAssetLevel getById(Integer id);	
		
	//获取无参list	
	List<LayerAssetLevel> getList();	
		
	//获取有参数list	
	List<LayerAssetLevel> getList(LayerAssetLevel model);
		
	//通过条件获取	
	LayerAssetLevel getByModel(LayerAssetLevel model);	
	
	//保存对象	
	int save(LayerAssetLevel model);	
	
	//更新对象	
	int update(LayerAssetLevel model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

    List<LayerAssetLevel> getListByLayerId(Integer layerId);
}
