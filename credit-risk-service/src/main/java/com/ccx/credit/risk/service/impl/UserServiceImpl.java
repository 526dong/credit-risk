package com.ccx.credit.risk.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ccx.credit.risk.api.UserApi;
import com.ccx.credit.risk.mapper.UserMapper;
import com.ccx.credit.risk.model.Module;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.UserNum;
import com.ccx.credit.risk.model.vo.UserVo;
import com.ccx.credit.risk.util.MD5;
import com.github.pagehelper.PageInfo;
@Service("userApi")
public class UserServiceImpl implements UserApi{

	@Autowired
    private UserMapper userMapper;
	
	@Autowired
	ApplicationContext context;
	
	@Override
	public List<User> selectByLoginName(User userVo) {
		User user = new User();
        user.setLoginName(userVo.getLoginName());
        return userMapper.selectListByLoginName(user.getLoginName());
	}

	@Override
	public PageInfo<UserVo> findAll(Map<String,Object> params) {
		List<UserVo> list = userMapper.findAll(params);
		PageInfo<UserVo> pages = new PageInfo<UserVo>(list);
		return pages;
	}
		
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
	@Override
	public UserNum getUserNumByInsId(long insId){
		return userMapper.getUserNumByInsId(insId);
	}
	
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
	@Override
	public int getHasUserNum(long insId){
		return userMapper.getHasUserNum(insId);
	}

	@Override
	public User selectUserById(Long id) {
		User user = userMapper.selectUserById(id);
		return user;
	}

	@Override
	public String updateTO(User user) {
		String result = "999";
		try {
			int msg = userMapper.updateTO(user);
			if(msg>0){
				result = "0000";
			}else{
				result = "999";
			}
		} catch (Exception e) {
			result = "999";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void doAddUser(User user) {
		userMapper.doAddUser(user);
	}

	@Override
	public void resetPassword(User user) {
		userMapper.updateTO(user);
	}

	@Override
	public User getUserByName(String loginName) {
		User user = userMapper.getUserByName(loginName);
		return user;
	}
	
	@Override
	public List<User> getUserListByName(String loginName){
		return userMapper.getUserListByName(loginName);
	}

	@Override
	public String freezeUser(Long id) {
		String result = "999";
		try {
			int msg = userMapper.freezeUser(id);
			if(msg>0){
				result = "0000";
			}else{
				result = "999";
			}
		} catch (Exception e) {
			result = "999";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public User getUser(HttpServletRequest request, User user) {
		Integer organizationId = Integer.parseInt(request.getParameter("organizationId"));
		// 获取当前时间
		Date createTime = new Date();
		String password = MD5.encryption(request.getParameter("password"));
		// 向角色表中添加创建时间
		user.setCreateTime(createTime);
		user.setPassword(password);
		user.setUserType(0);
		user.setStatus(0);
		user.setIsDel(0);
		user.setInstitutionId(organizationId);
		return user;
	}

	@Override
	public User getEditUser(HttpServletRequest request, User user) {
		long id = Long.parseLong(request.getParameter("id"));
		Integer status = Integer.parseInt(request.getParameter("status"));
		String name = request.getParameter("name");

		user.setId(id);
		user.setStatus(status);
		user.setName(name);
		return user;
	}

	@Override
	public String unfreeze(Long id) {
		String result = "999";
		try {
			int msg = userMapper.unfreezeUser(id);
			if(msg>0){
				result = "0000";
			}else{
				result = "999";
			}
		} catch (Exception e) {
			result = "999";
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void lockUser(Long id) {
		userMapper.lockUser(id);
	}

	@Override
	public void unlock(Long id, Cache cache) {
		//User user = userMapper.selectUserById(id);
		cache.clear();
		userMapper.unfreezeUser(id);
	}

	@Override
	public String deleteUser(Long id) {
		String result = "999";
		try {
			int msg = userMapper.deleteUser(id);
			if(msg>0){
				result = "0000";
			}else{
				result = "999";
			}
		} catch (Exception e) {
			result = "999";
			e.printStackTrace();
		}
		return result;
	}

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
	@Override
	public List<Module> getModuleByInsId(Long insId){
		return userMapper.getModuleByInsId(insId);
	}
	
	/**
	 * 
	 * @Title: UserServiceImpl   
	 * @Description: 查询当前登陆人所拥有的模块 
	 * @param: @param insId
	 * @param: @param userId
	 * @param: @return
	 * @throws
	 */
	@Override
	public List<Module> getMyModuleByInsId(Long insId,Long userId){
		return userMapper.getMyModuleByInsId(insId,userId);
	}
	
	/**
	 * 
	 * @Title: getMyCanUseModuleByInsId   
	 * @Description: 查询当前登陆人所拥有的模块,不限制日期
	 * @param: @param insId
	 * @param: @param userId
	 * @param: @return
	 * @throws
	 */
	@Override
	public List<Module> getMyCanUseModuleByInsId(Long insId,Long userId){
		return userMapper.getMyCanUseModuleByInsId(insId,userId);
	}
	
	
}
 