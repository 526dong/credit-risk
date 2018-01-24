package com.ccx.credit.risk.mapper.enterprise;

import java.util.List;

import com.ccx.credit.risk.model.enterprise.EnterpriseIndustry;
import org.apache.ibatis.annotations.Param;

/**
 * 企业行业
 * @author xzd
 * @date 2017/6/29
 */
public interface EnterpriseIndustryMapper {
	EnterpriseIndustry findIndustryById(Integer id);
	
	List<EnterpriseIndustry> findAllIndustryByPid(Integer id);

	//通过企业id和企业规模查评分卡id
    Integer getModelIdByIdAndEntType(@Param("industry2Id") int industry2Id, @Param("entType") int entType);
}
