package com.ccx.credit.risk.model.assetlayer;

import java.math.BigDecimal;
import java.util.Date;

public class LayerSetup {
    private Integer id;

    private Integer layerId;

    private Integer assetPakegeId;

    private Long publishCapital;

    private Date closePackageTime;

    private Date foundTime;

    private Date predictExpireTime;

    private Byte repaymentType;

    private Date firstRepaymentTime;

    private Integer repaymentIntervalTime;

    private Integer repaymentTime;

    private BigDecimal trusteeshipRate;

    private BigDecimal manageRate;

    private BigDecimal gradeRate;

    private BigDecimal taxRate;

    private Integer simulationNum;

    private BigDecimal expediteSettlementDefaultRate;

    private Integer assetNum;

    private String assetIds;

    private BigDecimal leftCapital;

    private BigDecimal leftPrincipal;

    private BigDecimal avgYear;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLayerId() {
        return layerId;
    }

    public void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    public Integer getAssetPakegeId() {
        return assetPakegeId;
    }

    public void setAssetPakegeId(Integer assetPakegeId) {
        this.assetPakegeId = assetPakegeId;
    }

    public Long getPublishCapital() {
        return publishCapital;
    }

    public void setPublishCapital(Long publishCapital) {
        this.publishCapital = publishCapital;
    }

    public Date getClosePackageTime() {
        return closePackageTime;
    }

    public void setClosePackageTime(Date closePackageTime) {
        this.closePackageTime = closePackageTime;
    }

    public Date getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(Date foundTime) {
        this.foundTime = foundTime;
    }

    public Date getPredictExpireTime() {
        return predictExpireTime;
    }

    public void setPredictExpireTime(Date predictExpireTime) {
        this.predictExpireTime = predictExpireTime;
    }

    public Byte getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(Byte repaymentType) {
        this.repaymentType = repaymentType;
    }

    public Date getFirstRepaymentTime() {
        return firstRepaymentTime;
    }

    public void setFirstRepaymentTime(Date firstRepaymentTime) {
        this.firstRepaymentTime = firstRepaymentTime;
    }

    public Integer getRepaymentIntervalTime() {
        return repaymentIntervalTime;
    }

    public void setRepaymentIntervalTime(Integer repaymentIntervalTime) {
        this.repaymentIntervalTime = repaymentIntervalTime;
    }

    public Integer getRepaymentTime() {
        return repaymentTime;
    }

    public void setRepaymentTime(Integer repaymentTime) {
        this.repaymentTime = repaymentTime;
    }

    public BigDecimal getTrusteeshipRate() {
        return trusteeshipRate;
    }

    public void setTrusteeshipRate(BigDecimal trusteeshipRate) {
        this.trusteeshipRate = trusteeshipRate;
    }

    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    public BigDecimal getGradeRate() {
        return gradeRate;
    }

    public void setGradeRate(BigDecimal gradeRate) {
        this.gradeRate = gradeRate;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getSimulationNum() {
        return simulationNum;
    }

    public void setSimulationNum(Integer simulationNum) {
        this.simulationNum = simulationNum;
    }

    public BigDecimal getExpediteSettlementDefaultRate() {
        return expediteSettlementDefaultRate;
    }

    public void setExpediteSettlementDefaultRate(BigDecimal expediteSettlementDefaultRate) {
        this.expediteSettlementDefaultRate = expediteSettlementDefaultRate;
    }

    public Integer getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(Integer assetNum) {
        this.assetNum = assetNum;
    }

    public String getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(String assetIds) {
        this.assetIds = assetIds == null ? null : assetIds.trim();
    }

    public BigDecimal getLeftCapital() {
        return leftCapital;
    }

    public void setLeftCapital(BigDecimal leftCapital) {
        this.leftCapital = leftCapital;
    }

    public BigDecimal getLeftPrincipal() {
        return leftPrincipal;
    }

    public void setLeftPrincipal(BigDecimal leftPrincipal) {
        this.leftPrincipal = leftPrincipal;
    }

    public BigDecimal getAvgYear() {
        return avgYear;
    }

    public void setAvgYear(BigDecimal avgYear) {
        this.avgYear = avgYear;
    }
}