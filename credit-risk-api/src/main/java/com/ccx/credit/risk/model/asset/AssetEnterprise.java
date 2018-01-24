package com.ccx.credit.risk.model.asset;

import java.util.Date;
/**
 * 资产企业关联表
 * @author xzd
 * @date 2017/6/24
 */
public class AssetEnterprise{
	private Integer id;
	
	/*创建时间*/
	private Date createTime;
	
	/*人员类型：借款人-0，增级方-1*/
	private String personType;
	
	/*添加主体名称*/
	private String enterpriseName;
	
	/*是否承担全部债务*/
	private Integer affordAllDebt;
	
	/*增级企业类型：1：担保方，2：差补方，3：共同债务人，4：卖方(有追保理)，5：其他*/
	private Integer type;
	
	/*资产id*/
	private Integer assetId;
	
	/*企业id*/
	private Integer enterpriseId;
	
	/*公司id*/
	private Integer companyId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}
	
	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAffordAllDebt() {
		return affordAllDebt;
	}

	public void setAffordAllDebt(Integer affordAllDebt) {
		this.affordAllDebt = affordAllDebt;
	}

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
