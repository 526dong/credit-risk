package com.ccx.credit.risk.mapper.gainreport;


import com.ccx.credit.risk.model.enterprise.Report;

import java.util.List;
import java.util.Map;

/**
 * lilong
 */
public interface GainReportMapper {
	//获取sheet信息
	List<Map<String,Object>>  getModleSheepList(Integer typeId);
	//获取字段信息
	List<Map<String,Object>>  getModleFiledpList(Integer typeId);
	//根据子表id获取子表信息
	Map<String ,Object> getSheetMsgBysheetId(Map map);
	//获取报表概况信息
	Report findById(Integer reportId);
	//获取报表状态表信息
	List<Map<String,Object>> findReportSheetStatusByid(Integer reportId);
	//获取报表值集合
	List<Map<String,Object>> findValuesByreportid(Integer reportId);
	//获取公式集合
	List<Map<String,Object>> findFormulasBytypeId(Integer typeId);

}
