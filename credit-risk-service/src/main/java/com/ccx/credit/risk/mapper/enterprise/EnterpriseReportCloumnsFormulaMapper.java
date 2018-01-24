package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportCloumnsFormula;

public interface EnterpriseReportCloumnsFormulaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportCloumnsFormula record);

    int insertSelective(EnterpriseReportCloumnsFormula record);

    EnterpriseReportCloumnsFormula selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EnterpriseReportCloumnsFormula record);

    int updateByPrimaryKey(EnterpriseReportCloumnsFormula record);
}