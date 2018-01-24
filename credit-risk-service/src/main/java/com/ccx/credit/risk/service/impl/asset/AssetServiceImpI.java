package com.ccx.credit.risk.service.impl.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.asset.AssetApi;
import com.ccx.credit.risk.mapper.asset.AssetMapper;
import com.ccx.credit.risk.model.asset.Asset;
import com.github.pagehelper.PageInfo;
@Service("assetApi")
public class AssetServiceImpI implements AssetApi{

	@Autowired
	AssetMapper dao ;

	@Override
	public void insert(Asset asset) {
		dao.insert(asset);
	}
	
	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);

		/*通过资产id删除资产包关联信息*/
		dao.deleteAssetPackageAssetId(id);
	}

	@Override
	public void update(Asset asset) {
		dao.update(asset);
	}
	
	@Override
	public Asset findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public int findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public int findByCode(String code) {
		return dao.findByCode(code);
	}

	@Override
	public int findByApplyCode(String applyCode) {
		return dao.findByApplyCode(applyCode);
	}

	@Override
	public List<Asset> findAllAsset() {
		return dao.findAllAsset();
	}
	
	@Override
	public PageInfo<Asset> findAll(Map<String, Object> params) {
		List<Asset> list = dao.findAll(params);
		PageInfo<Asset> pages = new PageInfo<Asset>(list);
		return pages;
	}

	@Override
	public List<User> findAllUser() {
		return dao.findAllUser();
	}

	/**
	 * 根据ids查资产
	 *
	 * @param assetIds
	 * @return
	 */
	@Override
	public PageInfo<Map<String, Object>> findByIds(String assetIds) {
		List<Integer> idList = new ArrayList<>();

		for (String id: assetIds.split(",")) {
			idList.add(Integer.parseInt(id));
		}
		List<Map<String, Object>> assetList = dao.findByIdList(idList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(assetList);

		return pageInfo;
	}

}
