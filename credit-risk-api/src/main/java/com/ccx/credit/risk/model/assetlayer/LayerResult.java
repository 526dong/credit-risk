package com.ccx.credit.risk.model.assetlayer;

import java.math.BigDecimal;
import java.util.Date;

public class LayerResult {
    private Integer id;

    private Integer layerId;

    private String level;

    private String payDateStr;

    private Date payDate;

    private BigDecimal proportion;

    private String proportionStr;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    public String getPayDateStr() {
        return payDateStr;
    }

    public void setPayDateStr(String payDateStr) {
        this.payDateStr = payDateStr;
    }

    public String getProportionStr() {
        return proportionStr;
    }

    public void setProportionStr(String proportionStr) {
        this.proportionStr = proportionStr;
    }
}
