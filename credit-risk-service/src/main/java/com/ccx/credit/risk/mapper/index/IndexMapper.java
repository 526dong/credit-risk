package com.ccx.credit.risk.mapper.index;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.index.*;

/**
 * 指标相关功能表数据库控制层
 * @author sunqi
 *
 */
public interface IndexMapper {

	public List<IndexBean> findIndexByElementId(Integer elementId);

	//
	List<IndexBean> findAllIndexByElementIds(List<Integer> elementIds);
	
	/*xzd add start*/
	void batchInsertIndex(List<EnterpriseIndexRelation> indexList);

	void deleteIndexRelation(Integer enterpriseId);
	/*xzd add end*/

	//查找指标和规则的值
	List<Map<String, String>> findIndexAndRule(List<Map<String, Object>> paramList);

	List<IndexBean> findNatureIndexByElementIds(List<Integer> elementIds);

	//查询指标不包含ruleList
    List<IndexBean> findIndexWithoutRuleByElementId(Integer eleId);

	//查询评级申请指标（定性）不包含ruleList
	List<IndexBean> findNatureIndexWithoutRuleByElementId(Integer eleId);
}
