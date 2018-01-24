package com.ccx.credit.risk.mapper.enterprise;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.approval.Approval;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseNature;
import com.ccx.credit.risk.model.vo.enterprise.SelectEnterpriseVO;

@Component
public interface EnterpriseMapper {
    void insert(Enterprise enterprise);

    void deleteById(Integer id);
    
    void deleteRelationByEnterpriseId(Integer id);

    /*通过id修改删除状态为1：标识已经删除*/
    void updateDeleteFlag(Integer id);

    void update(Enterprise enterprise);

    void updateBySelect (Enterprise enterprise);
    
    Enterprise findById(Integer id);
    //查询所有的报表时间
    List<Map<String, Object>> findAllReportTime(Integer enterpriseId);

    int findByName(String name);
    /*代码标识唯一*/
    int findByCreditCode(String creditCode);

    List<Enterprise> findAll(Map<String,Object> params);
    
    //未提交企业主体
    List<Enterprise> findAllUnCommit(Map<String,Object> params);
    
    //已提交企业主体
    List<Enterprise> findAllCommit(Map<String,Object> params);

    //已评级企业主体
    List<Enterprise> findAllRated();

    //已评级 不管完成不完成 添加新年报
    List<Enterprise> findRated();
    
    EnterpriseNature findNatureById(Integer id);

    List<EnterpriseNature> findAllNature();
    
    List<Map<String,Object>> findAllEvaluate(Map<String,Object> params);

    List<Map<String,Object>> findEvaluateHistory(Map<String,Object> params);

    //企业的指标id和规则id集合
    Map<String, String> selectEnterpriseIndexAndRules(Integer id);

    //查询定量指标是否存在
    int selectRegularIndex(@Param("id") Integer id);

    void updateEnterpriseType(Integer enterpriseId);

    //更新报告关系
    void updateEnterpriseRe(Approval approval);

    /**
     * 更新主体提交状态
     * @param enterpriseBak
     */
    void updateApprovalStatus(Enterprise enterpriseBak);

    void updateRefuseFlag(Enterprise enterprise);
}