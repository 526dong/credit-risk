package com.ccx.credit.risk.model.assetsPackage;

/**
 * @param
 * @author Created by xzd on 2017/11/2.
 * @Description 资产包和资产关联表
 */
public class AssetPackageMsg {
    private Integer id;

    //资产id
    private Integer assetId;

    //资产包id
    private Integer assetPackageId;

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

    public Integer getAssetPackageId() {
        return assetPackageId;
    }

    public void setAssetPackageId(Integer assetPackageId) {
        this.assetPackageId = assetPackageId;
    }
}
