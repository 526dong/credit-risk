package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportType;

import java.util.List;

public interface EnterpriseReportTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportType record);

    int insertSelective(EnterpriseReportType record);

    EnterpriseReportType selectByPrimaryKey(Integer id);

    //通过主键获取名称
    String getNameById(Integer id);

    int updateByPrimaryKeySelective(EnterpriseReportType record);

    int updateByPrimaryKey(EnterpriseReportType record);

    List<EnterpriseReportType> selectAll();
}