package com.ccx.credit.risk.api.enterprise;
	
import com.ccx.credit.risk.model.enterprise.EnterpriseReportCheck;
import com.github.pagehelper.Page;

import java.util.*;	
	
public interface EnterpriseReportCheckApi {

	//主键获取
	List<EnterpriseReportCheck> getByReportType(Integer reportType);
}
