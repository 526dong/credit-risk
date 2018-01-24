package com.ccx.credit.risk.service.impl.asset;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.asset.AssetEnterpriseApi;
import com.ccx.credit.risk.mapper.asset.AssetEnterpriseMapper;
import com.ccx.credit.risk.model.asset.AssetEnterprise;
@Service("assetEnterpriseApi")
public class AssetEnterpriseServiceImpI implements AssetEnterpriseApi{
	@Autowired
	AssetEnterpriseMapper dao ;

	@Override
	public void insert(AssetEnterprise assetEnterprise) {
		dao.insert(assetEnterprise);
	}

	@Override
	public void batchInsertEnterprise(List<AssetEnterprise> assetEnterpriseList) {
		dao.batchInsertEnterprise(assetEnterpriseList);
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public void deleteEnterpriseByAssetId(Map<String, Object> params) {
		dao.deleteEnterpriseByAssetId(params);
	}

	@Override
	public void update(AssetEnterprise assetEnterprise) {
		dao.update(assetEnterprise);
	}

	@Override
	public void updateByAsset(AssetEnterprise assetEnterprise) {
		dao.updateByAsset(assetEnterprise);
	}

	@Override
	public AssetEnterprise findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public AssetEnterprise findByEnterpriseId(Map<String, Object> params) {
		return dao.findByEnterpriseId(params);
	}

	@Override
	public List<AssetEnterprise> findEnterpriseByAssetId(Map<String, Object> params) {
		return dao.findEnterpriseByAssetId(params);
	}

}
