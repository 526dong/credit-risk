package com.ccx.credit.risk.model.index;

import java.io.Serializable;
/**
 * 行业指标映射关系 实体类
 * @author sunqi
 *
 */
public class IndustryIndex implements Serializable{

	private static final long serialVersionUID = -5718104414366625186L;
	
	private String id;
	private String industryCode;//行业代码
	private String industryName;//行业名称
	private String industryCodeOne;//一级行业代码
	private	String industryNameOne;//一级行业名称
	private String indexList;//指标集合
	private String elementList;//元素集合
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIndustryCode() {
		return industryCode;
	}
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	public String getIndustryName() {
		return industryName;
	}
	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}
	public String getIndustryCodeOne() {
		return industryCodeOne;
	}
	public void setIndustryCodeOne(String industryCodeOne) {
		this.industryCodeOne = industryCodeOne;
	}
	public String getIndustryNameOne() {
		return industryNameOne;
	}
	public void setIndustryNameOne(String industryNameOne) {
		this.industryNameOne = industryNameOne;
	}
	public String getIndexList() {
		return indexList;
	}
	public void setIndexList(String indexList) {
		this.indexList = indexList;
	}
	public String getElementList() {
		return elementList;
	}
	public void setElementList(String elementList) {
		this.elementList = elementList;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndustryIndex [id=");
		builder.append(id);
		builder.append(", industryCode=");
		builder.append(industryCode);
		builder.append(", industryName=");
		builder.append(industryName);
		builder.append(", industryCodeOne=");
		builder.append(industryCodeOne);
		builder.append(", industryNameOne=");
		builder.append(industryNameOne);
		builder.append(", indexList=");
		builder.append(indexList);
		builder.append(", elementList=");
		builder.append(elementList);
		builder.append("]");
		return builder.toString();
	}
	
}
