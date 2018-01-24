package com.ccx.credit.risk.model.rate;

public class RateResult {
    private Integer id;

    private String ratingApplyNum;

    private Integer entId;

    private String elementName;

    private String indexName;

    private String indexData;

    private Integer value;

    private String degree;

    private String finalFlag;

    private String regularIndexFlag;

    private Double doubleValue;

    private Double weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRatingApplyNum() {
        return ratingApplyNum;
    }

    public void setRatingApplyNum(String ratingApplyNum) {
        this.ratingApplyNum = ratingApplyNum == null ? null : ratingApplyNum.trim();
    }

    public Integer getEntId() {
        return entId;
    }

    public void setEntId(Integer entId) {
        this.entId = entId;
    }


    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName == null ? null : elementName.trim();
    }


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public String getIndexData() {
        return indexData;
    }

    public void setIndexData(String indexData) {
        this.indexData = indexData == null ? null : indexData.trim();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree == null ? null : degree.trim();
    }

    public String getFinalFlag() {
        return finalFlag;
    }

    public void setFinalFlag(String finalFlag) {
        this.finalFlag = finalFlag == null ? null : finalFlag.trim();
    }

    public String getRegularIndexFlag() {
        return regularIndexFlag;
    }

    public void setRegularIndexFlag(String regularIndexFlag) {
        this.regularIndexFlag = regularIndexFlag == null ? null : regularIndexFlag.trim();
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}