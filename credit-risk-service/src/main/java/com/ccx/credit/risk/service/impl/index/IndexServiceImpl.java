package com.ccx.credit.risk.service.impl.index;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.model.index.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.index.IndexApi;
import com.ccx.credit.risk.mapper.index.IndexMapper;
import com.ccx.credit.risk.model.index.IndexRule;

@Service("indexApi")
public class IndexServiceImpl implements IndexApi{

	@Autowired
	IndexMapper mapper;

	//查定性指标
	@Override
	public List<IndexBean> findNatureIndexByElementIds(List<Integer> elementIds) {
		return mapper.findNatureIndexByElementIds(elementIds);
	}

	@Override
	public void batchInsertIndex(List<EnterpriseIndexRelation> indexList) {
		mapper.batchInsertIndex(indexList);
	}

	@Override
	public void deleteIndexRelation(Integer enterpriseId) {
		mapper.deleteIndexRelation(enterpriseId);
	}

	/*xzd add end*/

	//查找指标和规则的值
	@Override
	public List<Map<String, String>> findIndexAndRule(List<Map<String, Object>> paramList) {
		return mapper.findIndexAndRule(paramList);
	}
}
