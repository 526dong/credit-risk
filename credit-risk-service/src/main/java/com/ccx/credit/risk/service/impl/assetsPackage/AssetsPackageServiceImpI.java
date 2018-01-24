package com.ccx.credit.risk.service.impl.assetsPackage;

import com.ccx.credit.risk.api.assetsPackage.AssetsPackageApi;
import com.ccx.credit.risk.mapper.assetsPackage.AssetsPackageMapper;
import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.assetsPackage.AssetPackageMsg;
import com.ccx.credit.risk.model.assetsPackage.AssetsPakege;
import com.ccx.credit.risk.util.StringUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service("assetsPackageApi")
public class AssetsPackageServiceImpI implements AssetsPackageApi{
	@Autowired
	AssetsPackageMapper assetsPackageMapper ;

	/**
	 * 
	* @Title: findAssetsPackage 
	* @Description: 根据资产包id查询资产包信息  
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public AssetsPakege findAssetsPackage(int assetsPackageId){
		return assetsPackageMapper.findAssetsPackage(assetsPackageId);
	}
	
	/**
	 * @Title: AssetsPackageServiceImpI   
	 * @Description: 查询资产表列表list（分页） 
	 * @param: @param params
	 * @param: @return
	 * @throws
	 */
	@Override
	public PageInfo<AssetsPakege> findAllAssetsPackageList(Map<String, Object> params) {
		List<AssetsPakege> list = assetsPackageMapper.findAllAssetsPackageList(params);
		PageInfo<AssetsPakege> pages = new PageInfo<AssetsPakege>(list);
		return pages;
	}
	
	/**
	 * 
	* @Title: checkAssetPackageName 
	* @Description: 验证资产包名称是否唯一
	* @param @param assetPackageName
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	@Override
	public AssetsPakege checkAssetPackageName(String assetPackageName){
		return assetsPackageMapper.checkAssetPackageName(assetPackageName);
	}
	
	/**
	 * 
	* @Title: checkAssetPackageNo 
	* @Description: 验证资产包编号是否唯一
	* @param @param assetPackageNo
	* @param @return    设定文件 
	* @return AssetsPakege    返回类型 
	* @throws
	 */
	@Override
	public AssetsPakege checkAssetPackageNo(String assetPackageNo){
		return assetsPackageMapper.checkAssetPackageNo(assetPackageNo);
	}
	
	/**
	 * 
	* @Title: saveAddAssetPackage 
	* @Description: 保存新增的资产包
	* @param @param assetsPakege
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public String saveAddAssetPackage(AssetsPakege assetsPakege){
		String result = "999";
		int flag = assetsPackageMapper.saveAddAssetPackage(assetsPakege);
		if( flag > 0 ){
			result = "1000";
		}
		return result;
	}
	
	/**
	 * 
	* @Title: deleteAssetsPackage 
	* @Description: 删除资产包 
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public String deleteAssetsPackage(int assetsPackageId){
		String result = "999";
		//删除资产包于资产的关联
		assetsPackageMapper.deleteByAssetPackageId(assetsPackageId);
		//删除资产包
		int flag = assetsPackageMapper.deleteAssetsPackage(assetsPackageId);
		if( flag > 0 ){
			result = "1000";
		}
		return result;
	}
	
	/**
	 * 
	* @Title: findAssetsListByPackageId 
	* @Description: 根据资产包id查询当前资产包下所有的资产list（分页）
	* @param @param params
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public PageInfo<AbsAsset> findAssetsListByPackageId(Map<String, Object> params){
		List<AbsAsset> list = assetsPackageMapper.findAssetsListByPackageId(params);
		PageInfo<AbsAsset> pages = new PageInfo<AbsAsset>(list);
		return pages;
	}
	
	/**
	 * 
	* @Title: deleteAssetsOfPackage 
	* @Description: 删除资产包下的资产
	* @param @param assetsId
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public String deleteAssetsOfPackage(int assetsId,int assetsPackageId){
		String result = "999";
		int flag = assetsPackageMapper.deleteAssetsOfPackage(assetsId,assetsPackageId);
		if( flag > 0 ){
			result = "1000";
		}
		return result;
	}
	
	/**
	 * 
	* @Title: findAllAssetsList 
	* @Description: 查询可以选择的资产list（分页） 
	* @param @param params
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public PageInfo<AbsAsset> findAllAssetsList(Map<String, Object> params){
		PageInfo<AbsAsset> pages =null;
		try {
			List<AbsAsset> list = assetsPackageMapper.findAllAssetsList(params);
			pages=new PageInfo(list);
		}catch (Exception e){
			e.printStackTrace();
		}
		return pages;
	}
	
	/**
	 * 
	* @Title: findAssetsListById 
	* @Description: 查询当前资产包下所有的资产信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public List<AbsAsset> findAssetsListById(int assetsPackageId){
		return assetsPackageMapper.findAssetsListById(assetsPackageId);
	}
	
	/**
	 * 
	* @Title: findRepaymentList 
	* @Description: 查询当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public List<LinkedHashMap<String, Object>> findRepaymentList(int assetsPackageId){
		return assetsPackageMapper.findRepaymentList(assetsPackageId);
	}
	
	/**
	 * 
	* @Title: countRepaymentList 
	* @Description: 按月统计当前资产包下所有的还款计划信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public List<LinkedHashMap<String, Object>> countRepaymentList(int assetsPackageId){
		return assetsPackageMapper.countRepaymentList(assetsPackageId);
	}
	
	/**
	 * 
	* @Title: countAssetsInsdustryList 
	* @Description: 统计当前资产包下所有行业信息
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public List<LinkedHashMap<String, Object>> countAssetsInsdustryList(int assetsPackageId){
		return assetsPackageMapper.countAssetsInsdustryList(assetsPackageId);
	}
	
	/**
	 * 
	* @Title: countAssetsAreaList 
	* @Description: 统计当前资产包下所有区域分布
	* @param @param assetsPackageId
	* @param @return    设定文件 
	* @throws
	 */
	@Override
	public List<LinkedHashMap<String, Object>> countAssetsAreaList(int assetsPackageId){
		return assetsPackageMapper.countAssetsAreaList(assetsPackageId);
	}

	/**
	 * 通过资产包id更新资产报名称
	 * @param id 资产包id
	 * @param name 资产包名称
	 * @param assetIdsStr 资产id集合
	 */
	@Override
	public void updateAssetPackageNameById(Integer id, String name, String assetIdsStr) {
		//一、保存资产包修改
		assetsPackageMapper.updateAssetPackageNameById(id, name);

		//空过滤
		if (!StringUtils.isEmpty(assetIdsStr)) {
			//二、删除资产包和资产现有关联
			assetsPackageMapper.deleteByAssetPackageId(id);

			//三、批量添加资产包和资产的关联信息
			String[] assetIds = assetIdsStr.split(",");

			List<AssetPackageMsg> assetPackageMsgList = new ArrayList<>();

			for (int i = 0;i < assetIds.length;i++) {
				if (!assetIds[i].isEmpty()) {
					AssetPackageMsg assetPackageMsg = new AssetPackageMsg();

					assetPackageMsg.setAssetId(Integer.parseInt(assetIds[i]));
					assetPackageMsg.setAssetPackageId(id);

					assetPackageMsgList.add(assetPackageMsg);
				}
			}

			assetsPackageMapper.batchInsertAsset(assetPackageMsgList);
		}
	}

}
