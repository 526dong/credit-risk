package com.ccx.credit.risk.service.impl.asset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.asset.AssetOperateApi;
import com.ccx.credit.risk.mapper.asset.AssetOperateMapper;
import com.ccx.credit.risk.model.asset.AssetOperate;
@Service("assetOperateApi")
public class AssetOperateServiceImpI implements AssetOperateApi{
	@Autowired
	AssetOperateMapper dao ;

	@Override
	public void insert(AssetOperate assetOperate) {
		dao.insert(assetOperate);
	}

	@Override
	public List<AssetOperate> findByAssetId(Integer id) {
		return dao.findByAssetId(id);
	}

}
