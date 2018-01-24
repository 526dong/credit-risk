package com.ccx.credit.risk.model.asset;

import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产创建
 * @author xzd
 * @date 2017/6/24
 */
public class Asset implements Serializable{
	private Integer id;
	
	/*资产申请编号*/
	private String applyCode;
	
	/*资产编号*/
	private String code;
	
	/*资产名称*/
	private String name;
	
	/*暂无*/
	private Integer type;

	/*主体名称*/
	private Map<String, Object> enterpriseInfo;
	
	/*创建人姓名*/
	private String creatorName;
	
	/*创建时间*/
	private Date createDate;
	
	/*更新时间*/
	private Date updateDate;
	
	/*更新id=abs_project_enterprise_relation表的update_id*/
	private Integer updateId;
	
	/*企业id*/
	private Integer enterpriseId;

	/*公司id*/
	private Integer companyId;

	/*是否删除标识：0-否，1-是。默认0.*/
	private Integer deleteFlag;

	/*投放日期*/
	private Date putDate;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getApplyCode() {
		return applyCode;
	}

	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
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
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	
	public String getCreateDate() throws Exception {
		if (!StringUtils.isEmpty(createDate)) {
			return DateUtils.formatDate(createDate);
		}else{
			return null;
		}
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public String getUpdateDate() throws ParseException {
		if (!StringUtils.isEmpty(updateDate)) {
			return DateUtils.formatDate(updateDate);
		}else{
			return null;
		}
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public Integer getUpdateId() {
		return updateId;
	}
	
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
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

	public Map getEnterpriseInfo() {
		return enterpriseInfo;
	}

	public void setEnterpriseInfo(Map enterpriseInfo) {
		this.enterpriseInfo = enterpriseInfo;
	}

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
