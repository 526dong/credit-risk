package com.ccx.credit.risk.api.index;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.index.*;

/**
 * 指标相关功能api
 * @author sunqi
 *
 */
public interface IndexApi {

	//查定性指标
	List<IndexBean> findNatureIndexByElementIds(List<Integer> elementIds);

	void batchInsertIndex(List<EnterpriseIndexRelation> indexList);

	void deleteIndexRelation(Integer enterpriseId);

	//查找指标和规则的值
	List<Map<String, String>> findIndexAndRule(List<Map<String, Object>> paramList);
}
