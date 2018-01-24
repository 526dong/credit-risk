package com.ccx.credit.risk.model.riskcheck;

import java.io.Serializable;

/**
 * 企业评级审批实体
 * @author sunqi
 *
 */
public class EnterPriseApprovalBean implements Serializable{

	private static final long serialVersionUID = -8054456648827065852L;
	private String id;
	private String enterPriseId;
	private String reportId;
	private String ratingApplyNum;
	private String initiator;
	private String initiateTime;
	private String approver;
	private String approvalTime;
	private String approvalStatus;
	private String preRatingResult;
	private String ratingResult;
	private String refuseReason;
	private String ratingType;
	private String companyId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEnterPriseId() {
		return enterPriseId;
	}
	public void setEnterPriseId(String enterPriseId) {
		this.enterPriseId = enterPriseId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getRatingApplyNum() {
		return ratingApplyNum;
	}
	public void setRatingApplyNum(String ratingApplyNum) {
		this.ratingApplyNum = ratingApplyNum;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public String getInitiateTime() {
		return initiateTime;
	}
	public void setInitiateTime(String initiateTime) {
		this.initiateTime = initiateTime;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getApprovalTime() {
		return approvalTime;
	}
	public void setApprovalTime(String approvalTime) {
		this.approvalTime = approvalTime;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getPreRatingResult() {
		return preRatingResult;
	}
	public void setPreRatingResult(String preRatingResult) {
		this.preRatingResult = preRatingResult;
	}
	public String getRatingResult() {
		return ratingResult;
	}
	public void setRatingResult(String ratingResult) {
		this.ratingResult = ratingResult;
	}
	public String getRefuseReason() {
		return refuseReason;
	}
	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}
	public String getRatingType() {
		return ratingType;
	}
	public void setRatingType(String ratingType) {
		this.ratingType = ratingType;
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
		builder.append("EnterPriseApprovalBean [id=");
		builder.append(id);
		builder.append(", enterPriseId=");
		builder.append(enterPriseId);
		builder.append(", reportId=");
		builder.append(reportId);
		builder.append(", ratingApplyNum=");
		builder.append(ratingApplyNum);
		builder.append(", initiator=");
		builder.append(initiator);
		builder.append(", initiateTime=");
		builder.append(initiateTime);
		builder.append(", approver=");
		builder.append(approver);
		builder.append(", approvalTime=");
		builder.append(approvalTime);
		builder.append(", approvalStatus=");
		builder.append(approvalStatus);
		builder.append(", preRatingResult=");
		builder.append(preRatingResult);
		builder.append(", ratingResult=");
		builder.append(ratingResult);
		builder.append(", refuseReason=");
		builder.append(refuseReason);
		builder.append(", ratingType=");
		builder.append(ratingType);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append("]");
		return builder.toString();
	}
}
