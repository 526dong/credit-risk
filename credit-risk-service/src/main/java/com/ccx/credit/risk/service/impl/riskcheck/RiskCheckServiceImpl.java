package com.ccx.credit.risk.service.impl.riskcheck;

import com.ccx.credit.risk.api.riskcheck.RiskCheckApi;
import com.ccx.credit.risk.mapper.riskcheck.RiskCheckMapper;
import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("riskCheckServiceImpl")
public class RiskCheckServiceImpl implements RiskCheckApi{

	@Autowired
	RiskCheckMapper dao;

	@Override
	public PageInfo<RiskCheckVO> allSubject(Map<String, Object> params) {
		List<RiskCheckVO> list = dao.allSubject(params);
		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>(list);
		return pages;
	}

	@Override
	public PageInfo<RiskCheckVO> historyAllSubject(Map<String, Object> params) {
		List<RiskCheckVO> list = dao.historyAllSubject(params);
		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>(list);
		return pages;
	}

	/*@Override
	public PageInfo<RiskCheckVO> pendingApprovalSubject(Map<String, Object> params) {
		List<RiskCheckVO> list = dao.pendingApprovalSubject(params);
		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>(list);
		return pages;
	}

	@Override
	public PageInfo<RiskCheckVO> alreadyRatedSubject(Map<String, Object> params) {
		List<RiskCheckVO> list = dao.alreadyRatedSubject(params);
		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>(list);
		return pages;
	}

	@Override
	public PageInfo<RiskCheckVO> beReturnedSubject(Map<String, Object> params) {
		List<RiskCheckVO> list = dao.beReturnedSubject(params);
		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>(list);
		return pages;
	}

	@Override
	public void updateRatingApproval(Approval approval) {
		dao.updateRatingApproval(approval);		
	}

	@Override
	public void updateRatingEnterprise(Enterprise enterprise) {
		dao.updateRatingEnterprise(enterprise);
	}

	@Override
	public void updateRefuseApproval(Approval approval) {
		dao.updateRefuseApproval(approval);
	}

	@Override
	public void updateRefuseEnterprise(Enterprise enterprise) {
		dao.updateRefuseEnterprise(enterprise);
	}*/

	//查询评级结果集合
    @Override
    public List findRateResult() {
        return dao.findRateResult();
    }

	//查询评级调整理由
	@Override
	public List findRateReason() {
		return dao.findRateReason();
	}

}
