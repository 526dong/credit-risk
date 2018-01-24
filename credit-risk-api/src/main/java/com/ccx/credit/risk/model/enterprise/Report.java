package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.ccx.credit.risk.util.DateUtils;

/**
 * 企业报表概况
 * @author xzd
 * @date 2017/6/15
 */
public class Report implements Serializable{

	private Integer id;

	/*报表时间:主要指年份*/
	private String reportTime;
	
	/*口径：1-合并，0-非合并*/
	private Integer cal;
	
	/*报表类型*/
	private Integer type;

	/*报表类型对应的报表名称*/
	private String reportName;

	/*周期：1-年报*/
	private Integer cycle;

	/*是否审计：1-是，0-否*/
	private Integer audit;
	
	/*币种：1-人民币*/
	private Integer currency;
	
	/*审计单位*/
	private String auditUnit;
	
	/*审计意见*/
	private String auditOpinion;

	/*录入状态*/
	private Integer state;

	/*创建人姓名*/
	private String creatorName;
	
	/*创建时间*/
	private Date createDate;

	/*更新时间*/
	private Date updateDate;

	/*企业id*/
	private Integer enterpriseId;

	/*公司id*/
	private Integer companyId;

	/*是否删除：0-否，1-是。默认0*/
	private Integer deleteFlag;

	private Integer approvalStatus;

	private Date CreateDayeTypeDate;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getCal() {
		return cal;
	}

	public void setCal(Integer cal) {
		this.cal = cal;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getAuditUnit() {
		return auditUnit;
	}

	public void setAuditUnit(String auditUnit) {
		this.auditUnit = auditUnit;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateDate() throws Exception {
		if (createDate != null) {
     		return DateUtils.formatDate(createDate);
 		}else{
 			return null;
 		}
	}

	/*获取date类型的时间*/
	public Date getCreateDayeTypeDate() {
		return createDate;
	}

	public void setCreateDayeTypeDate(Date createDayeTypeDate) {
		CreateDayeTypeDate = createDayeTypeDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() throws Exception {
		if (updateDate != null) {
     		return DateUtils.formatDate(updateDate);
 		}else{
 			return null;
 		}
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
}
