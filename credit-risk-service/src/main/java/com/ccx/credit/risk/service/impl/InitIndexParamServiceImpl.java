package com.ccx.credit.risk.service.impl;

import com.ccx.credit.risk.api.init.InitIndexParamApi;
import com.ccx.credit.risk.mapper.init.InitIndexParamMapper;
import com.ccx.credit.risk.model.index.IndexBean;
import com.ccx.credit.risk.model.index.IndexRule;
import com.ccx.credit.risk.model.index.IndustryIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("initIndexParamApi")
public class InitIndexParamServiceImpl implements InitIndexParamApi{

	@Autowired
	InitIndexParamMapper mapper ;
	
	@Override
	public List<IndustryIndex> selectIndustryIndex() {
		return mapper.selectIndustryIndex();
	}

	@Override
	public List<IndexBean> selectIndexData() {
		return mapper.selectIndexData();
	}

	@Override
	public List<IndexRule> selectIndexRule() {
		return mapper.selectIndexRule();
	}

}
