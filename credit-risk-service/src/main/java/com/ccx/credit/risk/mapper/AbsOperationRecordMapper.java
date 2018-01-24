package com.ccx.credit.risk.mapper;

import com.ccx.credit.risk.model.AbsOperationRecord;

import java.util.List;
import java.util.Map;

/**
 * 处理系统操作日志的mapper接口
 */
public interface AbsOperationRecordMapper {

    /**
     * 操作记录 插入操作
     * @param record
     * @return
     */
    int insert(AbsOperationRecord record);

    /**
     * 根据id查找产品
     * @return
     */
    String selectEnterPriseNameById(int id);

    /**
     * 根据id查找资产
     * @return
     */
    String selectAssetNameById(int id);

    /**
     * 根据id查找资产包
     * @return
     */
    String selectAssetsPackageNameById(int id);

    /**
     * 根据id查找角色
     * @return
     */
    String selectRoleNameById(int id);

    /**
     * 根据id查找资产包
     * @return
     */
    String selectUserNameById(int id);


    /**
     * 查找所有记录
     * @return
     */
    List<AbsOperationRecord> findAllRecord(Map<String,Object>paramMap);
}
