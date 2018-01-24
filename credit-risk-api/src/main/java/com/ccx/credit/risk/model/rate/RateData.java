package com.ccx.credit.risk.model.rate;

import java.io.Serializable;
import java.util.Date;

/**
 * 评级数据管理
 * @author xzd
 * @date 2017/7/13
 */
public class RateData implements Serializable{
	private Integer id;

	/*企业名称*/
    private String name;

    /*信用代码标识：0-统一信用代码，1-组织机构代码，2-事证号，3-其他*/
    private Integer creditCodeType;
    
    /*统一信用代码*/
    private String creditCode;

    /*组织机构代码*/
    private String organizationCode;

    /*事证号*/
    private String certificateCode;

    /*字典-评级机构id*/
    private Integer rateInstitution;
    
    /*字典-评级机构名称*/
    private String rateInstitutionName;
    
    /*字典-评级结果id*/
    private Integer rateResult;
    
    /*字典-评级结果名称*/
    private String rateResultName;
    
    /*评级时间*/
    private Date rateTime;

    /*创建人*/
    private String creatorName;

    /*创建时间*/
    private Date createTime;

    /*机构id*/
    private Integer institutionId;

    //优先级：中诚信-1，其他-2
    private Integer priority;

    //是否被影子评级使用：0-否，1-是，默认否
    private Integer useShadow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCreditCodeType() {
		return creditCodeType;
	}

	public void setCreditCodeType(Integer creditCodeType) {
		this.creditCodeType = creditCodeType;
	}

	public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode == null ? null : creditCode.trim();
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public void setOrganizationCode(String organizationCode) {

        this.organizationCode = organizationCode;
    }

    public Integer getRateInstitution() {
        return rateInstitution;
    }

    public void setRateInstitution(Integer rateInstitution) {
        this.rateInstitution = rateInstitution;
    }
    
	public String getRateInstitutionName() {
		return rateInstitutionName;
	}

	public void setRateInstitutionName(String rateInstitutionName) {
		this.rateInstitutionName = rateInstitutionName;
	}

	public Integer getRateResult() {
        return rateResult;
    }

    public void setRateResult(Integer rateResult) {
        this.rateResult = rateResult;
    }
    
	public String getRateResultName() {
		return rateResultName;
	}

	public void setRateResultName(String rateResultName) {
		this.rateResultName = rateResultName;
	}

    public Date getRateTime() {
        return rateTime;
    }

    public void setRateTime(Date rateTime) {
        this.rateTime = rateTime;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getUseShadow() {
        return useShadow;
    }

    public void setUseShadow(Integer useShadow) {
        this.useShadow = useShadow;
    }
}