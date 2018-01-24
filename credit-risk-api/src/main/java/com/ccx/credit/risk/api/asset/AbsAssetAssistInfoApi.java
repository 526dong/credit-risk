package com.ccx.credit.risk.api.asset;

import com.ccx.credit.risk.model.asset.AbsAssetAssistInfo;

/**
 * @Description 资产辅助信息
 * @author Created by xzd on 2017/12/8.
 */
public interface AbsAssetAssistInfoApi {

	int deleteById(Integer id);

	int insert(AbsAssetAssistInfo assistInfo);

	AbsAssetAssistInfo getById(Integer id);

	AbsAssetAssistInfo getByAssetId(Integer assetId);

	int update(AbsAssetAssistInfo assistInfo);


}
