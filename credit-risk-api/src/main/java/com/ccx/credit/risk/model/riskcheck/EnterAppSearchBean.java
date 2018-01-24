package com.ccx.credit.risk.model.riskcheck;

import java.io.Serializable;

/**
 * 企业审批查询条件实体
 * @author sunqi
 *
 */
public class EnterAppSearchBean implements Serializable{

	private static final long serialVersionUID = 8693824991392461371L;
	
	private String approvalStatus;//审批状态：0：待提交审批；1：提交审批(评级中)；2：已评级；3：已拒绝
	private String ratingType;//评级类型 （0：新评级。1：跟踪评级）
	private String ratingResult;//评级结果：AAA—CCC
	public String getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public String getRatingType() {
		return ratingType;
	}
	public void setRatingType(String ratingType) {
		this.ratingType = ratingType;
	}
	public String getRatingResult() {
		return ratingResult;
	}
	public void setRatingResult(String ratingResult) {
		this.ratingResult = ratingResult;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RatingResultSearchBean [approvalStatus=");
		builder.append(approvalStatus);
		builder.append(", ratingType=");
		builder.append(ratingType);
		builder.append(", ratingResult=");
		builder.append(ratingResult);
		builder.append("]");
		return builder.toString();
	}
}
