package com.ccx.credit.risk.api.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.model.enterprise.Report;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ReportApi {
    void momentSaveOrUpdate (HttpServletRequest request, Report report, Integer enterpriseId);

    void saveReport(HttpServletRequest request, Integer enterpriseId, Report report, Integer reportType,
                   Map<String, Integer> sheetIds, Map<String, List<EnterpriseReportDataStore>> reportSonDataMap);

    void insertRelation(Map<String,Integer> map);
    
    void deleteById(Integer enterpriseId, Integer reportId);

    void deleteRelationByReportId(Integer reportId);

    void update(Report report);
    
    Report findById(Integer id);

    int findByReportTimeAndCal(Integer enterpriseId, String reportTime, Integer cal);

    List<Report> findByEnterpriseId(Integer enterpriseId);

    //查询是否有新报表
    int findNewReportByEnt(Integer enterpriseId);

    //提交评级
    Map<String, Object> submiRate(String rateNo, Integer industryId2) throws Exception;

    //根据ids查报告
    List<Report> submitReportList(List<Integer> idList);

    //
    int calReportHashCode(String reportIds);
}
