package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportDataStoreApi {
		
	//主键获取
	EnterpriseReportDataStore getById(Integer id);

	/*通过reportType查询子表数据*/
	List<EnterpriseReportDataStore> findByReportId(Integer reportType);
		
	//获取无参list
	List<EnterpriseReportDataStore> getList();	
		
	//获取有参数list
	List<EnterpriseReportDataStore> getList(EnterpriseReportDataStore model);	
		
	//获取带分页list
	List<EnterpriseReportDataStore> getPageList(Page<EnterpriseReportDataStore> page);
		
	//通过条件获取
	EnterpriseReportDataStore getByModel(EnterpriseReportDataStore model);	
	
	//保存对象
	int save(EnterpriseReportDataStore model);

	/*插入报表数据*/
	void batchInsert(Map<String, Integer> sheetIdMap, List<EnterpriseReportDataStore> list, Integer reportId, Integer reportType);
	
	/*将状态置为不可用-即已经删除状态*/
	int updateDeleteColumn(Integer id);
		
	//删除对象
	int deleteById(Integer id);	
		
	//其他查询
	Map<String, Object> getOther();
}
