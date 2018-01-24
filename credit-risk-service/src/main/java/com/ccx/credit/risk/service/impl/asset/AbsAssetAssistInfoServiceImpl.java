package com.ccx.credit.risk.service.impl.asset;

import com.alibaba.fastjson.JSONArray;
import com.ccx.credit.risk.api.asset.AbsAssetApi;
import com.ccx.credit.risk.api.asset.AbsAssetAssistInfoApi;
import com.ccx.credit.risk.mapper.asset.AbsAssetAssistInfoMapper;
import com.ccx.credit.risk.mapper.asset.AbsAssetMapper;
import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.asset.AbsAssetAssistInfo;
import com.ccx.credit.risk.model.asset.AssetEnterpriseBaseInfo;
import com.ccx.credit.risk.util.JsonUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("absAssetAssistInfoApi")
/**
 * @Description 资产辅助信息
 * @author Created by xzd on 2017/12/8.
 */
public class AbsAssetAssistInfoServiceImpl implements AbsAssetAssistInfoApi {
	@Autowired
	private AbsAssetAssistInfoMapper assistInfoMapper;

	@Override
	public int deleteById(Integer id) {
		return assistInfoMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(AbsAssetAssistInfo assistInfo) {
		return assistInfoMapper.insert(assistInfo);
	}

	@Override
	public AbsAssetAssistInfo getById(Integer id) {
		return assistInfoMapper.selectByPrimaryKey(id);
	}

	@Override
	public AbsAssetAssistInfo getByAssetId(Integer assetId) {
		return assistInfoMapper.getByAssetId(assetId);
	}

	@Override
	public int update(AbsAssetAssistInfo assistInfo) {
		return assistInfoMapper.updateByPrimaryKey(assistInfo);
	}
}
