package com.ccx.credit.risk.mapper.rate;

import com.ccx.credit.risk.model.approval.Approval;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Approval record);

    int insertSelective(Approval record);

    Approval selectByPrimaryKey(Integer id);

    /*受评主体数据库*/
    List<Approval> findAllEvaluate();

    int updateByPrimaryKeySelective(Approval record);

    int updateByPrimaryKey(Approval record);

    //查询审批记录
    Approval selectByAppNo(String appNO);

    //更新编辑标记
    void updateEditFlag(@Param("ratingApplyNum") String appNum, @Param("editFlag") Integer flag);

    //更新状态
    void updateApprovalStatus(@Param("ratingApplyNum") String appNum, @Param("status") Integer status);
}