package com.ccx.credit.risk.service.impl.assetlayer;

import com.ccx.credit.risk.api.assetlayer.LayerAssetsRelevantApi;
import com.ccx.credit.risk.api.assetlayer.LayerSetupApi;
import com.ccx.credit.risk.mapper.assetlayer.LayerAssetsRelevantMapper;
import com.ccx.credit.risk.mapper.assetlayer.LayerSetupMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.assetlayer.LayerAssetsRelevant;
import com.ccx.credit.risk.model.assetlayer.LayerSetup;
import com.ccx.credit.risk.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
	
/**
 * @author zhaotm
 */
@Service
public class LayerAssetsRelevantServiceImpl implements LayerAssetsRelevantApi {
		
	@Autowired	
    private LayerAssetsRelevantMapper layerAssetsRelevantMapper;

	@Autowired
	private LayerSetupApi layerSetupApi;

	@Autowired
	private LayerSetupMapper layerSetupMapper;
		
	//主键获取	
	@Override	
	public LayerAssetsRelevant getById(Integer id) {
		return layerAssetsRelevantMapper.selectByPrimaryKey(id);
	}	
		
	//获取无参list	
	@Override	
	public List<LayerAssetsRelevant> getList() {	
		return null;	
	}	
		
	//获取有参数list	
	@Override	
	public List<LayerAssetsRelevant> getList(LayerAssetsRelevant model) {	
		return null;	
	}	
		
	//获取带分页list	
	@Override	
	public PageInfo<LayerAssetsRelevant> getPageList(Map<String, Object> parmas) {
		return null;	
	}	
		
	//通过条件获取	
	@Override	
	public LayerAssetsRelevant getByModel(LayerAssetsRelevant model) {	
		return null;	
	}	
	
	//保存对象	
	@Override	
	public int save(LayerAssetsRelevant model) {	
		return layerAssetsRelevantMapper.insert(model);
	}	
	
	//更新对象	
	@Override	
	public int update(LayerAssetsRelevant model) {	
		return layerAssetsRelevantMapper.updateByPrimaryKey(model);
	}	
		
	//删除对象	
	@Override	
	public int deleteById(Integer id) {	
		return layerAssetsRelevantMapper.deleteByPrimaryKey(id);
	}	
		
	//其他查询	
	@Override	
	public Map<String, Object> getOther() {	
		return null;	
	}

	/**
	 * 保存分层相关性
	 */
	@Override
	public Map<String, Integer> saveRelation(LayerAssetsRelevant relation, Map<String, Object> paramMap, String assetIdArr, String relationValueArr) {
		Integer newLayerId = -1;
		Integer newSetupId = -1;
		Integer layerId = relation.getLayerId();
		String status = (String) paramMap.get("status");
		String[] aeeetIds = assetIdArr.split(",");
		List<LayerAssetsRelevant> list = new ArrayList<>();
		Map<String, Integer> resultMap = new HashMap<>();

		if (StringUtils.isBlank(assetIdArr)) {
			return null;
		}

		this.myFillRelation(relation);

		if (aeeetIds.length > 0) {
			if (-1 == layerId.intValue()) {
				//新的layer增加一条分层记录
				newLayerId = layerSetupApi.mySaveNewLayer(paramMap, null);
				relation.setLayerId(newLayerId);

				//新的一条setup记录, 一条level记录
				LayerSetup setup = new LayerSetup();
				setup.setLayerId(newLayerId);
				layerSetupMapper.insert(setup);
				newSetupId = setup.getId();
			} else {
				if ("1".equals(status)) {
					//提交过的分层，新增一条分层记录
					paramMap.put("status", "0");
					newLayerId = layerSetupApi.mySaveNewLayer(paramMap, layerId);
					relation.setLayerId(newLayerId);

					LayerSetup setup = layerSetupMapper.getByLayerId(layerId);
					setup.setId(null);
					setup.setLayerId(newLayerId);
					layerSetupMapper.insert(setup);
					newSetupId = setup.getId();
					//layerSetupApi.myGenNewLevelByOldLayerId(layerId, newLayerId);
				} else {
					//分层未提交，可以继续需改,删除之前设置
					layerAssetsRelevantMapper.deleteByLayerId(layerId.intValue());
				}
			}

			String[] values = relationValueArr.split(",");
			for (int i = 0; i < values.length; i++) {
				String value = values[i];

				if (StringUtils.isNotBlank(value)) {
					Integer assetId = Integer.parseInt(aeeetIds[i]);
					LayerAssetsRelevant relevantSave = new LayerAssetsRelevant(relation.getLayerId(), relation.getAssetPakegeId(), assetId, Integer.parseInt(value), relation.getCreatorName(), relation.getCreateTime());
					list.add(relevantSave);
				}
			}
		}
		if (list.size() > 0) {
			layerAssetsRelevantMapper.insetList(list);
		}


		resultMap.put("newLayerId", newLayerId);
		resultMap.put("newSetupId", newSetupId);
		return resultMap;
	}

	/**
	 * 分层相关性查询
	 *
     * @param params
     */
	@Override
	public List<LayerAssetsRelevant> getAssetList(Map<String, Object> params) {
		List<LayerAssetsRelevant> list = layerAssetsRelevantMapper.getAssetList(params);
		return list;
	}

	/**
	 * 填充作者信息
	 */
	public void myFillRelation(LayerAssetsRelevant relation) {
		Date date = new Date();
		String user = ((User)(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()).getSession().getAttribute("risk_crm_user")).getLoginName();
		relation.setCreateTime(date);
		relation.setCreatorName(user);
	}
}	
