package com.ccx.credit.risk.model.assetlayer;

import com.ccx.credit.risk.util.DateUtils;

import java.util.Date;

public class LayerAssetsRelevant {
    private Integer id;

    private Integer layerId;

    private Integer assetPakegeId;

    private Integer assetId;

    private Integer relevantValue;

    private String creatorName;

    private Date createTime;

    private String createTimeStr;

    private String name;

    /***关联资产***/
    /**
     * 资产code
     */
    private String code;

    /**
     * 资产name
     */
    private String enterpriseName;

    public LayerAssetsRelevant() {
    }

    public LayerAssetsRelevant(Integer layerId, Integer assetPakegeId, Integer assetId, Integer relevantValue, String creatorName, Date createTime) {
        this.layerId = layerId;
        this.assetPakegeId = assetPakegeId;
        this.assetId = assetId;
        this.relevantValue = relevantValue;
        this.creatorName = creatorName;
        this.createTime = createTime;
    }

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

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public Integer getRelevantValue() {
        return relevantValue;
    }

    public void setRelevantValue(Integer relevantValue) {
        this.relevantValue = relevantValue;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTimeStr() {
        try {
            return createTime == null ? "" : DateUtils.formatDateStr(createTime);
        } catch (Exception e) {
            return "";
        }
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }
}