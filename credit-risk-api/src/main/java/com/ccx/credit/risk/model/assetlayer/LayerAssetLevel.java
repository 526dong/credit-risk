package com.ccx.credit.risk.model.assetlayer;

import java.math.BigDecimal;

public class LayerAssetLevel {
    private Integer id;

    private String levelId;

    private Integer layerId;

    private String layerName;

    private Byte securityType;

    private BigDecimal capitalRate;

    private BigDecimal expectEarningsRate;

    private Byte isFloat;

    private String layerResultLevel;

    private Integer issuePeriod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId == null ? null : levelId.trim();
    }

    public Integer getLayerId() {
        return layerId;
    }

    public void setLayerId(Integer layerId) {
        this.layerId = layerId;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName == null ? null : layerName.trim();
    }

    public Byte getSecurityType() {
        return securityType;
    }

    public void setSecurityType(Byte securityType) {
        this.securityType = securityType;
    }

    public BigDecimal getCapitalRate() {
        return capitalRate;
    }

    public void setCapitalRate(BigDecimal capitalRate) {
        this.capitalRate = capitalRate;
    }

    public BigDecimal getExpectEarningsRate() {
        return expectEarningsRate;
    }

    public void setExpectEarningsRate(BigDecimal expectEarningsRate) {
        this.expectEarningsRate = expectEarningsRate;
    }

    public Byte getIsFloat() {
        return isFloat;
    }

    public void setIsFloat(Byte isFloat) {
        this.isFloat = isFloat;
    }

    public String getLayerResultLevel() {
        return layerResultLevel;
    }

    public void setLayerResultLevel(String layerResultLevel) {
        this.layerResultLevel = layerResultLevel == null ? null : layerResultLevel.trim();
    }

    public Integer getIssuePeriod() {
        return issuePeriod;
    }

    public void setIssuePeriod(Integer issuePeriod) {
        this.issuePeriod = issuePeriod;
    }
}
