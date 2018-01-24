package com.ccx.credit.risk.api.asset;
	
import com.ccx.credit.risk.model.asset.AbsAssetEnhanceCredit;
import com.ccx.credit.risk.util.Page;

import java.util.*;
	
public interface AbsAssetEnhanceCreditApi {
		
	//主键获取	
	AbsAssetEnhanceCredit getById(Integer id);

	/**
	 * 通过父节点查询
	 * @param pid
	 * @return
	 */
	List<AbsAssetEnhanceCredit> findAllByPid(Integer pid);
		
	//获取无参list	
	List<AbsAssetEnhanceCredit> getList();	
		
	//获取有参数list	
	List<AbsAssetEnhanceCredit> getList(AbsAssetEnhanceCredit model);	
		
	//获取带分页list	
	List<AbsAssetEnhanceCredit> getPageList(Page<AbsAssetEnhanceCredit> page);
		
	//通过条件获取	
	AbsAssetEnhanceCredit getByModel(AbsAssetEnhanceCredit model);	
	
	//保存对象	
	int save(AbsAssetEnhanceCredit model);	
	
	//更新对象	
	int update(AbsAssetEnhanceCredit model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
