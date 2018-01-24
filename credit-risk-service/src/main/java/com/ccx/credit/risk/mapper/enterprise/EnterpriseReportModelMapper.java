package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface EnterpriseReportModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportModel record);

    int insertSelective(EnterpriseReportModel record);

    EnterpriseReportModel selectByPrimaryKey(Integer id);

    //通过报表类型查询报表模板
    List<EnterpriseReportModel> selectByReportType(@Param("reportType") Integer reportType, @Param("reportSonType") Integer reportSonType);

    int updateByPrimaryKeySelective(EnterpriseReportModel record);

    int updateByPrimaryKey(EnterpriseReportModel record);
}