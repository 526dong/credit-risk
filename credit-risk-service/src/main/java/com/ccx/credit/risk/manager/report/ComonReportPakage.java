package com.ccx.credit.risk.manager.report;

import com.ccx.credit.risk.model.enterprise.Report;

import java.util.List;
/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/25
 */
public class ComonReportPakage {

    //报表数据信息
    public static class  BaseValues {
        //报表基本信息
        private Report report;

        //字段属性值
        private List<Values> values;


        public List<Values> getValues() {
            return values;
        }

        public void setValues(List<Values> values) {
            this.values = values;
        }

        public Report getReport() {
            return report;
        }

        public void setReport(Report report) {
            this.report = report;
        }
    }

    //报表数据信息
    public static class  Values {
        //子表数据id(对应abs_enterprise_report_state 中的id)
        private Integer id;
        //sheetId
        private Integer sheetId;

        //子表名称
        private String sheetName;

        //子表存储状态 0-未完成，1-已完成
        private Integer status;

        private Integer sheetOrder;

        //获取类型信息
       // private Modle modle;

        //字段属性值
        private List<ValueField> fields;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<ValueField> getFields() {
            return fields;
        }

        public void setFields(List<ValueField> fields) {
            this.fields = fields;
        }

//        public Modle getModle() {
//            return modle;
//        }
//
//        public void setModle(Modle modle) {
//            this.modle = modle;
//        }

        public Integer getSheetOrder() {
            return sheetOrder;
        }

        public void setSheetOrder(Integer sheetOrder) {
            this.sheetOrder = sheetOrder;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getSheetId() {
            return sheetId;
        }

        public void setSheetId(Integer sheetId) {
            this.sheetId = sheetId;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }
    }

    //类型信息
    public static  class Modle {
        //子表id
        private Integer sheetId;
        //子表名称
        private String sheetName;
        //第一行列名
        private List<String> fristClumn;

        //字段属性值,
        private List<ModleField> fields;

        //公式集合
        private List<Formula> formulas;


        public Integer getSheetId() {
            return sheetId;
        }

        public List<ModleField> getFields() {
            return fields;
        }

        public void setFields(List<ModleField> fields) {
            this.fields = fields;
        }

        public void setSheetId(Integer sheetId) {
            this.sheetId = sheetId;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public List<String> getFristClumn() {
            return fristClumn;
        }

        public void setFristClumn(List<String> fristClumn) {
            this.fristClumn = fristClumn;
        }

        public List<Formula> getFormulas() {
            return formulas;
        }

        public void setFormulas(List<Formula> formulas) {
            this.formulas = formulas;
        }
    }

    //类型字段属性
    public static  class ModleField {
        //字段Id
        private Integer modleId;
        //字段名称
        private String name;
        //字段excel名称
        private String excelName;
        //字段排序
        private Integer order;
        //字段所在列数
        private Integer colunmNo;
        //字段是否必填
        private Integer required;

        public Integer getModleId() {
            return modleId;
        }

        public void setModleId(Integer modleId) {
            this.modleId = modleId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExcelName() {
            return excelName;
        }

        public void setExcelName(String excelName) {
            this.excelName = excelName;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public Integer getColunmNo() {
            return colunmNo;
        }

        public void setColunmNo(Integer colunmNo) {
            this.colunmNo = colunmNo;
        }

        public Integer getRequired() {
            return required;
        }

        public void setRequired(Integer required) {
            this.required = required;
        }
    }
    //报表字段值信息
    public static  class ValueField{
        //字段Id
        private Integer modleId;
        //字段模型值
        private ModleField modleField;
        //年初额
        private String beginBalance;
        //年末额
        private String endBalance;
        //年初excel命名
        private String beginExcelName;
        //年末excel命名
        private String endExcelName;

        public String getBeginBalance() {
            return beginBalance;
        }

        public void setBeginBalance(String beginBalance) {
            this.beginBalance = beginBalance;
        }

        public String getEndBalance() {
            return endBalance;
        }

        public void setEndBalance(String endBalance) {
            this.endBalance = endBalance;
        }

        public String getBeginExcelName() {
            return beginExcelName;
        }

        public void setBeginExcelName(String beginExcelName) {
            this.beginExcelName = beginExcelName;
        }

        public String getEndExcelName() {
            return endExcelName;
        }

        public void setEndExcelName(String endExcelName) {
            this.endExcelName = endExcelName;
        }

        public Integer getModleId() {
            return modleId;
        }

        public void setModleId(Integer modleId) {
            this.modleId = modleId;
        }

        public ModleField getModleField() {
            return modleField;
        }

        public void setModleField(ModleField modleField) {
            this.modleField = modleField;
        }
    }

    /**
     * sheet公式
     */
    public static class Formula{
        //字段id
        private Integer modleId;
        //初期公式
        private String beginFormula;
        //期末公式
        private String endFormula;

        public Integer getModleId() {
            return modleId;
        }

        public void setModleId(Integer modleId) {
            this.modleId = modleId;
        }

        public String getBeginFormula() {
            return beginFormula;
        }

        public void setBeginFormula(String beginFormula) {
            this.beginFormula = beginFormula;
        }

        public String getEndFormula() {
            return endFormula;
        }

        public void setEndFormula(String endFormula) {
            this.endFormula = endFormula;
        }
    }
}
