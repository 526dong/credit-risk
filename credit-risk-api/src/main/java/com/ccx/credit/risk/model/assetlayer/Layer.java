package com.ccx.credit.risk.model.assetlayer;

import com.ccx.credit.risk.util.DateUtils;

import java.util.Date;

public class Layer {
    private Integer id;

    private Integer assetPakegeId;

    private String layerApplyNum;

    private String lastLayerUserName;

    private Date lastLayerTime;

    private Byte status;

    private Byte finishFlag;

    private String lastLayerTimeStr;

    /**  资产相关  **/
    private String assetPackageNo;

    private String assetPackageName;

    private Integer assetNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetPakegeId() {
        return assetPakegeId;
    }

    public void setAssetPakegeId(Integer assetPakegeId) {
        this.assetPakegeId = assetPakegeId;
    }

    public String getLayerApplyNum() {
        return layerApplyNum;
    }

    public void setLayerApplyNum(String layerApplyNum) {
        this.layerApplyNum = layerApplyNum == null ? null : layerApplyNum.trim();
    }

    public String getLastLayerUserName() {
        return lastLayerUserName;
    }

    public void setLastLayerUserName(String lastLayerUserName) {
        this.lastLayerUserName = lastLayerUserName == null ? null : lastLayerUserName.trim();
    }

    public Date getLastLayerTime() {
        return lastLayerTime;
    }

    public void setLastLayerTime(Date lastLayerTime) {
        this.lastLayerTime = lastLayerTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getAssetPackageNo() {
        return assetPackageNo;
    }

    public void setAssetPackageNo(String assetPackageNo) {
        this.assetPackageNo = assetPackageNo;
    }

    public String getAssetPackageName() {
        return assetPackageName;
    }

    public void setAssetPackageName(String assetPackageName) {
        this.assetPackageName = assetPackageName;
    }

    public Integer getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(Integer assetNum) {
        this.assetNum = assetNum;
    }

    public String getLastLayerTimeStr() {
        try {
            return lastLayerTime == null ? "" : DateUtils.formatDateStr(lastLayerTime);
        } catch (Exception e) {
            return "";
        }
    }

    public Byte getFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(Byte finishFlag) {
        this.finishFlag = finishFlag;
    }
}