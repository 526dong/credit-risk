package com.ccx.credit.risk.api.init;

import com.ccx.credit.risk.model.index.IndexBean;
import com.ccx.credit.risk.model.index.IndexRule;
import com.ccx.credit.risk.model.index.IndustryIndex;

import java.util.List;


/**
 * 查询指标关系相关信息
 * @author sunqi
 *
 */
public interface InitIndexParamApi {

	List<IndustryIndex> selectIndustryIndex();
	
	List<IndexBean> selectIndexData();
	
	List<IndexRule> selectIndexRule();
}
