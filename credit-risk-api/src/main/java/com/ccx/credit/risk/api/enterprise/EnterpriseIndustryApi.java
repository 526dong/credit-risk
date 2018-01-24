package com.ccx.credit.risk.api.enterprise;

import java.util.List;

import com.ccx.credit.risk.model.enterprise.EnterpriseIndustry;

/**
 * 企业行业
 * @author xzd
 * @date 2017/6/29
 */
public interface EnterpriseIndustryApi {
	EnterpriseIndustry findIndustryById(Integer id);
	
	List<EnterpriseIndustry> findAllIndustryByPid(Integer id);

	//通过企业id和企业规模查评分卡id
    Integer getModelIdByIdAndEntType(int industry2Id, int entType);
}
