package com.ccx.credit.risk.model.index;

import java.io.Serializable;

/**
 * 财报字段实体类
 * @author sunqi
 *
 */
public class ReportColumnBean implements Serializable{

	private static final long serialVersionUID = -383379526540656176L;
	private String id;
	private String columnNameCN;//中文字段名称
	private String columnNameUS;//英文字段名称
	private String dataType;//财报字段类型。1：资产类数据。2：负债及所有者权益类数据。3：损益类数据。4：现金流类数据。5：现金流类补充资料。6：财务报表附注信息
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getColumnNameCN() {
		return columnNameCN;
	}
	public void setColumnNameCN(String columnNameCN) {
		this.columnNameCN = columnNameCN;
	}
	public String getColumnNameUS() {
		return columnNameUS;
	}
	public void setColumnNameUS(String columnNameUS) {
		this.columnNameUS = columnNameUS;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportColumnBean [id=");
		builder.append(id);
		builder.append(", columnNameCN=");
		builder.append(columnNameCN);
		builder.append(", columnNameUS=");
		builder.append(columnNameUS);
		builder.append(", dataType=");
		builder.append(dataType);
		builder.append("]");
		return builder.toString();
	}
}
