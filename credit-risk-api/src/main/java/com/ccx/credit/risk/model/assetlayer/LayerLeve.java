package com.ccx.credit.risk.model.assetlayer;

import java.math.BigDecimal;

public class LayerLeve {
    private int id;

    private String layerName;

    private int securityType;

    private BigDecimal capitalRate;

    private BigDecimal expectEarningsRate;

    private int isFloat;

    private int layerOrder;

    private BigDecimal floatValue;
    
    private int isDel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public int getSecurityType() {
		return securityType;
	}

	public void setSecurityType(int securityType) {
		this.securityType = securityType;
	}

	public BigDecimal getCapitalRate() {
		return capitalRate;
	}

	public void setCapitalRate(BigDecimal capitalRate) {
		this.capitalRate = capitalRate;
	}

	public BigDecimal getExpectEarningsRate() {
		return expectEarningsRate;
	}

	public void setExpectEarningsRate(BigDecimal expectEarningsRate) {
		this.expectEarningsRate = expectEarningsRate;
	}

	public int getIsFloat() {
		return isFloat;
	}

	public void setIsFloat(int isFloat) {
		this.isFloat = isFloat;
	}

	public int getLayerOrder() {
		return layerOrder;
	}

	public void setLayerOrder(int layerOrder) {
		this.layerOrder = layerOrder;
	}

	public BigDecimal getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(BigDecimal floatValue) {
		this.floatValue = floatValue;
	}

	public int getIsDel() {
		return isDel;
	}

	public void setIsDel(int isDel) {
		this.isDel = isDel;
	}

	
    
}