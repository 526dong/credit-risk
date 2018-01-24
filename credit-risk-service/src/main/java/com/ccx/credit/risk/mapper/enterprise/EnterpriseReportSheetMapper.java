package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportSheet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnterpriseReportSheetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportSheet record);

    int insertSelective(EnterpriseReportSheet record);

    EnterpriseReportSheet selectByPrimaryKey(Integer id);

    //通过报表类型查询报表子表概况信息
    List<EnterpriseReportSheet> selectByReportType(Integer reportType);

    //通过报表类型、子表名称查询报表子表概况信息
    EnterpriseReportSheet selectByReportSonName(@Param("reportType") Integer reportType, @Param("reportSonName") String reportSonName);

    int updateByPrimaryKeySelective(EnterpriseReportSheet record);

    int updateByPrimaryKey(EnterpriseReportSheet record);
}