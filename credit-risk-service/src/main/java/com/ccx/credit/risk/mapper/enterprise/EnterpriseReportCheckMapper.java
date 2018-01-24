package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportCheck;

import java.util.List;

public interface EnterpriseReportCheckMapper {
    List<EnterpriseReportCheck> selectByReportType(Integer reportType);
}