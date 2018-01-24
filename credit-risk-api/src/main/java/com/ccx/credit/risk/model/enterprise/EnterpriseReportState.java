package com.ccx.credit.risk.model.enterprise;

/**
 * 报表子表状态表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportState {
    private Integer id;

    private Integer reportId;

    /*报表类型*/
    private Integer reportType;

    /*报表子表类型*/
    private Integer reportSonType;

    /*报表子表状态*/
    private Integer state;

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}