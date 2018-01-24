package com.ccx.credit.risk.model.assetsPackage;

import java.util.Date;

import com.ccx.credit.risk.util.DateUtils;

public class AssetsPakege {
    private int id;

    private String assetPackageNo;

	private int assetType;

    private String assetPackageName;

    private String creatorName;

    private Date createTime;
    
    private int institutionId;
    
    private int isDel;
    
    private int assetsNum;
    
    

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getCreateTimeStr() throws Exception {
		if (createTime != null) {
			return DateUtils.formatDateStr(createTime);
		} else {
			return null;
		}
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getAssetsNum() {
		return assetsNum;
	}

	public void setAssetsNum(int assetsNum) {
		this.assetsNum = assetsNum;
	}

	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	public int getAssetType() {
		return assetType;
	}

	public void setAssetType(int assetType) {
		this.assetType = assetType;
	}
}