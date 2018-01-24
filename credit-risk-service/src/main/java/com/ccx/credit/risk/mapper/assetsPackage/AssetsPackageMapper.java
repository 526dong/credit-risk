package com.ccx.credit.risk.mapper.assetsPackage;

import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.assetsPackage.AssetPackageMsg;
import com.ccx.credit.risk.model.assetsPackage.AssetsPakege;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: AssetsPackageMapper 
* @Description: 表数据库控制层接口
* @author WXN 
* @date 2017年10月23日 上午11:04:29 
*
 */
public interface AssetsPackageMapper {

	/**
	 * 
	* @Title: findAssetsPackage 
	* @Description: 根据资产包id查询资产包信息  
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege findAssetsPackage(@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	 * @Title: findAllAssetsPackageList  
	 * @author: WXN
	 * @Description: 查询资产表列表list（分页） 
	 * @param: @param params
	 * @param: @return      
	 * @return: List<AssetsPakege>      
	 * @throws
	 */
	List<AssetsPakege> findAllAssetsPackageList(Map<String, Object> params);
	
	/**
	 * 
	* @Title: checkAssetPackageNameOrId 
	* @Description: 验证资产包名称是否唯一
	* @param @param map
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege checkAssetPackageName(@Param("assetPackageName")String assetPackageName);
	
	/**
	 * 
	* @Title: checkAssetPackageNameOrId 
	* @Description: 验证资产包编号是否唯一
	* @param @param map
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	AssetsPakege checkAssetPackageNo(@Param("assetPackageNo")String assetPackageNo);
	
	/**
	 * 
	* @Title: saveAddAssetPackage 
	* @Description: 保存新增的资产包
	* @param @param assetsPakege
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	int saveAddAssetPackage(AssetsPakege assetsPakege);
	
	/**
	 * 
	* @Title: deleteAssetsPackage 
	* @Description: 删除资产包
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	int deleteAssetsPackage(int assetsPackageId);
	
	/**
	 * 通过包id查资产
	 * @param assetPakegeId
	 * @return
	 */
	List<AbsAsset> getAssetListById(Integer assetPakegeId);
	
	/**
	 * 
	* @Title: findAssetsListByPackageId 
	* @Description: 根据资产包id查询当前资产包下所有的资产list（分页）
	* @param @param params
	* @param @return    设定文件 
	* @return List<Asset>    返回类型 
	* @throws
	 */
	List<AbsAsset> findAssetsListByPackageId(Map<String, Object> params);
	
	/**
	 * 
	* @Title: deleteAssetsOfPackage 
	* @Description: 删除资产包下的资产
	* @param @param assetsId
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	int deleteAssetsOfPackage(@Param("assetsId")int assetsId,@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	* @Title: findAllAssetsList 
	* @Description: 查询可以选择的资产list（分页）  
	* @param @param params
	* @param @return    设定文件 
	* @return List<Asset>    返回类型 
	* @throws
	 */
	List<AbsAsset> findAllAssetsList(Map<String, Object> params);
	
	/**
	 * 
	* @Title: findAssetsListById 
	* @Description: 查询当前资产包下所有的资产信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<Asset>    返回类型 
	* @throws
	 */
	List<AbsAsset> findAssetsListById(@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	* @Title: findRepaymentList 
	* @Description: 查询当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> findRepaymentList(@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	* @Title: countRepaymentList 
	* @Description: 按月统计当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countRepaymentList(@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	* @Title: countAssetsInsdustryList 
	* @Description: 统计当前资产包下所有行业信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countAssetsInsdustryList(@Param("assetsPackageId")int assetsPackageId);
	
	/**
	 * 
	* @Title: countAssetsAreaList 
	* @Description: 统计当前资产包下所有区域分布
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @return List<LinkedHashMap<String,Object>>    返回类型 
	* @throws
	 */
	List<LinkedHashMap<String, Object>> countAssetsAreaList(@Param("assetsPackageId")int assetsPackageId);

	/**
	 * 通过资产包id更新资产报名称
	 * @param id 资产包id
	 * @param name 资产包名称
	 */
	void updateAssetPackageNameById(@Param("id")Integer id, @Param("name")String name);

	/**
	 * 批量添加资产和资产包关联信息
	 * @param list 关联表list
	 */
	void batchInsertAsset(List<AssetPackageMsg> list);

	/**
	 * 通过资产包id删除资产包和资产关联
	 * @param id
	 */
	void deleteByAssetPackageId(@Param("id")Integer id);
}
