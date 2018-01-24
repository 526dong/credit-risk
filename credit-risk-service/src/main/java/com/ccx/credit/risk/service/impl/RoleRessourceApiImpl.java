package com.ccx.credit.risk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.RoleRessourceApi;
import com.ccx.credit.risk.mapper.RoleResourceMapper;
import com.ccx.credit.risk.model.RoleResource;

@Service("roleResApi")
public class RoleRessourceApiImpl implements RoleRessourceApi {

	@Autowired
	private RoleResourceMapper roleResMapper;
	
	@Override
	public List<Long> selectResIds(Long roleId) {
		List<Long> roleList = roleResMapper.selectResIds(roleId);
		return roleList;
	}

	@Override
	public List<RoleResource> selectByRoleId(Long roleId) {
		List<RoleResource> resList = roleResMapper.selectByRoleId(roleId);
		return resList;
	}
}
