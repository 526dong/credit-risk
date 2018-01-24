package com.ccx.credit.risk.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.service.IService;
import com.ccx.credit.risk.model.Role;
import com.ccx.credit.risk.model.RoleResource;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @description Role 表数据服务层接口
 * @author zxr
 * @date 2017 上午11:56:04
 */
public interface RoleApi extends IService<Role>{


	/**
	 * 根据用户ID查询资源集合
	 * @param id
	 * @return
	 */
	Map<String, Set<String>> selectResourceMapByUserId(Long userId);

	//获取角色列表模型
	PageInfo<Role> findAll(Map<String, Object> params);

	int selectUserByRoleId(long roleId);
	//逻辑删除角色
	String deleteByRoleId(long id);

	Role selectRoleById(Long id);

	//修改角色
	void updateTO(Role uRole);

	//创建角色
	void doAddRole(Role role);
	
	//校验角色是否唯一
	Role getRoleByName(Map<String, Object> map);

	//给角色分配权限
	void addRes2Role(Long roleId, String resIds);

	//获取所有角色集合
	List<Role> findAllRole();

	//返回树数据
	JSONArray treeData(HttpServletRequest request, List<RoleResource> roleRes);

	//分配权限时显示列表
	JSONArray showTree(List<RoleResource> roleRes,long insId);

	Object selectTree();
	
	List<Map<String, Object>> findRoleByInstitutionId(long id);

}
