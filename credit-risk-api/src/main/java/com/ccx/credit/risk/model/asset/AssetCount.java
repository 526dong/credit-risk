package com.ccx.credit.risk.model.asset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 统计分析接收类
 * @author Created by wuChao 
 * 2017年12月8日15:39:44
 */
public class AssetCount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*统计名称*/
    private String bothName;
    
    /*统计数量-笔数*/
    private String count;
    
    /*投放金额*/
    private String putMoney;
    
    /*剩余本金*/
    private String almoney;
    
    /*资产类型*/
    private String assetType;
    
    /*统计维度*/
    private String type;
    
    /*最大还款时间*/
    private String maxDay;
    
    /*还款总额*/
    private String money;
    
    /*债权本金*/
    private BigDecimal bondPrincipal;
    
    /*投放日期*/
    private Date putDate;

	public String getBothName() {
		return bothName;
	}

	public void setBothName(String bothName) {
		this.bothName = bothName;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPutMoney() {
		return putMoney;
	}

	public void setPutMoney(String putMoney) {
		this.putMoney = putMoney;
	}

	public String getAlmoney() {
		return almoney;
	}

	public void setAlmoney(String almoney) {
		this.almoney = almoney;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(String maxDay) {
		this.maxDay = maxDay;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public BigDecimal getBondPrincipal() {
		return bondPrincipal;
	}

	public void setBondPrincipal(BigDecimal bondPrincipal) {
		this.bondPrincipal = bondPrincipal;
	}

	public Date getPutDate() {
		return putDate;
	}

	public void setPutDate(Date putDate) {
		this.putDate = putDate;
	}

	@Override
	public String toString() {
		return "AssetCount [bothName=" + bothName + ", count=" + count + ", putMoney=" + putMoney + ", almoney="
				+ almoney + ", assetType=" + assetType + ", type=" + type + ", maxDay=" + maxDay + ", money=" + money
				+ ", bondPrincipal=" + bondPrincipal + ", putDate=" + putDate + "]";
	}
    
   
	
	
}