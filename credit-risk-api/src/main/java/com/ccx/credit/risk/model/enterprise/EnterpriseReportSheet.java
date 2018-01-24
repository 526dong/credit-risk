package com.ccx.credit.risk.model.enterprise;

import java.util.List;

/**
 * 评级报表sheet表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportSheet {
    /*相当于其他表中的'report_son_type'*/
    private Integer id;

    /*sheet名称*/
    private String name;

    /*sheet名称-input id*/
    private String columnId;

    /*报表模型id*/
    private Integer reportType;

    /*子表个数*/
    private Integer reportSonNo;

    /*报表第一行命名*/
    private String columnsFirstName;

    /*子表数据集合*/
    private List<EnterpriseReportDataStore> reportDataList;

    /*列名称*/
    private String column1Name;
    private String column2Name;
    private String column3Name;
    private String column4Name;
    private String column5Name;
    private String column6Name;

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

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getReportSonNo() {
        return reportSonNo;
    }

    public void setReportSonNo(Integer reportSonNo) {
        this.reportSonNo = reportSonNo;
    }

    public String getColumnsFirstName() {
        return columnsFirstName;
    }

    public void setColumnsFirstName(String columnsFirstName) {
        this.columnsFirstName = columnsFirstName == null ? null : columnsFirstName.trim();
    }

    public String getColumn1Name() {
        return column1Name;
    }

    public void setColumn1Name(String column1Name) {
        this.column1Name = column1Name;
    }

    public String getColumn2Name() {
        return column2Name;
    }

    public void setColumn2Name(String column2Name) {
        this.column2Name = column2Name;
    }

    public String getColumn3Name() {
        return column3Name;
    }

    public void setColumn3Name(String column3Name) {
        this.column3Name = column3Name;
    }

    public String getColumn4Name() {
        return column4Name;
    }

    public void setColumn4Name(String column4Name) {
        this.column4Name = column4Name;
    }

    public String getColumn5Name() {
        return column5Name;
    }

    public void setColumn5Name(String column5Name) {
        this.column5Name = column5Name;
    }

    public String getColumn6Name() {
        return column6Name;
    }

    public void setColumn6Name(String column6Name) {
        this.column6Name = column6Name;
    }

    public List<EnterpriseReportDataStore> getReportDataList() {
        return reportDataList;
    }

    public void setReportDataList(List<EnterpriseReportDataStore> reportDataList) {
        this.reportDataList = reportDataList;
    }
}