package com.ccx.credit.risk.mapper.init;

import com.ccx.credit.risk.model.index.IndexBean;
import com.ccx.credit.risk.model.index.IndexRule;
import com.ccx.credit.risk.model.index.IndustryIndex;

import java.util.List;


public interface InitIndexParamMapper {

	List<IndustryIndex> selectIndustryIndex();
	
	List<IndexBean> selectIndexData();
	
	List<IndexRule> selectIndexRule();
}
