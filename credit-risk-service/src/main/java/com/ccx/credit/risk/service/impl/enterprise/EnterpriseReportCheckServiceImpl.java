package com.ccx.credit.risk.service.impl.enterprise;
	
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportCheckMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportCheck;
import com.ccx.credit.risk.api.enterprise.EnterpriseReportCheckApi;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;	
import java.util.*;	
	
@Service("enterpriseReportCheckApi")
public class EnterpriseReportCheckServiceImpl implements EnterpriseReportCheckApi {
		
	@Autowired	
    private EnterpriseReportCheckMapper enterpriseReportCheckMapper;
		
	//主键获取
	@Override	
	public List<EnterpriseReportCheck> getByReportType(Integer reportType) {
		return enterpriseReportCheckMapper.selectByReportType(reportType);
	}	
}
