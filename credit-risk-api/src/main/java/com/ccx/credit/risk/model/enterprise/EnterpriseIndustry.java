package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
import java.util.Date;
/**
 * 企业行业
 * @author xzd
 * @date 2017/6/29
 */
public class EnterpriseIndustry implements Serializable{
	private static final long serialVersionUID = -8155295978294677831L;
	
	private Integer id;
	private String code;//行业编号：一级行业，二级行业
	private String name;//行业名称：一级行业，二级行业
	
	private String creatorName;//创建人姓名 
	private Date createDate;//创建时间
	
	private Integer pid;//父节点id
	
	private Integer backId;//后台行业id

	private Integer modelId0;

	private Integer modelId1;

	private Integer modelId2;
	
	public EnterpriseIndustry() {
		super();
	}

	@Override
	public String toString() {
		return "EnterpriseIndustry [id=" + id + ", code=" + code + ", name=" + name + ", creatorName=" + creatorName
				+ ", createDate=" + createDate + ", pid=" + pid + ", backId=" + backId + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getBackId() {
		return backId;
	}

	public void setBackId(Integer backId) {
		this.backId = backId;
	}

	public Integer getModelId0() {
		return modelId0;
	}

	public void setModelId0(Integer modelId0) {
		this.modelId0 = modelId0;
	}

	public Integer getModelId1() {
		return modelId1;
	}

	public void setModelId1(Integer modelId1) {
		this.modelId1 = modelId1;
	}

	public Integer getModelId2() {
		return modelId2;
	}

	public void setModelId2(Integer modelId2) {
		this.modelId2 = modelId2;
	}
}
