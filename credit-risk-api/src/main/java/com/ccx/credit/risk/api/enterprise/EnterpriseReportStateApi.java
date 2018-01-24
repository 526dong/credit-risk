package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportStateApi {
		
	//主键获取
	EnterpriseReportState getById(Integer id);	
		
	//获取无参list
	List<EnterpriseReportState> getList();	
		
	//获取有参数list
	List<EnterpriseReportState> getList(EnterpriseReportState model);	
		
	//获取带分页list
	List<EnterpriseReportState> getPageList(Page<EnterpriseReportState> page);
		
	//通过条件获取
	EnterpriseReportState getByModel(EnterpriseReportState model);	
	
	//保存对象
	int save(EnterpriseReportState model);	
	
	//更新对象
	int update(EnterpriseReportState model);	
		
	//删除对象
	int deleteById(Integer id);	
		
	//其他查询
	Map<String, Object> getOther();
}
