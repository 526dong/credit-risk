package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportModel;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportModelApi {

	//通过报表类型查询报表模板
	List<EnterpriseReportModel> selectByReportType(Integer reportType, Integer reportSonType);

	//主键获取
	EnterpriseReportModel getById(Integer id);

	//获取无参list
	List<EnterpriseReportModel> getList();	
		
	//获取有参数list
	List<EnterpriseReportModel> getList(EnterpriseReportModel model);	
		
	//获取带分页list
	List<EnterpriseReportModel> getPageList(Page<EnterpriseReportModel> page);
		
	//通过条件获取
	EnterpriseReportModel getByModel(EnterpriseReportModel model);	
	
	//保存对象
	int save(EnterpriseReportModel model);	
	
	//更新对象
	int update(EnterpriseReportModel model);	
		
	//删除对象
	int deleteById(Integer id);	
		
	//其他查询
	Map<String, Object> getOther();
}
