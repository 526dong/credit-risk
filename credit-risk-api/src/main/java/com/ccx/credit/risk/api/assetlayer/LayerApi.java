package com.ccx.credit.risk.api.assetlayer;

import com.ccx.credit.risk.model.assetlayer.Layer;
import com.github.pagehelper.PageInfo;
import java.util.*;
	
/**
 * @author zhaotm
 */
public interface LayerApi {
		
	//主键获取	
	Layer getById(Integer id);
		
	//获取无参list	
	List<Layer> getList();	
		
	//获取有参数list	
	List<Layer> getList(Layer model);	
		
	//获取带分页list	
	PageInfo<Layer> getPageList(Map<String,Object> params);
		
	//通过条件获取	
	Layer getByModel(Layer model);	
	
	//保存对象	
	int save(Layer model);	
	
	//更新对象	
	int update(Layer model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	/**
	 * 查分层历史
	 * @param packageId
	 * @return
	 */
    PageInfo<Layer> getLayerHistory(Integer packageId);

	/**
	 * 资产还款最大日期
	 * @param assetPackageId
	 * @return
	 */
	Date findMaxDateByPackageId(Integer assetPackageId);

	/**
	 * 资产投放最小日期
	 * @param assetPackageId
	 * @return
	 */
	Date findMinDateByPackageId(Integer assetPackageId);
}
