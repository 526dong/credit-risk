package com.ccx.credit.risk.api.assetlayer;	

import com.ccx.credit.risk.model.assetlayer.LayerResult;
import com.github.pagehelper.PageInfo;

import java.util.*;
	
/**
 * @author zhaotm
 */
public interface LayerResultApi {
		
	//主键获取	
	LayerResult getById(Integer id);
		
	//获取无参list	
	List<LayerResult> getList();	
		
	//获取有参数list	
	List<LayerResult> getList(LayerResult model);	
		
	//获取带分页list	
	PageInfo<LayerResult> getPageList(Map<String, Object> params);
		
	//通过条件获取	
	LayerResult getByModel(LayerResult model);	
	
	//保存对象	
	int save(LayerResult model);	
	
	//更新对象	
	int update(LayerResult model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

    List<LayerResult> selectListByLayerId(Integer layerId);
}
