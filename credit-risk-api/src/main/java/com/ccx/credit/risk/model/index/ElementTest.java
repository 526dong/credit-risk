package com.ccx.credit.risk.model.index;

import java.io.Serializable;
/**
 * 因素实体 test
 * @author xzd
 * @date 2017/7/6
 */
public class ElementTest implements Serializable{
	private static final long serialVersionUID = -6357458970709019640L;

	private Integer id;
	
	/*因素编号*/
	private String code;
	
	/*因素名称*/
	private String name;
	
	/*后台行业id*/
	private Integer industryId;

	public ElementTest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ElementTest [id=" + id + ", code=" + code + ", name=" + name + ", industryId=" + industryId + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}
	
}
