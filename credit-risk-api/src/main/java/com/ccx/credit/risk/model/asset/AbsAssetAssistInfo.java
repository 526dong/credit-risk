package com.ccx.credit.risk.model.asset;

import java.io.Serializable;

/**
 * @Description 资产辅助信息类-企业/增信措施
 * @author Created by xzd on 2017/12/6.
 */
public class AbsAssetAssistInfo implements Serializable{
    private Integer id;

    private Integer assetId;

    /**
     * 承租企业
     */
    private String enterpriseTenant;

    /**
     * 卖方
     */
    private String enterpriseSeller;

    /**
     * 买方
     */
    private String enterpriseBuyer;

    /**
     * 借款企业
     */
    private String enterpriseBorrow;

    /**
     * 增级企业
     */
    private String enterpriseEnhance;

    /**
     * 其他增信措施
     */
    private String otherMeasureCreditEnhance;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getEnterpriseTenant() {
        return enterpriseTenant;
    }

    public void setEnterpriseTenant(String enterpriseTenant) {
        this.enterpriseTenant = enterpriseTenant == null ? null : enterpriseTenant.trim();
    }

    public String getEnterpriseSeller() {
        return enterpriseSeller;
    }

    public void setEnterpriseSeller(String enterpriseSeller) {
        this.enterpriseSeller = enterpriseSeller;
    }

    public String getEnterpriseBuyer() {
        return enterpriseBuyer;
    }

    public void setEnterpriseBuyer(String enterpriseBuyer) {
        this.enterpriseBuyer = enterpriseBuyer;
    }

    public String getEnterpriseBorrow() {
        return enterpriseBorrow;
    }

    public void setEnterpriseBorrow(String enterpriseBorrow) {
        this.enterpriseBorrow = enterpriseBorrow == null ? null : enterpriseBorrow.trim();
    }

    public String getEnterpriseEnhance() {
        return enterpriseEnhance;
    }

    public void setEnterpriseEnhance(String enterpriseEnhance) {
        this.enterpriseEnhance = enterpriseEnhance == null ? null : enterpriseEnhance.trim();
    }

    public String getOtherMeasureCreditEnhance() {
        return otherMeasureCreditEnhance;
    }

    public void setOtherMeasureCreditEnhance(String otherMeasureCreditEnhance) {
        this.otherMeasureCreditEnhance = otherMeasureCreditEnhance == null ? null : otherMeasureCreditEnhance.trim();
    }
}