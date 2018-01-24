package com.ccx.credit.risk.mapper.enterprise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportModel;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Param;

import com.ccx.credit.risk.model.enterprise.Report;

@Component
public interface ReportMapper {
    void insert(Report report);
    
    void insertRelation(Map<String,Integer> map);

    void deleteById(Integer id);

    int findByReportTimeAndCal(@Param("enterpriseId")Integer enterpriseId, @Param("reportTime")String reportTime, @Param("cal")Integer cal);

    void deleteRelationByReportId(Integer reportId);
    
    void update(Report report);

    /*通过id修改删除状态为1：标识已经删除*/
    void updateDeleteFlagById(Integer id);
    
    Report findById(Integer id);
    
    List<Report> findByEnterpriseId(Integer enterpriseId);

    //查询是否有新报表
    int findNewReportByEnt(Integer enterpriseId);

    //根据ids查报告
    List<Report> findReportListByIds(List<Integer> ids);

    //查需要提交的报告
    List<Report> findApprovalReportByEntId(@Param("enterpriseId") Integer enterpriseId, @Param("approvalStatus") Integer approvalStatus, @Param("cal") Integer cal);
}