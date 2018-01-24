package com.ccx.credit.risk.model.enterprise;

/**
 * 评级报表字段公式表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportCloumnsFormula {
    private Integer id;

    /*公式*/
    private String formula;

    /*评级报类型id*/
    private Integer reportType;

    /*子表类型id-sheet表*/
    private Integer reportSonType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula == null ? null : formula.trim();
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
}