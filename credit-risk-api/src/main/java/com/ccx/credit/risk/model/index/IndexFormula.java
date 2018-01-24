package com.ccx.credit.risk.model.index;

import java.util.Date;
import java.util.List;

public class IndexFormula {
    private Integer id;

    private String formulaId;

    private String formulaName;

    private String formulaContent;

    private String formulaContent1;

    private String formulaContent2;

    private String formulaContent3;

    private Integer year;

    private Integer parentId;

    private Integer parentFlag;

    private String creator;

    private Date createTime;

    private String createTimeStr;

    private Integer yearLen;

    private List<IndexFormula> formulaList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formulaId) {
        this.formulaId = formulaId == null ? null : formulaId.trim();
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName == null ? null : formulaName.trim();
    }

    public String getFormulaContent() {
        return formulaContent;
    }

    public void setFormulaContent(String formulaContent) {
        this.formulaContent = formulaContent == null ? null : formulaContent.trim();
    }

    public String getFormulaContent1() {
        return formulaContent1;
    }

    public void setFormulaContent1(String formulaContent1) {
        this.formulaContent1 = formulaContent1;
    }

    public String getFormulaContent2() {
        return formulaContent2;
    }

    public void setFormulaContent2(String formulaContent2) {
        this.formulaContent2 = formulaContent2;
    }

    public String getFormulaContent3() {
        return formulaContent3;
    }

    public void setFormulaContent3(String formulaContent3) {
        this.formulaContent3 = formulaContent3;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(Integer parentFlag) {
        this.parentFlag = parentFlag;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getYearLen() {
        return yearLen;
    }

    public void setYearLen(Integer yearLen) {
        this.yearLen = yearLen;
    }

    public List<IndexFormula> getFormulaList() {
        return formulaList;
    }

    public void setFormulaList(List<IndexFormula> formulaList) {
        this.formulaList = formulaList;
    }
}