package com.ccx.credit.risk.model.enterprise;

import java.util.Date;

/**
 * 评级报表联表校验表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportCheck {
    private Integer id;

    /*评级报表类型id*/
    private Integer reportType;

    /*公式*/
    private String formula;

    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}