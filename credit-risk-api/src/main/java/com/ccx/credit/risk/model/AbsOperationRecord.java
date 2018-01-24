package com.ccx.credit.risk.model;

import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

/**
 * 系统操作日志实体类
 * @author ymj
 */
@TableName("abs_operation_record")
public class AbsOperationRecord {

    /**操作记录id*/
    private long id;

    /**操作人*/
    private String operator;

    /**操作时间*/
    private String operationTime;

    /**操作基础模块*/
    private String basicModule;

    /**操作具体模块*/
    private String concreteModule;

    /**操方法*/
    private String operationMethod;

    /**对象名称*/
    private String OperationName;

    /**机构id*/
    private Integer companyId;

    /**请求地址*/
    private String urlPath;

    /**请求参数*/
    private String urlParam;


    public String getOperationName() {
        return OperationName;
    }

    public void setOperationName(String operationName) {
        OperationName = operationName;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperatTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getBasicModule() {
        return basicModule;
    }

    public void setBasicModule(String basicModule) {
        this.basicModule = basicModule;
    }

    public String getConcreteModule() {
        return concreteModule;
    }

    public void setConcreteModule(String concreteModule) {
        this.concreteModule = concreteModule;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public String getOperationMethod() {
        return operationMethod;

    }

    public void setOperationMethod(String operationMethod) {
        this.operationMethod = operationMethod;
    }

}
