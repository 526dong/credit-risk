package com.ccx.credit.risk.model.asset;

import java.math.BigDecimal;

/**
 * @author Created by xzd on 2017/12/6.
 * @Description 其他增信措施
 */
public class AssetEnhanceCredit {
    private String enhanceCreditId;

    private String enhanceCreditName;

    private String enhanceCreditCode;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 价值
     */
    private String enhanceCreditValue;

    //creditEnhanceMethod-增信措施三级联动
    /**
     * 一级
     */
    private String enhanceCreditMeasureGpId;
    private String enhanceCreditMeasureGpName;
    /**
     * 二级
     */
    private String enhanceCreditMeasurePId;
    private String enhanceCreditMeasurePName;
    /**
     * 三级
     */
    private String enhanceCreditMeasureId;
    private String enhanceCreditMeasureName;

    public String getEnhanceCreditId() {
        return enhanceCreditId;
    }

    public void setEnhanceCreditId(String enhanceCreditId) {
        this.enhanceCreditId = enhanceCreditId;
    }

    public String getEnhanceCreditName() {
        return enhanceCreditName;
    }

    public void setEnhanceCreditName(String enhanceCreditName) {
        this.enhanceCreditName = enhanceCreditName;
    }

    public String getEnhanceCreditCode() {
        return enhanceCreditCode;
    }

    public void setEnhanceCreditCode(String enhanceCreditCode) {
        this.enhanceCreditCode = enhanceCreditCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getEnhanceCreditValue() {
        return enhanceCreditValue;
    }

    public void setEnhanceCreditValue(String enhanceCreditValue) {
        this.enhanceCreditValue = enhanceCreditValue;
    }

    public String getEnhanceCreditMeasureGpName() {
        return enhanceCreditMeasureGpName;
    }

    public void setEnhanceCreditMeasureGpName(String enhanceCreditMeasureGpName) {
        this.enhanceCreditMeasureGpName = enhanceCreditMeasureGpName;
    }

    public String getEnhanceCreditMeasurePName() {
        return enhanceCreditMeasurePName;
    }

    public void setEnhanceCreditMeasurePName(String enhanceCreditMeasurePName) {
        this.enhanceCreditMeasurePName = enhanceCreditMeasurePName;
    }

    public String getEnhanceCreditMeasureName() {
        return enhanceCreditMeasureName;
    }

    public void setEnhanceCreditMeasureName(String enhanceCreditMeasureName) {
        this.enhanceCreditMeasureName = enhanceCreditMeasureName;
    }

    public String getEnhanceCreditMeasureGpId() {
        return enhanceCreditMeasureGpId;
    }

    public void setEnhanceCreditMeasureGpId(String enhanceCreditMeasureGpId) {
        this.enhanceCreditMeasureGpId = enhanceCreditMeasureGpId;
    }

    public String getEnhanceCreditMeasurePId() {
        return enhanceCreditMeasurePId;
    }

    public void setEnhanceCreditMeasurePId(String enhanceCreditMeasurePId) {
        this.enhanceCreditMeasurePId = enhanceCreditMeasurePId;
    }

    public String getEnhanceCreditMeasureId() {
        return enhanceCreditMeasureId;
    }

    public void setEnhanceCreditMeasureId(String enhanceCreditMeasureId) {
        this.enhanceCreditMeasureId = enhanceCreditMeasureId;
    }
}
