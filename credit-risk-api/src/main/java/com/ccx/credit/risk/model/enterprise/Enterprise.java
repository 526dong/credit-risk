package com.ccx.credit.risk.model.enterprise;

import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.util.DateUtils;
import com.ccx.credit.risk.util.StringUtils;

import java.util.Date;

/**
 * 企业信息
 * @author xzd
 * @date 2017/6/8
 */
public class Enterprise{
	private Integer id;

    /*企业名称*/
    private String name;

    /*流水号*/
    private String cid;

	/*信用代码标识：0-统一信用代码，1-组织机构代码，2-事证号，3-其他*/
    private Integer creditCodeType;
    
    /*信用代码value*/
    private String creditCode;

    /*企业规模：0-大中型企业,1-小微企业*/
    private String scale;
    
    /*企业性质id*/
    private Integer nature;
    
    /*企业性质名称：0-事业单位，1-国有企业，2-集体所有制企业，3-三资企业，4-私营企业，5-其他*/
    private String natureName;

	/*企业一级行业id*/
    private Integer industry1;
    
    /*企业一级行业名称*/
    private String industry1Name;
    
    /*企业二级行业id*/
    private Integer industry2;
    
    /*企业二级行业名称*/
    private String industry2Name;

    /*报表类型/名称-对应于industry2Id的值*/
    private EnterpriseReportType reportType;

	/*省id*/
    private String provinceId;
    
    /*省名称*/
    private String provinceName;
    
    /*城市id*/
    private String cityId;
    
    /*城市名称*/
    private String cityName;
    
    /*地区/县id*/
    private String countyId;
    
    /*地区/县名称*/
    private String countyName;
    
    /*企业区域：省市县，当前为详细地址*/
    private String address;

    /*企业类型：0-新评级，1-更新评级*/
    private Integer type;

    /*企业法人姓名*/
    private String corporateName;

    /*企业法人身份证号*/
    private String corporateCid;

    /*企业法人手机号码*/
    private String corporatePhone;

    /*创建人姓名*/
    private String creatorName;

    /*创建日期*/
    private Date createDate;

    private String createDateStr;

    /*更新日期*/
    private Date updateDate;

    /*录入状态:0-未完成，1-已完成*/
    private Integer state;
    
    /*审批状态：0-未提交，1-已提交，2-已评级，3-被退回*/
    private Integer approvalState;
    
    /*评级状态：0-新评级，1-跟踪评级*/
    private Integer ratingType;
    
    /*评级申请编号*/
    private String ratingApplyNum;

    /*评级信息*/
    private Approval approval;
    
    /*最新报表id*/
    private Integer reportId;
    
    /*最新报表信息*/
    private Report latestReport;

    /*公司id*/
    private Integer companyId;

    /*是否删除：0-否，1-是。默认0*/
    private Integer deleteFlag;

	private Integer refuseFlag;
    
    public Enterprise() {
		super();
	}

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
		this.name = name;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
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
		this.creditCode = creditCode;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Integer getNature() {
		return nature;
	}

	public void setNature(Integer nature) {
		this.nature = nature;
	}

	public String getNatureName() {
		return natureName;
	}

	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}

	public Integer getIndustry1() {
		return industry1;
	}

	public void setIndustry1(Integer industry1) {
		this.industry1 = industry1;
	}

	public String getIndustry1Name() {
		return industry1Name;
	}

	public void setIndustry1Name(String industry1Name) {
		this.industry1Name = industry1Name;
	}

	public Integer getIndustry2() {
		return industry2;
	}

	public void setIndustry2(Integer industry2) {
		this.industry2 = industry2;
	}

	public String getIndustry2Name() {
		return industry2Name;
	}

	public void setIndustry2Name(String industry2Name) {
		this.industry2Name = industry2Name;
	}

	public EnterpriseReportType getReportType() {
		return reportType;
	}

	public void setReportType(EnterpriseReportType reportType) {
		this.reportType = reportType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCorporateName() {
		return corporateName;
	}

	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}

	public String getCorporateCid() {
		return corporateCid;
	}

	public void setCorporateCid(String corporateCid) {
		this.corporateCid = corporateCid;
	}

	public String getCorporatePhone() {
		return corporatePhone;
	}

	public void setCorporatePhone(String corporatePhone) {
		this.corporatePhone = corporatePhone;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreateDate() throws Exception {
		if (!StringUtils.isEmpty(createDate)) {
    		return DateUtils.formatDate(createDate);
		}else{
			return null;
		}
	}
	
	public String getCreateDateStr() throws Exception {
		if (!StringUtils.isEmpty(createDate)) {
    		return DateUtils.formatDateStr(createDate);
		}else{
			return null;
		}
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getApprovalState() {
		return approvalState;
	}

	public void setApprovalState(Integer approvalState) {
		this.approvalState = approvalState;
	}

	public Integer getRatingType() {
		return ratingType;
	}

	public void setRatingType(Integer ratingType) {
		this.ratingType = ratingType;
	}

	public String getRatingApplyNum() {
		return ratingApplyNum;
	}

	public void setRatingApplyNum(String ratingApplyNum) {
		this.ratingApplyNum = ratingApplyNum;
	}

	public Approval getApproval() {
		return approval;
	}

	public void setApproval(Approval approval) {
		this.approval = approval;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}
	
	public Report getLatestReport() {
		return latestReport;
	}

	public void setLatestReport(Report latestReport) {
		this.latestReport = latestReport;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Integer getRefuseFlag() {
		return refuseFlag;
	}

	public void setRefuseFlag(Integer refuseFlag) {
		this.refuseFlag = refuseFlag;
	}
}