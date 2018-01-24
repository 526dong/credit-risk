package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;

import java.util.List;

public interface EnterpriseReportStateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportState record);

    int batchInsert(List<EnterpriseReportState> list);

    int insertSelective(EnterpriseReportState record);

    EnterpriseReportState selectByPrimaryKey(Integer id);

    /*通过reportId查询报表子表状态list*/
    List<EnterpriseReportState> selectByReportId(Integer reportId);

    int updateByPrimaryKeySelective(EnterpriseReportState record);

    int updateByPrimaryKey(EnterpriseReportState record);
}