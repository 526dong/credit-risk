package com.ccx.credit.risk.api.asset;
	
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.asset.AssetCount;
import com.github.pagehelper.PageInfo;
	
public interface AbsAssetApi {
		
	//主键获取	
	AbsAsset getById(HttpServletRequest request, Integer id);

	int findByApplyCode(String applyCode);

	int validateName(String name);

	int validateCode(String code);

	//获取无参list	
	List<AbsAsset> getList();	
		
	//获取有参数list	
	List<AbsAsset> getList(AbsAsset model);	
		
	//获取带分页list	
	PageInfo<AbsAsset> getPageList(Map<String,Object> params);

	List<Map<String, Object>> findAllRateEnt();

	List<Map<String, Object>> findAllBusinessType(Integer assetType);
		
	//保存对象
	void addOrUpdate(String asset, String baseEnt, String enhanceEnt, String enhanceCredit, String cashFlow);
	
	//更新对象	
	int update(AbsAsset model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//根据资产类型统计行业地区等分布
	Map<String, Object> analyseDimCount(AssetCount assetCount);
	
	//统计资产基本信息
	Map<String, Object> assetMessCount(AssetCount assetCount);
}
