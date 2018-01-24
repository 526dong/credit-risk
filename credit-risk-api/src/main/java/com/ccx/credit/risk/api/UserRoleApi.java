package com.ccx.credit.risk.api;

import javax.servlet.http.HttpServletRequest;

import com.ccx.credit.risk.model.UserRole;

public interface UserRoleApi {


	void updateRoleId(UserRole userRole);

	void addRoleToUser(UserRole userRole);

	UserRole selectUserRole(Long id);

	Long selectRoleId(Long id);

	UserRole getUserRole(HttpServletRequest request);
	
}
