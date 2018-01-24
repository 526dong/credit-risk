package com.ccx.credit.risk.api.assetlayer;


import com.ccx.credit.risk.model.assetlayer.LayerSetup;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface LayerSetupApi {
		
	//主键获取	
	LayerSetup getById(Integer id);
		
	//获取无参list	
	List<LayerSetup> getList();
		
	//获取有参数list	
	List<LayerSetup> getList(LayerSetup model);	
		
	//获取带分页list	
	PageInfo<LayerSetup> getPageList(Map<String,Object> params);
		
	//通过条件获取	
	LayerSetup getByModel(LayerSetup model);	
	
	//保存对象	
	int save(LayerSetup model);	
	
	//更新对象	
	int update(LayerSetup model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();

	/**
	 * 保存分层设置
	 */
	Map<String, Integer> saveLayerSet(HttpServletRequest request, Map<String, Object> paramMap, LayerSetup setup) throws ParseException;

	/**
	 * 保存设置非事物方法，供其他service调用
	 */
	Integer mySaveNewLayer(Map<String, Object> paramMap, Integer layerId);

	/**
	 * 删除level 非事物方法，供其他service调用
	 * @param layerId
	 */
	void myDeleteLayerLevel(Integer layerId);

    /**
	 * 保存设置非事物方法，供其他service调用
	 */
	void mySaveNewLayerLevel(HttpServletRequest request, Integer layerId);

	/**
	 * 迭代level 非事物方法，供其他service调用
	 * @param layerId
	 * @param newLayerId
	 */
	void myGenNewLevelByOldLayerId(Integer layerId, Integer newLayerId);

	/**
	 * 开始分层
	 */
	Map<String, Object> saveLayerStart(Integer id);

	/**
	 * 配置根据分层id获取
	 */
    LayerSetup getByLayerId(Integer layerId);

}
