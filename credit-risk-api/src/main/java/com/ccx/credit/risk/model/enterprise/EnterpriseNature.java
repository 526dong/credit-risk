package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
import java.util.Date;
/**
 * 企业性质
 * @author xzd
 * @date 2017/7/4
 */
public class EnterpriseNature implements Serializable{
	private static final long serialVersionUID = -1488693998156963542L;
	
	private Integer id;
	
	private String code;//性质编号
	private String name;//性质名称
	
	private String creatorName;//创建人姓名 
	private Date createDate;//创建时间
	
	public EnterpriseNature() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "EnterpriseIndustry [id=" + id + ", code=" + code + ", name=" + name + ", creatorName=" + creatorName
				+ ", createDate=" + createDate + "]";
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

}
