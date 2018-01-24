package com.ccx.credit.risk.manager.modelmessage;

import java.util.List;
import java.util.Map;

/**
 * @Description: 分词模型基础数据包实体类
 * @author:lilong
 * @Date: 2017/10/23
 */
public class CommonModlePakage {
    /**
     * 现金流
     */
    public static class CashFlow {
        /**
         * 贷款时长
         */
        private String loanLength;

        /**
         * 资产名称
         */
        private  String assetsName;

        /**
         * 还款计划
         */
        private List<Repayment> repayments;

        public String getLoanLength() {
            return loanLength;
        }

        public void setLoanLength(String loanLength) {
            this.loanLength = loanLength;
        }

        public String getAssetsName() {
            return assetsName;
        }

        public void setAssetsName(String assetsName) {
            this.assetsName = assetsName;
        }

        public List<Repayment> getRepayments() {
            return repayments;
        }

        public void setRepayments(List<Repayment> repayments) {
            this.repayments = repayments;
        }
    }

    /**
     * 还款计划
     */
    public static class  Repayment{
        //还款日期
        private String date;
        //还款金额
        private Double amount;

        public Repayment() {
        }

        public Repayment(String date, Double amount) {
            this.date = date;
            this.amount = amount;
        }

        public String getDate() {
            return date.indexOf("年0")>0?date.replace("年0","年"):date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }

    //违约率
    public static class Defaulrates{
        //级别
        private  String level;

        //违约率
        private List<Map<String,Double>> rates;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public List<Map<String, Double>> getRates() {
            return rates;
        }

        public void setRates(List<Map<String, Double>> rates) {
            this.rates = rates;
        }
    }

    //回收率
    public static class  Recoveryrates{
        //级别
        private String level;
        //回收率
        private Double rate;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }
    }

}
