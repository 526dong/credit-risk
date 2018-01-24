package com.ccx.credit.risk.model.enterprise;

import java.io.Serializable;
/**
 * 地区表
 * @author xzd
 * @date 2017/6/28
 */
public class Region implements Serializable{
	private static final long serialVersionUID = 5675116143110526020L;
	
	private Integer id;//代码
	private String name;//地区名称
	private Integer pid;//父节点id
	
	public Region() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return "Region [id=" + id + ", name=" + name + ", pid=" + pid + "]";
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getPid() {
		return pid;
	}
	
	public void setPid(Integer pid) {
		this.pid = pid;
	}
}
