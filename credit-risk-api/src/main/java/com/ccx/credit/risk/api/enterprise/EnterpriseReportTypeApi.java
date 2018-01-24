package com.ccx.credit.risk.api.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportType;
import com.github.pagehelper.Page;

import java.util.*;

public interface EnterpriseReportTypeApi {

	//主键获取
	EnterpriseReportType getById(Integer id);

	//通过主键获取名称
	String getNameById(Integer id);

	//获取无参list
	List<EnterpriseReportType> getList();

	//获取有参数list
	List<EnterpriseReportType> getList(EnterpriseReportType model);

	//获取带分页list
	List<EnterpriseReportType> getPageList(Page<EnterpriseReportType> page);

	//通过条件获取
	EnterpriseReportType getByModel(EnterpriseReportType model);

	//保存对象
	int save(EnterpriseReportType model);

	//更新对象
	int update(EnterpriseReportType model);

	//删除对象
	int deleteById(Integer id);

	//其他查询
	Map<String, Object> getOther();
}
