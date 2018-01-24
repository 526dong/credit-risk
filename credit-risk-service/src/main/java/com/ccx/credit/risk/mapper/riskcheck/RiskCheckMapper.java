package com.ccx.credit.risk.mapper.riskcheck;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;

public interface RiskCheckMapper {
	List<RiskCheckVO> allSubject(Map<String,Object> params);

	List<RiskCheckVO> historyAllSubject(Map<String,Object> params);

	/*List<RiskCheckVO> pendingApprovalSubject(Map<String,Object> params);
	
	List<RiskCheckVO> alreadyRatedSubject(Map<String,Object> params);
	
	List<RiskCheckVO> beReturnedSubject(Map<String,Object> params);
	
	void updateRatingApproval(Approval approval);
	
	void updateRatingEnterprise(Enterprise enterprise);
	
	void updateRefuseApproval(Approval approval);
	
	void updateRefuseEnterprise(Enterprise enterprise);*/

	//查询评级结果集合
	List findRateResult();

	//查询评级调整理由
    List findRateReason();
}
