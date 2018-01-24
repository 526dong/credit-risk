package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportCloumnsFormula;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportCloumnsFormulaApi {
		
	//主键获取
	EnterpriseReportCloumnsFormula getById(Integer id);	
		
	//获取无参list
	List<EnterpriseReportCloumnsFormula> getList();	
		
	//获取有参数list
	List<EnterpriseReportCloumnsFormula> getList(EnterpriseReportCloumnsFormula model);	
		
	//获取带分页list
	List<EnterpriseReportCloumnsFormula> getPageList(Page<EnterpriseReportCloumnsFormula> page);
		
	//通过条件获取
	EnterpriseReportCloumnsFormula getByModel(EnterpriseReportCloumnsFormula model);	
	
	//保存对象
	int save(EnterpriseReportCloumnsFormula model);	
	
	//更新对象
	int update(EnterpriseReportCloumnsFormula model);	
		
	//删除对象
	int deleteById(Integer id);	
		
	//其他查询
	Map<String, Object> getOther();
}
