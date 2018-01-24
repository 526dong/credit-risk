package com.ccx.credit.risk.api;

import java.util.List;

import com.ccx.credit.risk.model.RoleResource;

public interface RoleRessourceApi {

	List<Long> selectResIds(Long roleId);

	List<RoleResource> selectByRoleId(Long roleId);

}
