package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 评级报表数据存储表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportDataStore implements Serializable {
    private Integer id;

    /*报表id*/
    private Integer reportId;

    /*评级模型字段id*/
    private Integer reportModelId;

    /*前3列-年初余额（元）/上期金额（元）/上年累计数（元）/上年*/
    private BigDecimal beginBalance;

    /*前3列-年末余额（元）/本期金额（元）/本年累计数（元）/本年*/
    private BigDecimal endBalance;

    //名称
    private String name;

    /*年初excel命名*/
    private String beginExcelName;

    /*年末excel命名*/
    private String endExcelName;

    /*报表列数*/
    private Integer reportSonNo;

    /*排序字段*/
    private Integer orderNo;

    private String creatorName;

    private Date createDate;

    private Date updateDate;

    /*是否删除标识：0-可用，未删除；1-不可用，已删除*/
    private Integer deleteFlag;

    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getReportModelId() {
        return reportModelId;
    }

    public void setReportModelId(Integer reportModelId) {
        this.reportModelId = reportModelId;
    }

    public BigDecimal getBeginBalance() {
        return beginBalance;
    }

    public void setBeginBalance(BigDecimal beginBalance) {
        this.beginBalance = beginBalance;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginExcelName() {
        return beginExcelName;
    }

    public void setBeginExcelName(String beginExcelName) {
        this.beginExcelName = beginExcelName == null ? null : beginExcelName.trim();
    }

    public String getEndExcelName() {
        return endExcelName;
    }

    public void setEndExcelName(String endExcelName) {
        this.endExcelName = endExcelName == null ? null : endExcelName.trim();
    }

    public Integer getReportSonNo() {
        return reportSonNo;
    }

    public void setReportSonNo(Integer reportSonNo) {
        this.reportSonNo = reportSonNo;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    public Date getCreateDate() {
        return createDate;
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

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}