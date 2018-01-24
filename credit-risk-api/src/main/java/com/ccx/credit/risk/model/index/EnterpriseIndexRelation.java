package com.ccx.credit.risk.model.index;

import java.io.Serializable;
/**
 * 企业指标关联实体
 * @author xzd
 * @date 2017/7/7
 */
public class EnterpriseIndexRelation implements Serializable{
	private static final long serialVersionUID = 3376991862198433663L;

	private Integer id;
	
	/*企业id*/
	private Integer enterpriseId;
	/*指标id*/
	private Integer indexId;
	
	/*指标名称*/
	private String indexName;
	
	/*指标数据*/
	private String indexData;
	
	/*指标规则id*/
	private Integer ruleId;
	/*公司id*/
	private Integer companyId;
	
	public EnterpriseIndexRelation() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "EnterpriseIndexRelation [id=" + id + ", enterpriseId=" + enterpriseId + ", indexId=" + indexId
				+ ", indexName=" + indexName + ", indexData=" + indexData + ", ruleId=" + ruleId + ", companyId="
				+ companyId + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getIndexId() {
		return indexId;
	}

	public void setIndexId(Integer indexId) {
		this.indexId = indexId;
	}
	
	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getIndexData() {
		return indexData;
	}

	public void setIndexData(String indexData) {
		this.indexData = indexData;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
}
