package com.ccx.credit.risk.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.credit.risk.util.Record;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccx.credit.risk.api.RoleApi;
import com.ccx.credit.risk.api.UserApi;
import com.ccx.credit.risk.api.UserRoleApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.UserNum;
import com.ccx.credit.risk.model.UserRole;
import com.ccx.credit.risk.model.vo.UserVo;
import com.ccx.credit.risk.shiro.ShiroSpringCacheManager;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.MD5;
import com.ccx.credit.risk.util.UsedUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * 
 * @description 用户管理
 * @author zxr
 * @date 2017 下午2:19:25
 */
@Controller
@RequestMapping("/user")
public class UserController extends BasicController{

	private static Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserApi userApi;

	@Autowired
	private RoleApi roleApi;

	@Autowired
	private UserRoleApi userRoleApi;

	@Autowired
	ApplicationContext context;

	/**
	 * 用户管理页
	 *
	 * @return
	 */
	@GetMapping("/manager")
	public String manager() {
		return "user/user";
	}

	/**
	 * 用户列表
	 * 
	 * @return
	 */
	@PostMapping("/findAll")
	@ResponseBody
	@Record(operationType="查看用户列表",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public PageInfo<UserVo> findAll(HttpServletRequest request) {
		PageInfo<UserVo> pages = new PageInfo<UserVo>();
		// 获取查询条件
		Map<String, Object> params = ControllerUtil.requestMap(request);
		User user =ControllerUtil.getSessionUser(request);
		if(UsedUtil.isNotNull(user)){
    		int insId = user.getInstitutionId();
    		params.put("insId", insId);
    	}
		// 获取当前页数
		String currentPage = (String) params.get("currentPage");
		//获取每页展示数
		String pageSize = (String) params.get("pageSize");
		//当前页数
		int pageNum = 1;
		if (UsedUtil.isNotNull(currentPage)) { 
			pageNum = Integer.valueOf(currentPage);
		}
		//设置每页展示数
		int pageSizes = 10;
		if(UsedUtil.isNotNull(pageSize)){ 
			pageSizes = Integer.valueOf(pageSize);
		}
		PageHelper.startPage(pageNum, pageSizes);
		pages = userApi.findAll(params);
		return pages;
	}

	@RequestMapping(value="/checkUserNumLimit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> checkUserNumLimit(HttpServletRequest request) {
		User user =ControllerUtil.getSessionUser(request);
		Map<String, Object> mapResult = new HashMap<String, Object>();
		String result = "999";
		if(UsedUtil.isNotNull(user)){
    		int insId = user.getInstitutionId();
    		//通过机构id获取该机构下最多可设置的账号数量
    		UserNum userNumBean = userApi.getUserNumByInsId(insId);
    		int userNum = 0;
    		if(null != userNumBean){
    			userNum = userNumBean.getLimitnum();
    		}
    		mapResult.put("userNumLimit", userNum);
    		//通过机构id获取该机构下已经拥有的账号数量
    		int hasUserNum = userApi.getHasUserNum(insId);
    		if(hasUserNum>=userNum){
    			result = "999";
    		}else{
    			result = "0000";//只有当已经拥有的用户数量少于上限的时候，才能继续创建用户
    		}
    	}else{
    		result = "999";
    	}
		mapResult.put("msg", result);
		return mapResult;
	}
	
	/**
	 * 跳转到编辑用户页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@Record(operationType="跳转到编辑页面",
			operationBasicModule="系统管理",
			operationConcreteModule ="编辑用户")
	public String update(HttpServletRequest request) {
		Long id = (long) Integer.parseInt(request.getParameter("id"));
		// 根据用户id查询到对应的角色roleId
		UserRole userRole = userRoleApi.selectUserRole(id);
		// 角色名称列表
		List<Map<String, Object>> roleList = new ArrayList<Map<String, Object>>();
		User userr =ControllerUtil.getSessionUser(request);
		if(UsedUtil.isNotNull(userr)){
    		int insId = userr.getInstitutionId();
    		roleList = roleApi.findRoleByInstitutionId(insId);
    	}
		
		// 用户信息
		User user = userApi.selectUserById(id);
		if (user != null) {
			request.setAttribute("user", user);
			request.setAttribute("userId", id);
			request.setAttribute("userRole", userRole);
			if(null != roleList && roleList.size()>0){
				for (int i = 0; i < roleList.size(); i++) {
					Map<String, Object> map = roleList.get(i);
					long roleid = (long) map.get("id");
					if (roleid == userRole.getRoleId() ) {
						request.setAttribute("userRoleName", (String) map.get("name"));
					}
				}
			}
		}
		request.setAttribute("roleList", roleList);

		return "user/editUser";
	}

	/**
	 * 保存修改的用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updateTo", method = RequestMethod.POST)
	@ResponseBody
	@Record(operationType="保存用户修改",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户修改")
	public Map<String, String> updateTo(HttpServletRequest request) {
		
		String userIdd=request.getParameter("userId") == null ? "" : request.getParameter("userId").trim();
		String userRoleIdd=request.getParameter("userRoleId") == null ? "" : request.getParameter("userRoleId").trim();
		//String loginName=request.getParameter("loginName") == null ? "" : request.getParameter("loginName").trim();
    	String name=request.getParameter("name") == null ? "" : request.getParameter("name").trim();
    	String roleIdd=request.getParameter("roleId") == null ? "" : request.getParameter("roleId").trim();
    	String phone=request.getParameter("phone") == null ? "" : request.getParameter("phone").trim();
    	String email=request.getParameter("email") == null ? "" : request.getParameter("email").trim();
    	User user = new User();
    	user.setId(Long.valueOf(userIdd));
    	//user.setLoginName(loginName);
    	user.setName(name);
    	user.setPhone(phone);
    	user.setEmail(email);
		UserRole userRole = new UserRole();
		userRole.setId(Long.valueOf(userRoleIdd));
		userRole.setRoleId(Long.valueOf(roleIdd));
		Map<String, String> map = new HashMap<>();
		try {
			userApi.updateTO(user);
			userRoleApi.updateRoleId(userRole);
			logger.debug("角色修改成功！");
			map.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("角色修改失败！");
			map.put("result", "0");
		}
		return map;
	}

	/**
	 * 跳转到新增用户页面
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	@Record(operationType="跳转新增用户页",
			operationBasicModule="系统管理",
			operationConcreteModule ="新增用户")
	public String add(HttpServletRequest request) {
		//获取当前登录用户信息
    	User user =ControllerUtil.getSessionUser(request);
    	List<Map<String, Object>> roleList = null;
    	if(UsedUtil.isNotNull(user)){
    		roleList = roleApi.findRoleByInstitutionId(user.getInstitutionId());
    	}
    	
		request.setAttribute("roleList", roleList);
		return "/user/addUser";
	}

	/**
	 * 保存新增用户的信息
	 */
	@PostMapping("/doAdd")
	@ResponseBody
	@Record(operationType="添加用户",
			operationBasicModule="系统管理",
			operationConcreteModule ="新增用户")
	public Map<String, String> doAdd(HttpServletRequest request) {
		logger.debug("保存用户创建信息");
		String loginName=request.getParameter("loginName") == null ? "" : request.getParameter("loginName").trim();
    	String name=request.getParameter("name") == null ? "" : request.getParameter("name").trim();
    	String password=request.getParameter("password") == null ? "" : request.getParameter("password").trim();
    	String roleIdd=request.getParameter("roleId") == null ? "" : request.getParameter("roleId").trim();
    	String phone=request.getParameter("phone") == null ? "" : request.getParameter("phone").trim();
    	String email=request.getParameter("email") == null ? "" : request.getParameter("email").trim();
    	User user = new User();
    	user.setLoginName(loginName);
    	user.setName(name);
    	user.setPhone(phone);
    	user.setEmail(email);
    	// 获取当前时间
		Date createTime = new Date();
		password = MD5.encryption(password);
		// 向角色表中添加创建时间
		user.setCreateTime(createTime);
		user.setPassword(password);
		user.setUserType(2);
		user.setStatus(0);
		user.setIsDel(0);
		User userr =ControllerUtil.getSessionUser(request);
		if(UsedUtil.isNotNull(userr)){
    		int insId = userr.getInstitutionId();
    		user.setInstitutionId(insId);
    		user.setCreater(userr.getName());
    	}
		UserRole userRole = new UserRole();
		Long roleId = Long.parseLong(roleIdd);
		Map<String, String> map = new HashMap<>();
		try {
			userApi.doAddUser(user);
			// 在用户角色中间表中插入对应的id
			userRole.setUserId(user.getId());
			userRole.setRoleId(roleId);
			userRoleApi.addRoleToUser(userRole);
			logger.debug("角色创建成功！");
			map.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("角色创建失败！");
			map.put("result", "0");
		}
		return map;
	}

	/**
	 * 跳转到修改密码界面
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String reset(HttpServletRequest request) {
		long id = Long.parseLong(request.getParameter("id"));
		User user = userApi.selectUserById(id);
		if (user != null) {
			request.setAttribute("user", user);
			request.setAttribute("userId", id);
		}
		return "/user/resetPassword";
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/doReset", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> doReset(HttpServletRequest request) {
		User user = new User();
		long id = Long.parseLong(request.getParameter("id"));
		String password = MD5.encryption(request.getParameter("password"));
		user.setId(id);
		user.setPassword(password);
		ShiroSpringCacheManager shiroSpringCacheManager = (ShiroSpringCacheManager) context.getBean("shiroSpringCacheManager");
		Cache cache = shiroSpringCacheManager.getCacheManager().getCache("halfHour");
		cache.clear();
		userApi.unlock(id,cache);
		Map<String, String> map = new HashMap<>();
		try {
			userApi.resetPassword(user);
			logger.debug("密码修改成功！");
			map.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("密码修改失败！");
			map.put("result", "0");
		}
		return map;
	}

	/**
	 * 检查原密码是否正确
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> check(HttpServletRequest request) {
		// 获取页面输入的旧密码
		String oldpwd = request.getParameter("oldpwd");
		String password = MD5.encryption(oldpwd);
		// 根据用户id查询用户的旧密码
		long id = Long.parseLong(request.getParameter("id"));
		User user = userApi.selectUserById(id);
		Map<String, String> map = new HashMap<>();
		if (user.getPassword().equals(password)) {
			map.put("result", "1");
		} else {
			map.put("result", "0");
		}
		return map;
	}
	
	/**
	 * 冻结用户
	 * @param request
	 */
	@RequestMapping(value="/freeze",method=RequestMethod.POST)
	@ResponseBody
	@Record(operationType="冻结用户",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public String freeze(HttpServletRequest request){
		Long id = Long.parseLong(request.getParameter("id"));
		String result = "999";
		try {
			result = userApi.freezeUser(id);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
	/**
	 * 解冻用户
	 * @return
	 */
	@RequestMapping(value="/unfreeze",method=RequestMethod.POST)
	@ResponseBody
	@Record(operationType="解冻用户",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public String unfreeze(HttpServletRequest request){
		Long id = Long.parseLong(request.getParameter("id"));
		String result = "999";
		try {
			result = userApi.unfreeze(id);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
	/**
	 * 解锁用户
	 * @return
	 */
	@RequestMapping(value="/unlock",method=RequestMethod.POST)
	@ResponseBody
	@Record(operationType="解锁用户",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public String unlock(HttpServletRequest request){
		User newUserBean = new User();
		Long id = Long.parseLong(request.getParameter("id"));
		String result = "999";
		try {
			newUserBean.setId(id);
			newUserBean.setLoginNum(0);
			newUserBean.setStatus(0);
			result = userApi.updateTO(newUserBean);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
	/**
	 * 检验账号是否存在
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkLoginName",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> checkLoginName(HttpServletRequest request){
		Map<String,String> map = new HashMap<>();
		String loginName = request.getParameter("loginName");
//		User userList = userApi.getUserByName(loginName);
		List<User> userList = userApi.getUserListByName(loginName);
		if(null != userList && userList.size()>0){
			map.put("result", "0");
		}else{
			map.put("result", "1");
		}
		return map;
	}
	
	/**
	 * 注销用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/isDel",method=RequestMethod.POST)
	@ResponseBody
	@Record(operationType="注销用户",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public String isDel(HttpServletRequest request){
		Long id = Long.parseLong(request.getParameter("id"));
		String result = "999";
		try {
			result = userApi.deleteUser(id);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
	/**
	 * 修改密码
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/updatePassword",method=RequestMethod.POST)
	@ResponseBody
	@Record(operationType="修改密码",
			operationBasicModule="系统管理",
			operationConcreteModule ="用户管理")
	public String updatePassword(HttpServletRequest request){
		Long userId = Long.parseLong(request.getParameter("userId"));
		String password = (String)request.getParameter("passwordNew");
		password = MD5.encryption(password);
		User user = new User();
		String result = "999";
		try {
			user.setId(userId);
			user.setPassword(password);
			result = userApi.updateTO(user);
		} catch (Exception e) {
			result = "999";
		}
		return result;
	}
	
	
}
