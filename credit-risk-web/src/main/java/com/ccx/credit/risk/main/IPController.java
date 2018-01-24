package com.ccx.credit.risk.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ccx.credit.risk.util.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccx.credit.risk.api.IPApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.Ip;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.UsedUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/ip")
public class IPController extends BasicController{

	@Autowired
	private IPApi ipApi;

	/**
	 * IP管理页
	 */
	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	@Record(operationType="查看ip管理页",
			operationBasicModule="系统管理",
			operationConcreteModule ="ip管理")
	public String manager() {
		return "/ip/ipList";
	}

	/**
	 * 显示所有ip数据
	 */
	@RequestMapping(value="/findIPList",method=RequestMethod.POST)
	@ResponseBody
	public PageInfo<Ip> selectAll(HttpServletRequest request) {
		PageInfo<Ip> pages = new PageInfo<Ip>();
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
		pages = ipApi.findIPList(params);
   		return pages;
	}
	
	/**
	 * 点击添加ip时,检验ip数量和ip上限
	 */
	@RequestMapping(value="/check",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,String> checkIPCount(HttpServletRequest request){
		Map<String,String> map = new HashMap<>();
		//现在可用的ip数量
		Integer ipCount = ipApi.selectIPCount();
		//ip上限数量
		Integer ipUpperLimit = Integer.parseInt(request.getParameter("ipUpperLimit"));
		if(ipCount < ipUpperLimit){
			map.put("result", "1");
		}else{
			map.put("result", "0");
		}
		return map;
	}
	
	/**
	 * 新增ip页面
	 */
	@RequestMapping(value="/addIP",method=RequestMethod.GET)
	@Record(operationType="跳转新增ip页面",
			operationBasicModule="系统管理",
			operationConcreteModule ="ip管理")
	public String add(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		request.setAttribute("user", user);
		return "/ip/addIP";
	}
	
	/**
	 * 保存新增的ip
	 */
	@RequestMapping(value="/doAdd",method=RequestMethod.POST)
	@Record(operationType="添加ip",
			operationBasicModule="系统管理",
			operationConcreteModule ="ip管理")
	public Map<String, String> doAdd(HttpServletRequest request){
		Map<String,String> map = new HashMap<>();
		Ip ip = ipApi.getIP(request);
		try {
			ipApi.doAddIP(ip);
			map.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("result", "0");
		}
		return map;
	}
	
	/**
	 * 删除ip
	 */
	@RequestMapping(value="/deleteIp",method=RequestMethod.GET)
	@Record(operationType="删除ip",
			operationBasicModule="系统管理",
			operationConcreteModule ="ip管理")
	public String delete(HttpServletRequest request){
		Integer id = Integer.parseInt(request.getParameter("id"));
		ipApi.updateState(id);
		return "redirect:/ip/manager";
	}
	
	/**
	 * 跳转到修改ip页面
	 */
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(HttpServletRequest request){
		Integer id = Integer.parseInt(request.getParameter("id"));
		Ip ip = ipApi.selectByPrimaryKey(id);
		request.setAttribute("id", id);
		request.setAttribute("ipAddress", ip.getIpAddress());
		return "/ip/editIP";
	}
	
	/**
	 * 修改ip
	 */
	@RequestMapping(value="/doEdit",method = RequestMethod.POST)
	@ResponseBody
	@Record(operationType="修改ip",
			operationBasicModule="系统管理",
			operationConcreteModule ="ip管理")
	public Map<String,String> doEdit(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		map.put("id", Integer.parseInt(request.getParameter("id")));
		map.put("ipAddress", request.getParameter("ipAddress"));
		Map<String,String> hMap = new HashMap<>();
		try {
			ipApi.updateByPrimaryKey(map);
			hMap.put("result", "1");
		} catch (Exception e) {
			e.printStackTrace();
			hMap.put("result", "0");
		}
		return hMap;
	}
	
	/**
	 * 修改ip上限
	 */
	@RequestMapping()
	public Map<String,Integer> checkIPLimit(HttpServletRequest request){
		Map<String,Integer> map = new HashMap<>();
		//获取ip上限
		Integer ipLimit = Integer.parseInt(request.getParameter("ipUpperLimit"));
		//获取现在可用的ip数量
		Integer ipCount = ipApi.selectIPCount();
		if(ipLimit < ipCount){
			map.put("result", 0);
		}else{
			//修改ip上限
			ipApi.setUpperLimit(ipLimit);
			map.put("result", 1);
		}
		return map;
	}
}
