package com.ccx.credit.risk.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.IPApi;
import com.ccx.credit.risk.mapper.IPMapper;
import com.ccx.credit.risk.model.Ip;
import com.ccx.credit.risk.model.User;
import com.github.pagehelper.PageInfo;

/**
* <p>Title: IPApiImpl.java<／p>
* <p>Description: <／p>
* @author dym
* @date 2017年7月1日下午2:09:30
*/

@Service("IPApi")
public class IPApiImpl implements IPApi {
	
	@Autowired
	private IPMapper ipMapper;

	@Override
	public PageInfo<Ip> findIPList(Map<String, Object> params) {
		List<Ip> ipList = ipMapper.findIPList(params);
		PageInfo<Ip> pages = new PageInfo<Ip>(ipList);
		return pages;
	}

	@Override
	public Ip getIP(HttpServletRequest request) {
		Ip ip = new Ip();
		String ipAddress = request.getParameter("ipAddress");
		User user = (User) request.getSession().getAttribute("user");
		String creator = user.getName();
		Integer ipUpperLimit = Integer.parseInt(request.getParameter("ipUpperLimit"));
		ip.setIpAddress(ipAddress);
		ip.setCreateTime(new Date());
		ip.setCreator(creator);
		ip.setState(0);
		ip.setIpUpperLimit(ipUpperLimit);
		return ip;
	}

	@Override
	public void doAddIP(Ip ip) {
		ipMapper.insert(ip);
	}

	@Override
	public void updateState(Integer id) {
		ipMapper.updateState(id);
	}

	@Override
	public void updateByPrimaryKey(Map<String, Object> map) {
		ipMapper.updateByPrimaryKey(map);
	}

	@Override
	public Integer selectIPCount() {
		Integer ipCount = ipMapper.selectIPCount();
		return ipCount;
	}

	@Override
	public void setUpperLimit(Integer ipLimit) {
		ipMapper.setUpperLimit(ipLimit);
	}

	@Override
	public Ip selectByPrimaryKey(Integer id) {
		Ip ip = ipMapper.selectByPrimaryKey(id);
		return ip;
	}
	
	//查询ip是否存在
	@Override
	public int checkIP(String loginIp,long insId){
		return ipMapper.checkIP(loginIp,insId);
	}
	
}
