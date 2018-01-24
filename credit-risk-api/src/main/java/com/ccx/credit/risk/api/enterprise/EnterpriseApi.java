package com.ccx.credit.risk.api.enterprise;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseNature;
import com.ccx.credit.risk.model.vo.enterprise.SelectEnterpriseVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

public interface EnterpriseApi {
    void insert(Enterprise enterprise);

    void deleteById(Integer id);
    
    void deleteRelationByEnterpriseId(Integer id);

    void update(Enterprise enterprise);

    void updateBySelect (Enterprise enterprise);

    Enterprise findById(Integer id);

    //查询所有的报表时间的最大值
    List<Map<String, Object>> findAllReportTime(Integer enterpriseId);

    int findByName(String name);

    int findByCreditCode(String creditCode);
    
    PageInfo<Enterprise> findAll(Map<String,Object> params);
    
    //未提交企业主体
    PageInfo<Enterprise> findAllUnCommit(Map<String,Object> params);
    
    //已提交企业主体
    PageInfo<Enterprise> findAllCommit(Map<String,Object> params);

    //已评级企业主体
    PageInfo<Enterprise> findAllRated();

    //已评级 不管完成不完成 添加新年报
    PageInfo<Enterprise> findRated();
    
    EnterpriseNature findNatureById(Integer id);

    List<EnterpriseNature> findAllNature();
    
    //查询受评主体数据库信息
    PageInfo<Map<String, Object>> findAllEvaluate(Map<String,Object> params);

    PageInfo<Map<String, Object>> findEvaluateHistory(Map<String, Object> params);
    
    Map<String, String> selectEnterpriseIndexAndRules(Integer id);

    //查询定量指标是否存在
    int selectRegularIndex(Integer id);

    void updateEnterpriseType(Integer enterpriseId);
}
