package com.ccx.credit.risk.model.enterprise;

import java.util.Date;

/**
 * 评级报表类型表
 * create by xzd 2017/9/25
 */
public class EnterpriseReportType {
    /*相当于其他表中的'report_type'*/
    private Integer id;

    /*模板类型名称*/
    private String name;

    /*状态 0：可用 1：历史版本 2:后台删除*/
    private Integer status;

    private String creatorName;

    private Date createDate;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}