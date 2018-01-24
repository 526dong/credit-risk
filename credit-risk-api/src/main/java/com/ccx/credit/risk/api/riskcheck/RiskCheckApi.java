package com.ccx.credit.risk.api.riskcheck;

import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RiskCheckApi {

    PageInfo<RiskCheckVO> allSubject(Map<String,Object> params);

    PageInfo<RiskCheckVO> historyAllSubject(Map<String,Object> params);

	/*PageInfo<RiskCheckVO> pendingApprovalSubject(Map<String,Object> params);
	
    PageInfo<RiskCheckVO> alreadyRatedSubject(Map<String,Object> params);
	
	PageInfo<RiskCheckVO> beReturnedSubject(Map<String,Object> params);
	
	void updateRatingApproval(Approval approval);
	
	void updateRatingEnterprise(Enterprise enterprise);
	
	void updateRefuseApproval(Approval approval);
	
	void updateRefuseEnterprise(Enterprise enterprise);*/

	//查询评级结果集合
	List findRateResult();

	//查询评级调整理由
    List findRateReason();
}
