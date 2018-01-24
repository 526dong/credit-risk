package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportSheet;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportSheetApi {
		
	//主键获取
	EnterpriseReportSheet getById(Integer id);

	//通过报表类型查询报表子表概况信息
	List<EnterpriseReportSheet> selectByReportType(Integer reportType);

	//通过报表类型、子表名称查询报表子表概况信息
	EnterpriseReportSheet selectByReportSonName(Integer reportType, String reportSonName);
		
	//获取无参list
	List<EnterpriseReportSheet> getList();	
		
	//获取有参数list
	List<EnterpriseReportSheet> getList(EnterpriseReportSheet model);	
		
	//获取带分页list
	List<EnterpriseReportSheet> getPageList(Page<EnterpriseReportSheet> page);
		
	//通过条件获取
	EnterpriseReportSheet getByModel(EnterpriseReportSheet model);	
	
	//保存对象
	int save(EnterpriseReportSheet model);	
	
	//更新对象
	int update(EnterpriseReportSheet model);	
		
	//删除对象
	int deleteById(Integer id);	
		
	//其他查询
	Map<String, Object> getOther();
}
