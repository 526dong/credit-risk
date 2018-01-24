package com.ccx.credit.risk.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.credit.risk.model.Ip;
import com.github.pagehelper.PageInfo;

/**
* <p>Title: IPApi.java<／p>
* <p>Description: <／p>
* @author dym
* @date 2017年7月1日下午2:08:16
*/
public interface IPApi {

	//查询所有ip数据
	PageInfo<Ip> findIPList(Map<String, Object> params);

	//获取要保存的ip信息
	Ip getIP(HttpServletRequest request);

	//保存ip
	void doAddIP(Ip ip);

	//逻辑删除ip
	void updateState(Integer id);

	//修改ip
	void updateByPrimaryKey(Map<String, Object> map);

	//查询现有ip数量(可用)
	Integer selectIPCount();

	//修改上限数量
	void setUpperLimit(Integer ipLimit);

	//根据id查询ip信息
	Ip selectByPrimaryKey(Integer id);
	
	//查询ip是否存在
	int checkIP(String loginIp,long insId);

}
