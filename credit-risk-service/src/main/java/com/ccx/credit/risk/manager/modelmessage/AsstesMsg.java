package com.ccx.credit.risk.manager.modelmessage;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/10/25
 */
//资产信息
public  class AsstesMsg{
    //资产id
    private Integer assetsid;
    //主体id
    private Integer enterpriseid;
    //主体区域（省）
    private String provinceid;
    //主体名称
    private String name;
    //后台行业Id
    private Integer bgid;

    public Integer getAssetsid() {
        return assetsid;
    }

    public void setAssetsid(Integer assetsid) {
        this.assetsid = assetsid;
    }

    public Integer getEnterpriseid() {
        return enterpriseid;
    }

    public void setEnterpriseid(Integer enterpriseid) {
        this.enterpriseid = enterpriseid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBgid() {
        return bgid;
    }

    public void setBgid(Integer bgid) {
        this.bgid = bgid;
    }
}
