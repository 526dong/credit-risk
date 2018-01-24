package com.ccx.credit.risk.service.impl.enterprise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.enterprise.EnterpriseIndustryApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseIndustryMapper;
import com.ccx.credit.risk.model.enterprise.EnterpriseIndustry;

@Service("enterpriseIndustryApi")
public class EnterpriseIndustryServiceImpI implements EnterpriseIndustryApi{
	@Autowired
	EnterpriseIndustryMapper dao ;

	@Override
	public EnterpriseIndustry findIndustryById(Integer id) {
		return dao.findIndustryById(id);
	}
	
	@Override
	public List<EnterpriseIndustry> findAllIndustryByPid(Integer id) {
		return dao.findAllIndustryByPid(id);
	}

	//通过企业id和企业规模查评分卡id
	@Override
	public Integer getModelIdByIdAndEntType(int industry2Id, int entType) {
		return dao.getModelIdByIdAndEntType(industry2Id, entType);
	}

}
