package com.ccx.credit.risk.model.asset;

import com.ccx.credit.risk.util.DateUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 资产辅助信息类-企业/增信措施
 * @author Created by xzd on 2017/12/6.
 */
public class AbsAsset implements Serializable{
    private Integer id;

    private String name;

    private String code;

    /**
     * 资产申请编号
     */
    private String applyCode;

    /**
     * 资产类型：0-租赁债权，1-保理债权，2-保理债权
     */
    private Integer type;

    /**
     * 业务类型
     */
    private Integer businessType;
    /**
     * 字典表解析
     */
    private String businessTypeName;
    /**
     * 页面填写
     */
    private String businessName;

    /**
     * 承租企业
     */
    private String tenantEnt;

    /**
     * 卖方
     */
    private String sellerEnt;

    /**
     * 买方
     */
    private String buyerEnt;

    /**
     * 借款企业
     */
    private String borrowEnt;

    /**
     * 资产状态：1-进行中，0-已结束
     */
    private Integer state;
    /**
     * 当选择已结束时，填写已结束理由
     */
    private String assetEndReason;

    private BigDecimal bondPrincipal;

    private Integer timeLimit;

    private Integer interestRateType;

    private BigDecimal annualInterestRate;

    private BigDecimal irr;

    private String feeCollectTypeAndRate;

    private Date putDate;

    private String putDateStr;

    /**
     * 主体企业
     */
    private Integer entId;
    private String entName;
    /**
     * 资产级别
     */
    private String level;

    private Integer entShadowId;
    private String entShadowName;
    /**
     * 资产影子级别
     */
    private String shadowLevel;

    /**
     * 省份
     */
    private String province;

    /**
     * 一级行业、二级行业
     */
    private String industry1;
    private String industry2;

    private String operator;

    private Date operateDate;

    private Date updateDate;

    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public String getTenantEnt() {
        return tenantEnt;
    }

    public void setTenantEnt(String tenantEnt) {
        this.tenantEnt = tenantEnt;
    }

    public String getSellerEnt() {
        return sellerEnt;
    }

    public void setSellerEnt(String sellerEnt) {
        this.sellerEnt = sellerEnt;
    }

    public String getBuyerEnt() {
        return buyerEnt;
    }

    public void setBuyerEnt(String buyerEnt) {
        this.buyerEnt = buyerEnt;
    }

    public String getBorrowEnt() {
        return borrowEnt;
    }

    public void setBorrowEnt(String borrowEnt) {
        this.borrowEnt = borrowEnt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAssetEndReason() {
        return assetEndReason;
    }

    public void setAssetEndReason(String assetEndReason) {
        this.assetEndReason = assetEndReason;
    }

    public BigDecimal getBondPrincipal() {
        return bondPrincipal;
    }

    public void setBondPrincipal(BigDecimal bondPrincipal) {
        this.bondPrincipal = bondPrincipal;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getInterestRateType() {
        return interestRateType;
    }

    public void setInterestRateType(Integer interestRateType) {
        this.interestRateType = interestRateType;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public BigDecimal getIrr() {
        return irr;
    }

    public void setIrr(BigDecimal irr) {
        this.irr = irr;
    }

    public String getFeeCollectTypeAndRate() {
        return feeCollectTypeAndRate;
    }

    public void setFeeCollectTypeAndRate(String feeCollectTypeAndRate) {
        this.feeCollectTypeAndRate = feeCollectTypeAndRate == null ? null : feeCollectTypeAndRate.trim();
    }

    public Date getPutDate() {
        return putDate;
    }

    public void setPutDate(Date putDate) {
        this.putDate = putDate;
    }

    public String getPutDateStr() {
        return putDateStr;
    }

    public void setPutDateStr(String putDateStr) {
        this.putDateStr = putDateStr;
    }

    public Integer getEntId() {
        return entId;
    }

    public void setEntId(Integer entId) {
        this.entId = entId;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getEntShadowId() {
        return entShadowId;
    }

    public void setEntShadowId(Integer entShadowId) {
        this.entShadowId = entShadowId;
    }

    public String getEntShadowName() {
        return entShadowName;
    }

    public void setEntShadowName(String entShadowName) {
        this.entShadowName = entShadowName;
    }

    public String getShadowLevel() {
        return shadowLevel;
    }

    public void setShadowLevel(String shadowLevel) {
        this.shadowLevel = shadowLevel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIndustry1() {
        return industry1;
    }

    public void setIndustry1(String industry1) {
        this.industry1 = industry1;
    }

    public String getIndustry2() {
        return industry2;
    }

    public void setIndustry2(String industry2) {
        this.industry2 = industry2;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getOperateDate() throws Exception {
        if (operateDate != null) {
            return DateUtils.formatDate(operateDate);
        }else{
            return null;
        }
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}