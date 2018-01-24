package com.ccx.credit.risk.api.assetlayer;

import com.ccx.credit.risk.model.assetlayer.LayerAssetsRelevant;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
	
/**
 * @author zhaotm
 */
public interface LayerAssetsRelevantApi {
		
	//主键获取	
	LayerAssetsRelevant getById(Integer id);
		
	//获取无参list	
	List<LayerAssetsRelevant> getList();	
		
	//获取有参数list	
	List<LayerAssetsRelevant> getList(LayerAssetsRelevant model);	
		
	//获取带分页list	
	public PageInfo<LayerAssetsRelevant> getPageList(Map<String, Object> parmas);
		
	//通过条件获取	
	LayerAssetsRelevant getByModel(LayerAssetsRelevant model);	
	
	//保存对象	
	int save(LayerAssetsRelevant model);	
	
	//更新对象	
	int update(LayerAssetsRelevant model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	/**
	 * 保存分层相关性
	 */
	public Map<String, Integer> saveRelation(LayerAssetsRelevant relation, Map<String, Object> paramMap, String assetIdArr, String relationValueArr);

	/**
	 * 分层相关性查询
	 */
    List<LayerAssetsRelevant> getAssetList(Map<String, Object> params);
}
