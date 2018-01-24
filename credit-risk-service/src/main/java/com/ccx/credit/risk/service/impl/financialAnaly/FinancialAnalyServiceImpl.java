package com.ccx.credit.risk.service.impl.financialAnaly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.financialAnaly.FinancialAnalyApi;
import com.ccx.credit.risk.mapper.financialAnaly.FinancialAnalyMapper;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.FinancialAnaly;
import com.ccx.credit.risk.model.enterprise.Report;
import com.github.pagehelper.PageInfo;
@Service("FinancialAnalyApi")
public class FinancialAnalyServiceImpl implements FinancialAnalyApi{

	@Autowired
    private FinancialAnalyMapper financialAnalyMapper;
	
	
	/**
	 * 
	 * @Title: FinancialAnalyServiceImpl   
	 * @Description: 查询显示的企业信息 
	 * @param: @param params
	 * @param: @return
	 * @throws
	 */
	@Override
	public PageInfo<Enterprise> findCompanyMsgList(Map<String,Object> params) {
		List<Enterprise> list = financialAnalyMapper.findCompanyMsgList(params);
		PageInfo<Enterprise> pages = new PageInfo<Enterprise>(list);
		return pages;
	}
	
	/**
	 * 
	 * @Title: FinancialAnalyServiceImpl   
	 * @Description: 根据主体id获取到与他关联的报表List    
	 * @param: @param reportId
	 * @param: @param reportStartTime
	 * @param: @param reportEndTime
	 * @param: @param koujing
	 * @param: @return
	 * @throws
	 */
	@Override
	public List<Report> findReportList(int financialID,String reportStartTime,String reportEndTime,String koujing){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("financialID", financialID);
		paramMap.put("reportStartTime", reportStartTime);
		paramMap.put("reportEndTime", reportEndTime);
		paramMap.put("koujing", koujing);
		return financialAnalyMapper.findReportList(paramMap);
	}
	
	/**
	 * 
	 * @Title: FinancialAnalyServiceImpl   
	 * @Description: 通过报表类型获取文件路径以及文件名信息
	 * @param: @param reportType
	 * @param: @return
	 * @throws
	 */
	@Override
	public FinancialAnaly getFinancialTemByType(int reportType){
		return financialAnalyMapper.getFinancialTemByType(reportType);
	}
	
	
	
	
}
 