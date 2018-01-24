package com.ccx.credit.risk.service.impl.assetlayer;	

import com.ccx.credit.risk.api.assetlayer.LayerResultApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerResultMapper;
import com.ccx.credit.risk.model.assetlayer.LayerResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
/**
 * @author zhaotm
 */
@Service
public class LayerResultServiceImpl implements LayerResultApi {
		
	@Autowired	
    private LayerResultMapper layerResultMapper;
		
	//主键获取	
	@Override	
	public LayerResult getById(Integer id) {
		return layerResultMapper.selectByPrimaryKey(id);
	}	
		
	//获取无参list	
	@Override	
	public List<LayerResult> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<LayerResult> getList(LayerResult model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public PageInfo<LayerResult> getPageList(Map<String, Object> params) {
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public LayerResult getByModel(LayerResult model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(LayerResult model) {	
		return layerResultMapper.insert(model);
	}	
	
	//更新对象	
	@Override	
	public int update(LayerResult model) {	
		return layerResultMapper.updateByPrimaryKey(model);
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return layerResultMapper.deleteByPrimaryKey(id);
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	@Override
	public List<LayerResult> selectListByLayerId(Integer layerId) {
		return layerResultMapper.selectListByLayerId(layerId);
	}
}	
