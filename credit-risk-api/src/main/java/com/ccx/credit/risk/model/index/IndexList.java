package com.ccx.credit.risk.model.index;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业指标list
 * @author xzd
 * @date 2017/7/7
 */
public class IndexList {
	private List<EnterpriseIndexRelation> indexList = new ArrayList<EnterpriseIndexRelation>();

	public List<EnterpriseIndexRelation> getIndexList() {
		return indexList;
	}

	public void setIndexList(List<EnterpriseIndexRelation> indexList) {
		this.indexList = indexList;
	}

}
