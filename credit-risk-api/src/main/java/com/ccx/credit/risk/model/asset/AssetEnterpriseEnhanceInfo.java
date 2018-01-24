package com.ccx.credit.risk.model.asset;

import java.io.Serializable;

/**
 * @author Created by xzd on 2017/12/6.
 * @Description 承租企业
 */
public class AssetEnterpriseEnhanceInfo implements Serializable{
    private String enhanceEntId;

    private String enhanceEntName;

    /**
     * 企业类型
     */
    private String enhanceEntType;

    /**
     * 承担/担保比例
     */
    private String enhanceDebtProportion;

    /**
     * 承担企业级别
     */
    private String enhanceRateResult;

    /**
     * 影子级别
     */
    private String enhanceShadowRateResult;

    /**
     * 省份
     */
    private String enhanceEntProvince;
    /**
     * 一级行业、二级行业
     */
    private String enhanceEntIndustry1;
    private String enhanceEntIndustry2;

    public String getEnhanceEntId() {
        return enhanceEntId;
    }

    public void setEnhanceEntId(String enhanceEntId) {
        this.enhanceEntId = enhanceEntId;
    }

    public String getEnhanceEntName() {
        return enhanceEntName;
    }

    public void setEnhanceEntName(String enhanceEntName) {
        this.enhanceEntName = enhanceEntName;
    }

    public String getEnhanceEntType() {
        return enhanceEntType;
    }

    public void setEnhanceEntType(String enhanceEntType) {
        this.enhanceEntType = enhanceEntType;
    }

    public String getEnhanceDebtProportion() {
        return enhanceDebtProportion;
    }

    public void setEnhanceDebtProportion(String enhanceDebtProportion) {
        this.enhanceDebtProportion = enhanceDebtProportion;
    }

    public String getEnhanceRateResult() {
        return enhanceRateResult;
    }

    public void setEnhanceRateResult(String enhanceRateResult) {
        this.enhanceRateResult = enhanceRateResult;
    }

    public String getEnhanceShadowRateResult() {
        return enhanceShadowRateResult;
    }

    public void setEnhanceShadowRateResult(String enhanceShadowRateResult) {
        this.enhanceShadowRateResult = enhanceShadowRateResult;
    }

    public String getEnhanceEntProvince() {
        return enhanceEntProvince;
    }

    public void setEnhanceEntProvince(String enhanceEntProvince) {
        this.enhanceEntProvince = enhanceEntProvince;
    }

    public String getEnhanceEntIndustry1() {
        return enhanceEntIndustry1;
    }

    public void setEnhanceEntIndustry1(String enhanceEntIndustry1) {
        this.enhanceEntIndustry1 = enhanceEntIndustry1;
    }

    public String getEnhanceEntIndustry2() {
        return enhanceEntIndustry2;
    }

    public void setEnhanceEntIndustry2(String enhanceEntIndustry2) {
        this.enhanceEntIndustry2 = enhanceEntIndustry2;
    }
}
