package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 动态报表模板
 * Created by xzd on 2017/9/20.
 */
public class EnterpriseReportModel implements Serializable{
    private Integer id;

    /*报表id*/
    private Integer reportId;

    /*报表类型：1-金融财务报表，2-事业单位财务报表，3-医疗卫生行业财务报表*/
    private Integer reportType;

    /*
        报表子表类型：
        1-金融财务报表：1-资产类数据，2-负债及所有者权益类数据，3-损益类数据，4-现金流类数据，5-现金流补充信息，6-财务报表附注补充信息
        2-事业单位财务报表：7-资产类数据，8-负债类数据，9-收支类数据
        3-医疗卫生行业财务报表：10-资产负债类数据，11-收入支出总表，12-医疗收入费用明细，13-基本数字表
    */
    private Integer reportSonType;

    /*财务科目*/
    private String financialSubject;

    /*excel 名称*/
    private String columnExcel;

    /*jsp input id*/
    private String columnId;

    /*是否必填：0-非必填，1-必填*/
    private Integer required;

    /*年初余额（元）/上期金额（元）/上年累计数（元）/上年*/
    private BigDecimal beginBalance;

    /*年末余额（元）/本期金额（元）/本年累计数（元）/本年*/
    private BigDecimal endBalance;

    /*子表个数*/
    private Integer reportSonNo;

    /*报表子表排序*/
    private Integer orderNo;

    private String creatorName;
    private Date createDate;
    private Date updateDate;

    /*报表子表状态：0-未完成，1-已完成*//*
    private Integer state;*/

    /*机构id*/
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

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getReportSonType() {
        return reportSonType;
    }

    public void setReportSonType(Integer reportSonType) {
        this.reportSonType = reportSonType;
    }

    public String getFinancialSubject() {
        return financialSubject;
    }

    public void setFinancialSubject(String financialSubject) {
        this.financialSubject = financialSubject;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnExcel() {
        return columnExcel;
    }

    public void setColumnExcel(String columnExcel) {
        this.columnExcel = columnExcel;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
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

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getReportSonNo() {
        return reportSonNo;
    }

    public void setReportSonNo(Integer reportSonNo) {
        this.reportSonNo = reportSonNo;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    /*public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }*/

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
