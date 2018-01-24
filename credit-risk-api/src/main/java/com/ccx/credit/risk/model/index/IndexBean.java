package com.ccx.credit.risk.model.index;

import java.io.Serializable;
import java.util.List;
/**
 * 指标库实体
 * @author sunqi update by xzd on 2017/7/6
 *
 */
public class IndexBean{
	private Integer id;

	private String indexCode;

	private String indexName;

	private String equalPick;

	private Integer aveYears;

	private String regularIndexFlag;

	private String varName;

	private Double indexWeight;

	private String creditFlag;

    private Integer state;

    private Integer elementId;
	
	private Integer formulaId;

    private String formulaName;

	private String indexEnName;

	private String indexDescribe;

	/*指标规则集合*/
	private List<IndexRule> ruleList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode == null ? null : indexCode.trim();
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName == null ? null : indexName.trim();
	}

	public String getEqualPick() {
		return equalPick;
	}

	public void setEqualPick(String equalPick) {
		this.equalPick = equalPick == null ? null : equalPick.trim();
	}

	public Integer getAveYears() {
		return aveYears;
	}

	public void setAveYears(Integer aveYears) {
		this.aveYears = aveYears;
	}

	public String getRegularIndexFlag() {
		return regularIndexFlag;
	}

	public void setRegularIndexFlag(String regularIndexFlag) {
		this.regularIndexFlag = regularIndexFlag == null ? null : regularIndexFlag.trim();
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName == null ? null : varName.trim();
	}

	public Double getIndexWeight() {
		return indexWeight;
	}

	public void setIndexWeight(Double indexWeight) {
		this.indexWeight = indexWeight;
	}

	public String getCreditFlag() {
		return creditFlag;
	}

	public void setCreditFlag(String creditFlag) {
		this.creditFlag = creditFlag == null ? null : creditFlag.trim();
	}

	public Integer getState() {
			return state;
		}

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(Integer elementId) {
        this.elementId = elementId;
    }

	public Integer getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(Integer formulaId) {
        this.formulaId = formulaId;
    }

    public String getFormulaName() {
        return formulaName;
    }

    public void setFormulaName(String formulaName) {
        this.formulaName = formulaName == null ? null : formulaName.trim();
    }

   public String getIndexEnName() {
        return indexEnName;
    }

    public void setIndexEnName(String indexEnName) {
        this.indexEnName = indexEnName == null ? null : indexEnName.trim();
    }

    public String getIndexDescribe() {
        return indexDescribe;
    }

    public void setIndexDescribe(String indexDescribe) {
        this.indexDescribe = indexDescribe == null ? null : indexDescribe.trim();
    }



	public List<IndexRule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(List<IndexRule> ruleList) {
		this.ruleList = ruleList;
	}
}
