package com.ccx.credit.risk.model.approval;

import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.JsonUtils;
import com.ccx.credit.risk.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author sgs
 * @date 2017/6/21
 */
public class Approval implements Serializable{
	private Integer id;

	private Integer enterpriseId;

	private String reportIds;

	private String ratingApplyNum;

	private String initiator;

	private Date initiateTime;

	private String approver;

	private Date approvalTime;

	private String approvalTimeStr;

	private Integer approvalStatus;

	private String preRatingResult;

	private String ratingResult;

	private String refuseReason;

	private Integer ratingType;

	private Integer companyId;

	private String actTaskId;

	private String indexIds;

	private String ruleIds;

	private String adjustContent;

	private String adjustChange;

	//影子评级结果
	private String shadowRatingResult;

	//影子评级时间
    private Date shadowApprovalTime;

    //提交时的主体信息
    private Enterprise enterprise;

    //提交时的主体信息json
	private String enterpriseJson;

	//提交指标json
	private String approvalIndexNameAndValueJson;

	//提交时候的口径
	private Integer cal;

	//评级最新报表
	private String rateReport;

	//提交时报告数据
	private Integer reportHashCode;

	//退回修改标记
	private Integer editFlag;

	//提交时的模板ID
	private Integer modelId;

	//审批的报告ids
	private String rateReportIds;

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

	public String getReportIds() {
		return reportIds;
	}

	public void setReportIds(String reportIds) {
		this.reportIds = reportIds == null ? null : reportIds.trim();
	}

	public String getRatingApplyNum() {
		return ratingApplyNum;
	}

	public void setRatingApplyNum(String ratingApplyNum) {
		this.ratingApplyNum = ratingApplyNum == null ? null : ratingApplyNum.trim();
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator == null ? null : initiator.trim();
	}

	public Date getInitiateTime() {
		return initiateTime;
	}

	public void setInitiateTime(Date initiateTime) {
		this.initiateTime = initiateTime;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver == null ? null : approver.trim();
	}

	public String getApprovalTime() throws Exception {
		if (!StringUtils.isEmpty(approvalTime)) {
			return DateUtils.formatDate(approvalTime);
		}else{
			return null;
		}
	}

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getPreRatingResult() {
		return preRatingResult;
	}

	public void setPreRatingResult(String preRatingResult) {
		this.preRatingResult = preRatingResult == null ? null : preRatingResult.trim();
	}

	public String getRatingResult() {
		return ratingResult;
	}

	public void setRatingResult(String ratingResult) {
		this.ratingResult = ratingResult == null ? null : ratingResult.trim();
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason == null ? null : refuseReason.trim();
	}

	public Integer getRatingType() {
		return ratingType;
	}

	public void setRatingType(Integer ratingType) {
		this.ratingType = ratingType;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getActTaskId() {
		return actTaskId;
	}

	public void setActTaskId(String actTaskId) {
		this.actTaskId = actTaskId == null ? null : actTaskId.trim();
	}

	public String getIndexIds() {
		return indexIds;
	}

	public void setIndexIds(String indexIds) {
		this.indexIds = indexIds == null ? null : indexIds.trim();
	}

	public String getRuleIds() {
		return ruleIds;
	}

	public void setRuleIds(String ruleIds) {
		this.ruleIds = ruleIds == null ? null : ruleIds.trim();
	}

	public String getAdjustContent() {
		return adjustContent;
	}

	public void setAdjustContent(String adjustContent) {
		this.adjustContent = adjustContent == null ? null : adjustContent.trim();
	}

	public String getAdjustChange() {
		return adjustChange;
	}

	public void setAdjustChange(String adjustChange) {
		this.adjustChange = adjustChange;
	}

	public String getApprovalTimeStr() {
		String str = "";

		try {
			str = DateUtils.formatDate(approvalTime);
		} catch (Exception e) {
		}
		return str;
	}

	public String getShadowRatingResult() {
		return shadowRatingResult;
	}

	public void setShadowRatingResult(String shadowRatingResult) {
		this.shadowRatingResult = shadowRatingResult;
	}

	public Date getShadowApprovalTime() {
		return shadowApprovalTime;
	}

	public void setShadowApprovalTime(Date shadowApprovalTime) {
		this.shadowApprovalTime = shadowApprovalTime;
	}

	public Enterprise getEnterprise() {
		Enterprise enterprise = null;

		if (null == enterpriseJson) {
			return null;
		}
		try {
			enterprise = JsonUtils.parse(this.enterpriseJson, Enterprise.class);
		} catch (Exception e) {
			//e.printStackTrace();
			enterprise = new Enterprise();
		}
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public String getEnterpriseJson() {
		return enterpriseJson;
	}

	public void setEnterpriseJson(String enterpriseJson) {
		this.enterpriseJson = enterpriseJson;
	}

	public String getApprovalIndexNameAndValueJson() {
		return StringUtils.isNotBlank(approvalIndexNameAndValueJson)? approvalIndexNameAndValueJson : "[]";
	}

	public void setApprovalIndexNameAndValueJson(String approvalIndexNameAndValueJson) {
		this.approvalIndexNameAndValueJson = approvalIndexNameAndValueJson;
	}

	public Integer getReportHashCode() {
		return reportHashCode;
	}

	public void setReportHashCode(Integer reportHashCode) {
		this.reportHashCode = reportHashCode;
	}

	public Integer getCal() {
		return cal;
	}

	public void setCal(Integer cal) {
		this.cal = cal;
	}

	public Integer getEditFlag() {
		return editFlag;
	}

	public void setEditFlag(Integer editFlag) {
		this.editFlag = editFlag;
	}

	public String getRateReport() {
		return rateReport;
	}

	public void setRateReport(String rateReport) {
		this.rateReport = rateReport;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getRateReportIds() {
		return rateReportIds;
	}

	public void setRateReportIds(String rateReportIds) {
		this.rateReportIds = rateReportIds;
	}
}
