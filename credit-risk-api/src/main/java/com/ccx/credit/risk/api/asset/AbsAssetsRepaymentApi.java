package com.ccx.credit.risk.api.asset;

import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;
import com.ccx.credit.risk.util.Page;

import java.util.List;
import java.util.Map;
	
public interface AbsAssetsRepaymentApi {
		
	//主键获取	
	AbsAssetsRepayment getById(Integer id);
		
	//获取无参list	
	List<AbsAssetsRepayment> getList();	
		
	//获取有参数list	
	List<AbsAssetsRepayment> getList(AbsAssetsRepayment model);	
		
	//获取带分页list	
	List<AbsAssetsRepayment> getPageList(Page<AbsAssetsRepayment> page);
		
	//通过条件获取	
	AbsAssetsRepayment getByModel(AbsAssetsRepayment model);	
	
	//保存对象	
	int save(AbsAssetsRepayment model);	
	
	//更新对象	
	int update(AbsAssetsRepayment model);	
		
	//删除对象	
	int deleteById(Integer id);	
		
	//其他查询	
	Map<String, Object> getOther();	
}	
