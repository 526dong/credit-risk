package com.ccx.credit.risk.model.rate;

import java.io.Serializable;
/**
 * 评级结果实体
 * @author sunqi
 *
 */
public class RatingResult implements Serializable{

	private static final long serialVersionUID = 4339340900585952744L;
	
	private String id;
	private String ratingApplyNum;
	private String entId;
	private String elementId;
	private String elementName;
	private String indexId;
	private String indexName;
	private String indexData;
	private String value;
	private String degree;
	private String finalFlag;
	private String regularIndexFlag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRatingApplyNum() {
		return ratingApplyNum;
	}
	public void setRatingApplyNum(String ratingApplyNum) {
		this.ratingApplyNum = ratingApplyNum;
	}
	public String getEntId() {
		return entId;
	}
	public void setEntId(String entId) {
		this.entId = entId;
	}
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getFinalFlag() {
		return finalFlag;
	}
	public void setFinalFlag(String finalFlag) {
		this.finalFlag = finalFlag;
	}
	public String getRegularIndexFlag() {
		return regularIndexFlag;
	}
	public void setRegularIndexFlag(String regularIndexFlag) {
		this.regularIndexFlag = regularIndexFlag;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RatingResult [id=");
		builder.append(id);
		builder.append(", ratingApplyNum=");
		builder.append(ratingApplyNum);
		builder.append(", entId=");
		builder.append(entId);
		builder.append(", elementId=");
		builder.append(elementId);
		builder.append(", elementName=");
		builder.append(elementName);
		builder.append(", indexId=");
		builder.append(indexId);
		builder.append(", indexName=");
		builder.append(indexName);
		builder.append(", indexData=");
		builder.append(indexData);
		builder.append(", value=");
		builder.append(value);
		builder.append(", degree=");
		builder.append(degree);
		builder.append(", finalFlag=");
		builder.append(finalFlag);
		builder.append(", regularIndexFlag=");
		builder.append(regularIndexFlag);
		builder.append("]");
		return builder.toString();
	}
	
}
