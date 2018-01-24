package com.ccx.credit.risk.model.asset;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 现金流
 * @author Created by xzd on 2017/12/11.
 */
public class AbsAssetsRepayment {
    private Integer id;

    private Integer assetsId;

    private String repaymentDate;

    private BigDecimal repaymentAmount;

    private BigDecimal repaymentInterest;

    private BigDecimal repaymentCost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Integer assetsId) {
        this.assetsId = assetsId;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getRepaymentInterest() {
        return repaymentInterest;
    }

    public void setRepaymentInterest(BigDecimal repaymentInterest) {
        this.repaymentInterest = repaymentInterest;
    }

    public BigDecimal getRepaymentCost() {
        return repaymentCost;
    }

    public void setRepaymentCost(BigDecimal repaymentCost) {
        this.repaymentCost = repaymentCost;
    }
}