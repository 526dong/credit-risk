package com.ccx.credit.risk.api;

import com.ccx.credit.risk.model.AbsOperationRecord;

import java.util.List;
import java.util.Map;

/**
 * 处理系统操作日志的service接口
 */
public interface AbsOperationRecordApi {

    /**
     * 操作记录 插入操作
     * @param record
     * @return
     */
    int add(AbsOperationRecord record);

    /**
     * 查找企业名称
     * @param id
     * @return
     */
    String selectEnterPriseNameById(int id);

    /**
     * 查找资产名称
     * @param id
     * @return
     */
    String selectAssetNameById(int id);

    /**
     * 查找资产包名称
     * @param id
     * @return
     */
    String selectAssetsPackageNameById(int id);

    /**
     * 查找角色名称
     * @param id
     * @return
     */
    String selectRoleNameById(int id);

    /**
     * 查找用户名称
     * @param id
     * @return
     */
    String selectUserNameById(int id);


    /**
     * 查找所有日志记录
     * @param paramMap
     * @return
     */
    List<AbsOperationRecord> findAllRecord(Map<String,Object>paramMap);
}
