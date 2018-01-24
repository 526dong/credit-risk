package com.ccx.credit.risk.service.impl.assetlayer;

import com.ccx.credit.risk.api.assetlayer.LayerApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerMapper;

import com.ccx.credit.risk.model.assetlayer.Layer;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;	

/**
 * @author zhaotm
 */
@Service
public class LayerServiceImpl implements LayerApi {
		
	@Autowired	
    private LayerMapper layerMapper;
		
	//主键获取	
	@Override	
	public Layer getById(Integer id) {
		return layerMapper.selectByPrimaryKey(id);	
	}	
		
	//获取无参list	
	@Override	
	public List<Layer> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<Layer> getList(Layer model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public PageInfo<Layer> getPageList(Map<String,Object> params) {
		List<Layer> list = layerMapper.getPageList(params);
		PageInfo<Layer> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}	
		
	//通过条件获取	
	@Override	
	public Layer getByModel(Layer model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(Layer model) {	
		return layerMapper.insert(model);	
	}	
	
	//更新对象	
	@Override	
	public int update(Layer model) {	
		return layerMapper.updateByPrimaryKey(model);	
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return layerMapper.deleteByPrimaryKey(id);	
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	/**
	 * 查分层历史
	 *
	 * @param packageId
	 * @return
	 */
	@Override
	public PageInfo<Layer> getLayerHistory(Integer packageId) {
		List<Layer> list = layerMapper.getLayerHistory(packageId);
		return new PageInfo<>(list);
	}

	/**
	 * 资产还款最大日期
	 *
	 * @param assetPackageId
	 * @return
	 */
	@Override
	public Date findMaxDateByPackageId(Integer assetPackageId) {
		return layerMapper.findMaxDateByPackageId(assetPackageId);
	}

	/**
	 * 资产投放最小日期
	 *
	 * @param assetPackageId
	 * @return
	 */
	@Override
	public Date findMinDateByPackageId(Integer assetPackageId) {
		return layerMapper.findMinDateByPackageId(assetPackageId);
	}
}	
