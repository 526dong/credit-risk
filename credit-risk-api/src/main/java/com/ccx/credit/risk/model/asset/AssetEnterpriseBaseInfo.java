package com.ccx.credit.risk.model.asset;

import java.io.Serializable;

/**
 * @author Created by xzd on 2017/12/6.
 * @Description 承租企业
 */
public class AssetEnterpriseBaseInfo implements Serializable{
    /**
     * 承担企业/卖方/借款企业
     */
    private String entId;
    private String entName;

    /**
     * 承担/担保比例
     */
    private String entDebtProportion;

    /**
     * 承担企业级别
     */
    private String entRateResult;

    /**
     * 影子评级结果
     */
    private String entShadowRateResult;
    /**
     * 省份
     */
    private String entProvince;
    /**
     * 一级行业、二级行业
     */
    private String entIndustry1;
    private String entIndustry2;

    /**
     * 买方
     */
    private String buyerEntId;
    private String buyerEntName;
    private String buyerEntRateResult;
    private String buyerEntShadowRateResult;

    /**
     * 省份
     */
    private String buyerEntProvince;
    /**
     * 一级行业、二级行业
     */
    private String buyerEntIndustry1;
    private String buyerEntIndustry2;

    public String getEntId() {
        return entId;
    }

    public void setEntId(String entId) {
        this.entId = entId;
    }

    public String getEntName() {
        return entName;
    }

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getEntDebtProportion() {
        return entDebtProportion;
    }

    public void setEntDebtProportion(String entDebtProportion) {
        this.entDebtProportion = entDebtProportion;
    }

    public String getEntRateResult() {
        return entRateResult;
    }

    public void setEntRateResult(String entRateResult) {
        this.entRateResult = entRateResult;
    }

    public String getEntShadowRateResult() {
        return entShadowRateResult;
    }

    public void setEntShadowRateResult(String entShadowRateResult) {
        this.entShadowRateResult = entShadowRateResult;
    }

    public String getEntProvince() {
        return entProvince;
    }

    public void setEntProvince(String entProvince) {
        this.entProvince = entProvince;
    }

    public String getEntIndustry1() {
        return entIndustry1;
    }

    public void setEntIndustry1(String entIndustry1) {
        this.entIndustry1 = entIndustry1;
    }

    public String getEntIndustry2() {
        return entIndustry2;
    }

    public void setEntIndustry2(String entIndustry2) {
        this.entIndustry2 = entIndustry2;
    }

    public String getBuyerEntId() {
        return buyerEntId;
    }

    public void setBuyerEntId(String buyerEntId) {
        this.buyerEntId = buyerEntId;
    }

    public String getBuyerEntName() {
        return buyerEntName;
    }

    public void setBuyerEntName(String buyerEntName) {
        this.buyerEntName = buyerEntName;
    }

    public String getBuyerEntRateResult() {
        return buyerEntRateResult;
    }

    public void setBuyerEntRateResult(String buyerEntRateResult) {
        this.buyerEntRateResult = buyerEntRateResult;
    }

    public String getBuyerEntShadowRateResult() {
        return buyerEntShadowRateResult;
    }

    public void setBuyerEntShadowRateResult(String buyerEntShadowRateResult) {
        this.buyerEntShadowRateResult = buyerEntShadowRateResult;
    }

    public String getBuyerEntProvince() {
        return buyerEntProvince;
    }

    public void setBuyerEntProvince(String buyerEntProvince) {
        this.buyerEntProvince = buyerEntProvince;
    }

    public String getBuyerEntIndustry1() {
        return buyerEntIndustry1;
    }

    public void setBuyerEntIndustry1(String buyerEntIndustry1) {
        this.buyerEntIndustry1 = buyerEntIndustry1;
    }

    public String getBuyerEntIndustry2() {
        return buyerEntIndustry2;
    }

    public void setBuyerEntIndustry2(String buyerEntIndustry2) {
        this.buyerEntIndustry2 = buyerEntIndustry2;
    }
}
