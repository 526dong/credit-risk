package com.ccx.credit.risk.api.assetsPackage;

import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.assetsPackage.AssetsPakege;
import com.github.pagehelper.PageInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface AssetsPackageApi {
	
	/**
	 * 
	* @Title: findAssetsPackage 
	* @Description: 根据资产包id查询资产包信息 
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege findAssetsPackage(int assetsPackageId);
	
	/**
	 * 
	 * @Title: findAllAssetsPackageList  
	 * @author: WXN
	 * @Description: 查询资产表列表list（分页）
	 * @param: @param params
	 * @param: @return      
	 * @return: PageInfo<AssetsPakege>      
	 * @throws
	 */
	PageInfo<AssetsPakege> findAllAssetsPackageList(Map<String, Object> params);
	
	/**
	 * 
	* @Title: checkAssetPackageName 
	* @Description: 验证资产包名称是否唯一
	* @param @param assetPackageName
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege checkAssetPackageName(String assetPackageName);
	
	/**
	 * 
	* @Title: checkAssetPackageNo 
	* @Description: 验证资产包编号是否唯一
	* @param @param assetPackageName
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege checkAssetPackageNo(String assetPackageNo);
	
	/**
	 * 
	* @Title: saveAddAssetPackage 
	* @Description: 保存新增的资产包
	* @param @param assetsPakege
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	String saveAddAssetPackage(AssetsPakege assetsPakege);
	
	/**
	 * 
	* @Title: deleteAssetsPackage 
	* @Description: 删除资产包   
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	String deleteAssetsPackage(int assetsPackageId);
	
	/**
	 * 
	* @Title: findAssetsListByPackageId 
	* @Description: 根据资产包id查询当前资产包下所有的资产list（分页）
	* @param @param params
	* @param @return    设定文件 
	* @return PageInfo<Asset>    返回类型 
	* @throws
	 */
	PageInfo<AbsAsset> findAssetsListByPackageId(Map<String, Object> params);
	
	/**
	 * 
	* @Title: deleteAssetsOfPackage 
	* @Description: 删除资产包下的资产
	* @param @param assetsId
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	String deleteAssetsOfPackage(int assetsId,int assetsPackageId);
	
	/**
	 * 
	* @Title: findAllAssetsList 
	* @Description: 查询可以选择的资产list（分页）
	* @param @param params
	* @param @return    设定文件 
	* @return PageInfo<Asset>    返回类型 
	* @throws
	 */
	PageInfo<AbsAsset> findAllAssetsList(Map<String, Object> params);

	/**
	 * 
	* @Title: findAssetsListById 
	* @Description: 查询当前资产包下所有的资产信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<Asset>    返回类型 
	* @throws
	 */
	List<AbsAsset> findAssetsListById(int assetsPackageId);
	
	/**
	 * 
	* @Title: findRepaymentList 
	* @Description: 查询当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> findRepaymentList(int assetsPackageId);
	
	/**
	 * 
	* @Title: countRepaymentList 
	* @Description: 按月统计当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countRepaymentList(int assetsPackageId);
	
	/**
	 * 
	* @Title: countRepaymentList 
	* @Description: 统计当前资产包下所有行业分布
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countAssetsInsdustryList(int assetsPackageId);
	
	/**
	 * 
	* @Title: countAssetsAreaList 
	* @Description: 统计当前资产包下所有区域分布
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countAssetsAreaList(int assetsPackageId);

	/**
	 * 通过资产包id更新资产报名称
	 * @param id 资产包id
	 * @param name 资产包名称
	 * @param assetIdsStr 资产id集合
	 */
	void updateAssetPackageNameById(Integer id, String name, String assetIdsStr);

}
