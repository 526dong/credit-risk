package com.ccx.credit.risk.mapper.enterprise;

import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;

import java.util.List;

public interface EnterpriseReportDataStoreMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EnterpriseReportDataStore record);

    /*插入报表数据*/
    void batchInsert(List<EnterpriseReportDataStore> list);

    EnterpriseReportDataStore selectByPrimaryKey(Integer id);

    /*通过reportId查询子表数据*/
    List<EnterpriseReportDataStore> findByReportId(Integer reportId);

    /*将状态置为不可用-即已经删除状态*/
    int updateDeleteFlagById(Integer id);
}