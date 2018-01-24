package com.ccx.credit.risk.service.impl;

import com.ccx.credit.risk.api.AbsOperationRecordApi;
import com.ccx.credit.risk.mapper.AbsOperationRecordMapper;
import com.ccx.credit.risk.model.AbsOperationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 系统日志操作的service实现类
 */
@Service("absOperationRecordApi")
public class AbsOperationRecordImpl implements AbsOperationRecordApi {

    /**注入mapper*/
    @Autowired
    private AbsOperationRecordMapper absOperationRecordMapper;


    /**
     *  操作日志添加操作
     * @param record
     * @return
     */
    @Override
    public int add(AbsOperationRecord record) {

        return absOperationRecordMapper.insert(record);
    }

    /**+
     * 查找企业名称
     * @param id
     * @return
     */
    @Override
    public String selectEnterPriseNameById(int id) {
        return absOperationRecordMapper.selectEnterPriseNameById(id);
    }

    /**
     * 查找资产名称
     * @param id
     * @return
     */
    @Override
    public String selectAssetNameById(int id) {
        return absOperationRecordMapper.selectAssetNameById(id);
    }

    /**
     * 查找资产包名称
     * @param id
     * @return
     */
    @Override
    public String selectAssetsPackageNameById(int id) {
        return absOperationRecordMapper.selectAssetsPackageNameById(id);
    }

    /**
     * 查询角色名称
     * @param id
     * @return
     */
    @Override
    public String selectRoleNameById(int id) {
        return absOperationRecordMapper.selectRoleNameById(id);
    }

    /**
     * 查询用户名称
     * @param id
     * @return
     */
    @Override
    public String selectUserNameById(int id) {
        return absOperationRecordMapper.selectUserNameById(id);
    }

    /**
     * 查询所有的操作日志
     * @return
     */
    @Override
    public List<AbsOperationRecord> findAllRecord(Map<String,Object>paramMap) {
        return absOperationRecordMapper.findAllRecord(paramMap);
    }
}
