package com.ccx.credit.risk.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ccx.credit.risk.model.PermissionBean;
import com.ccx.credit.risk.model.Role;

/**
 * 
 * @description Role 表数据库控制层接口
 * @author zxr
 * @date 2017 下午12:05:11
 */
public interface RoleMapper extends BaseMapper<Role>{

	/**
	 * 查询角色对应的资源
	 * @param roleId
	 * @return
	 */
	List<Map<Long, String>> selectResourceListByRoleId(@Param("roleId") Long roleId);

	/**
	 * 多个角色对应多个资源
	 * @param list
	 * @return
	 */
	List<PermissionBean> selectResourceListByRoleIdList(@Param("list") List<Long> list);
	
	/**
	 * 获取角色列表分页模型
	 * @param params 
	 * @return
	 */
	List<Role> findAll(Map<String, Object> params);

	/**
	 * 
	 * @Title: selectUserByRoleId  
	 * @author: WXN
	 * @Description:查询该角色下是否存在用户 
	 * @param: @param roleId
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	int selectUserByRoleId(long roleId);
	/**
	 * 逻辑删除角色
	 * @param id
	 * @return
	 */
	int deleteByRoleId(@Param("id") long id);

	/**
	 * 获取角色信息
	 * @param id
	 * @return
	 */
	Role selectRoleById(@Param("id") Long id);

	/**
	 * 修改角色
	 * @param uRole
	 */
	void updateRoleById(Role uRole);

	/**
	 * 创建角色
	 * @param role
	 */
	void doAddRole(Role role);
	
	/**
	 * 
	 * @Title: getRoleByName  
	 * @author: WXN
	 * @Description: 校验角色是否唯一
	 * @param: @param map
	 * @param: @return      
	 * @return: Role      
	 * @throws
	 */
	Role getRoleByName(Map<String, Object> map);

	/**
	 * 修改用户角色
	 * @param id
	 * @param roleId
	 */
	void updateRoleId(@Param("id") long id, @Param("roleId") Long roleId);

	/**
	 * 获取所有角色集合
	 * @return
	 */
	List<Role> findAllRole();
	
	List<Map<String, Object>> findRoleByInstitutionId(long id);

}
