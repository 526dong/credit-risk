package com.ccx.credit.risk.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ccx.credit.risk.model.Module;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.UserNum;
import com.ccx.credit.risk.model.vo.UserVo;

/**
 * 
 * @description User 表数据库控制层接口
 * @author zxr
 * @date 2017 上午11:57:12
 */
public interface UserMapper extends BaseMapper<User>{

	/**
	 * 根据用户名获取用户信息
	 * @param loginName
	 * @return
	 */
	List<User> selectListByLoginName(@Param("loginName") String loginName);

	/**
	 * 查询分页信息
	 * @param page
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> selectUserPage(Page<Map<String, Object>> page, Map<String, Object> condition);

	/**
	 * 查询页面显示的用户信息
	 * @return
	 */
	List<UserVo> findAll(Map<String,Object> params);
	
	/**
	 * 
	 * @Title: getUserNumByInsId  
	 * @author: WXN
	 * @Description: 通过机构id获取该机构下最多可设置的账号数量(这里用一句话描述这个方法的作用)   
	 * @param: @param id
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	UserNum getUserNumByInsId(long insId);
	
	/**
	 * 
	 * @Title: getHasUserNum  
	 * @author: WXN
	 * @Description: 获取该机构下已经创建的账号数量(这里用一句话描述这个方法的作用)   
	 * @param: @param insId
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	int getHasUserNum(long insId);

	/**
	 * 展示用户信息
	 * @param id
	 * @return
	 */
	User selectUserById(@Param("id") Long id);

	/**
	 * 修改用户信息
	 * @param user
	 */
	int updateTO(User user);

	/**
	 * 保存新增用户信息
	 * @param user
	 */
	void doAddUser(User user);

	/**
	 * 根据loginName得到user
	 * @param loginName
	 * @return
	 */
	User getUserByName(@Param("loginName") String loginName);
	
	List<User> getUserListByName(String loginName);

	/**
	 * 冻结用户
	 * @param id
	 */
	int freezeUser(Long id);

	/**
	 * 解冻用户
	 * @param id
	 */
	int unfreezeUser(Long id);

	/**
	 * 用户被锁定
	 * @param id 
	 */
	void lockUser(Long id);

	/**
	 * 根据id注销用户
	 * @param id
	 */
	int deleteUser(Long id);
	
	/**
	 * 
	 * @Title: getModuleByInsId  
	 * @author: WXN
	 * @Description: 获取后台给机构分配的模块 
	 * @param: @param insId
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	List<Module> getModuleByInsId(Long insId);
	
	/**
	 * 
	 * @Title: getMyModuleByInsId  
	 * @author: WXN
	 * @Description: 查询当前登陆人所拥有的模块
	 * @param: @param insId
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	List<Module> getMyModuleByInsId(@Param("insId")Long insId,@Param("userId")Long userId);
	
	/**
	 * 
	 * @Title: getMyCanUseModuleByInsId  
	 * @author: WXN
	 * @Description: 查询当前登陆人所拥有的模块,不限制日期
	 * @param: @param insId
	 * @param: @param userId
	 * @param: @return      
	 * @return: List<Module>      
	 * @throws
	 */
	List<Module> getMyCanUseModuleByInsId(@Param("insId")Long insId,@Param("userId")Long userId);
	
	
}
