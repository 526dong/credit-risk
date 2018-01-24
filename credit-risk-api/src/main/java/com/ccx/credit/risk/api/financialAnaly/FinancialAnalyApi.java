package com.ccx.credit.risk.api.financialAnaly;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.FinancialAnaly;
import com.ccx.credit.risk.model.enterprise.Report;
import com.github.pagehelper.PageInfo;

public interface FinancialAnalyApi {
	
	/**
	 * 
	 * @Title: findCompanyMsgList  
	 * @author: WXN
	 * @Description: 查询显示的企业信息 
	 * @param: @param params
	 * @param: @return      
	 * @return: PageInfo<Enterprise>      
	 * @throws
	 */
	PageInfo<Enterprise> findCompanyMsgList(Map<String,Object> params);
	
	/**
	 * 
	 * @Title: findReportList  
	 * @author: WXN
	 * @Description: 根据主体id获取到与他关联的报表List  
	 * @param: @param financialID
	 * @param: @param reportStartTime
	 * @param: @param reportEndTime
	 * @param: @param koujing
	 * @param: @return      
	 * @return: List<Report>      
	 * @throws
	 */
	List<Report> findReportList(int financialID,String reportStartTime,String reportEndTime,String koujing);
	
	/**
	 * 
	 * @Title: getFinancialTemByType  
	 * @author: WXN
	 * @Description: 通过报表类型获取文件路径以及文件名信息  
	 * @param: @param reportType
	 * @param: @return      
	 * @return: FinancialAnaly      
	 * @throws
	 */
	FinancialAnaly getFinancialTemByType(int reportType);

    
}
