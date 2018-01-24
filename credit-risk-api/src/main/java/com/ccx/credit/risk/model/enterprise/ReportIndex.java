package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;

/**
 * 企业定性指标实体
 * @author sunqi
 *
 */
public class ReportIndex implements Serializable{

	private static final long serialVersionUID = 1486399944248043471L;
	private String id;
	private String reportId;
	private String indexId;
	private String indexName;
	private String indexData;
	private String valueId;
	private String companyId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
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
	public String getValueId() {
		return valueId;
	}
	public void setValueId(String valueId) {
		this.valueId = valueId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportIndex [id=");
		builder.append(id);
		builder.append(", reportId=");
		builder.append(reportId);
		builder.append(", indexId=");
		builder.append(indexId);
		builder.append(", indexName=");
		builder.append(indexName);
		builder.append(", indexData=");
		builder.append(indexData);
		builder.append(", valueId=");
		builder.append(valueId);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append("]");
		return builder.toString();
	}

}
