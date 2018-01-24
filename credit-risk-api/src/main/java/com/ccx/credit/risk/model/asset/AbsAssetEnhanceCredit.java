package com.ccx.credit.risk.model.asset;

/**
 * @Description 增信措施
 * @author Created by xzd on 2017/12/19.
 */
public class AbsAssetEnhanceCredit {
    private Integer id;

    private String name;

    private Integer pid;

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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}