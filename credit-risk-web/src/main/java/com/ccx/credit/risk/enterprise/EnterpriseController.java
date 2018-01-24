package com.ccx.credit.risk.enterprise;

import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.api.enterprise.EnterpriseIndustryApi;
import com.ccx.credit.risk.api.index.ElementApi;
import com.ccx.credit.risk.api.index.IndexApi;
import com.ccx.credit.risk.api.riskcheck.RiskCheckApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.mapper.rate.ApprovalMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.index.EnterpriseIndexRelation;
import com.ccx.credit.risk.model.index.IndexBean;
import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;
import com.ccx.credit.risk.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 创建企业
 * @author xzd
 * @date 2017/6/15
 */
@Controller
@RequestMapping(value="/enterprise")
public class EnterpriseController extends BasicController{
	private static Logger logger = LogManager.getLogger(EnterpriseController.class);

  	@Autowired
	private EnterpriseApi api;
	/*行业字典*/
	@Autowired
	private EnterpriseIndustryApi industryApi;
	/*指标*/
	@Autowired
	private IndexApi indexApi;
	/*指标因素*/
	@Autowired
	private ElementApi elementApi;
	@Autowired
	private ApprovalMapper approvalMapper;
	@Autowired
	private RiskCheckApi riskCheckApi;

	/**
	 * 创建企业列表页
	 */
	@GetMapping("/list")
	@Record(operationType="查看企业列表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="数据录入")
	public String list(){
		return "enterprise/enterpriseList";
	}

	/**
	 * 查询创建企业列表
	 * @return
	 */
	@PostMapping("/findAll")
	@ResponseBody
	public Map<String, Object> findAll(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取查询条件
		Map<String, Object> params = ControllerUtil.requestMap(request);
		PageInfo<Enterprise> pages = new PageInfo<Enterprise>();
		PageHelper.startPage(getPageNum(), getPageSize());
		try {
			pages = api.findAll(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", "企业创建列表展示信息查询失败");
			logger.error("企业创建列表展示信息查询失败", e);
		}
		return resultMap;
	}

	/**
	 * 验证企业主体名称的唯一性
	 * @return
	 */
	@PostMapping("/validateName")
	@ResponseBody
	public Map<String, Object> validateName(HttpServletRequest request, @RequestParam(required = true) Integer enterpriseId,
			@RequestParam(required = true) String name){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = 0;
		try {
			count = api.findByName(name.trim());
			//更新
			if (enterpriseId > 0) {
				if (count == 1) {
					Enterprise enterprise = api.findById(enterpriseId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (name.equals(enterprise.getName())) {
						resultMap.put("code", 200);
					} else {
						//不是当前查询到的，重复，不保存
						resultMap.put("code", 500);
						resultMap.put("msg", "主体名称已存在，请重新添加！");
						return resultMap;
					}
				} else if (count > 1) {
					resultMap.put("code", 500);
					resultMap.put("msg", "主体名称已存在，请重新添加！");
				} else {
					resultMap.put("code", 200);
				}
			} else {
				//新增
				if (count > 0) {
					resultMap.put("code", 500);
					resultMap.put("msg", "主体名称已存在，请重新添加！");
					return resultMap;
				} else {
					resultMap.put("code", 200);
				}
			}
		} catch (Exception e) {
			resultMap.put("code", 500);

			logger.error("通过主体名称查询企业主体失败", e);
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 验证企业代码标识的唯一性
	 * @return
	 */
	@PostMapping("/validateCreditCode")
	@ResponseBody
	public Map<String, Object> validateCreditCode(HttpServletRequest request, @RequestParam(required = true) Integer enterpriseId,
			@RequestParam(required = true) String creditCode){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		int count = 0;
		try {
			count = api.findByCreditCode(creditCode.trim());
			//更新
			if (enterpriseId > 0) {
				if (count == 1) {
					Enterprise enterprise = api.findById(enterpriseId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (creditCode.equals(enterprise.getCreditCode())) {
						resultMap.put("code", 200);
					} else {
						//不是当前查询到的，重复，不保存
						resultMap.put("code", 500);
						resultMap.put("msg", "企业代码标识已存在，请重新添加！");
						return resultMap;
					}
				} else if (count > 1) {
					resultMap.put("code", 500);
					resultMap.put("msg", "企业代码标识已存在，请重新添加！");
				} else {
					resultMap.put("code", 200);
				}
			} else {
				//新增
				if (count > 0) {
					resultMap.put("code", 500);
					resultMap.put("msg", "企业代码标识已存在，请重新添加！");
					return resultMap;
				} else {
					resultMap.put("code", 200);
				}
			}
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("通过企业代码标识查询企业主体失败", e);
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 受评主体数据库列表页
	 */
	@GetMapping("/evaluateList")
	public String evaluateList(){
		return "enterprise/evaluateList";
	}
	
	/**
	 * 查询受评主体数据库列表
	 * @return
	 */
	@PostMapping("/findAllEvaluate")
	@ResponseBody
    @Record(operationType="查询受评主体数据库列表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="受评企业数据库列表")
	public Map<String, Object> findAllEvaluate(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取查询条件
		Map<String, Object> params = ControllerUtil.requestMap(request);
		PageHelper.startPage(getPageNum(), getPageSize());
		try {
			PageInfo<Map<String, Object>> pages = api.findAllEvaluate(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("受评主体数据库列表查询失败", e);
		}
		return resultMap;
	}

	/**
	 * 受评主体数据库-历史评级
	 */
	@GetMapping("/evaluateHistory")
    @Record(operationType="查询受评主体数据库历史评级",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="受评企业数据库评级历史")
    public String evaluateHistory(HttpServletRequest request){
		//企业id
		request.setAttribute("entId", request.getParameter("id"));
		return "enterprise/evaluateHistoryList";
	}

	/**
	 * 查询受评主体数据库企业历史评级列表
	 * @return
	 */
	@PostMapping("/findEvaluateHistory")
	@ResponseBody
	public Map<String, Object> findEvaluateHistory(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取查询条件
		Map<String, Object> params = ControllerUtil.requestMap(request);
		PageHelper.startPage(getPageNum(), getPageSize());
		try {
			PageInfo<Map<String, Object>> pages = api.findEvaluateHistory(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("受评主体数据库列表查询失败", e);
		}
		return resultMap;
	}


	/**
	 * 异步加载二级行业
	 */
	@ResponseBody
	@PostMapping("/getIndustry")
	public Map<String, Object> getIndustry(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//加载二级行业
		resultMap = EnterpriseDataUtils.loadIndustry(request, industryApi);
		return resultMap;
	}
	
	/**
	 * 异步加载指标
	 */
	@ResponseBody
	@PostMapping("/getIndex")
	public Map<String, Object> getIndex(@RequestParam(required = true) Integer industry2Id,
			@RequestParam(required = true) Integer entType){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//参数
		Map<String, Integer> params = new HashMap<>();
		params.put("industry2Id", industry2Id);
		params.put("entType", entType);
		//指标列表
		List<IndexBean> indexList = null;
		try {
			//加载指标
			indexList = EnterpriseDataUtils.loadIndex(industryApi, indexApi, elementApi, params);
			resultMap.put("list", indexList);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("加载指标列表失败：", e);
		}
		return resultMap;
	}
	
	/**
	 * 跳转企业创建添加页面
	 * @return
	 */
	@GetMapping("/add")
	@Record(operationType="跳转企业创建页面",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="新建评级")
	public String add(HttpServletRequest request){
		//加载企业初始化数据
		EnterpriseDataUtils.loadInitData(request, api, industryApi);
		return "enterprise/enterpriseAdd";
	}
	
	/**
	 * 保存企业创建信息到数据库
	 */
	@PostMapping("/doAdd")
	@ResponseBody
	@Record(operationType="保存企业创建信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="新建评级")
	public Map<String, Object> doAdd(HttpServletRequest request, Enterprise enterprise){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//创建时间
		enterprise.setCreateDate(new Date());
		//评级类型：新评级
		enterprise.setType(0);
		//录入状态：未完成
		enterprise.setState(0);
		//创建人
		User user = ControllerUtil.getSessionUser(request);
		if (user != null) {
			enterprise.setCreatorName(user.getLoginName());
		}
		//生成流水号
		enterprise.setCid(EnterpriseDataUtils.getCid());
		try {
			api.insert(enterprise);
			resultMap.put("code", 200);
		}catch (Exception e){
			logger.error("企业创建失败！", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}
	
	/**
	 * 跳转企业创建更新页面
	 * @return
	 */
	@GetMapping("/update")
	@Record(operationType="跳转企业更新页面",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public String update(HttpServletRequest request){
		//企业id
		String id = request.getParameter("id");
		//跟踪评级标识
		String track = request.getParameter("track");
		if (!StringUtils.isEmpty(track)) {
			request.setAttribute("track", track);
		}
		//加载企业初始化数据
		EnterpriseDataUtils.loadInitData(request, api, industryApi);
		if (!StringUtils.isEmpty(id)) {
			//企业主体信息
			Enterprise enterprise = new Enterprise();
			try {
				enterprise = api.findById(Integer.parseInt(id));
				if (enterprise != null){
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, api);
					request.setAttribute("enterprise", enterprise);
				}
			} catch (Exception e) {
				logger.error("查询企业主体信息失败", e);
			}
			request.setAttribute("enterpriseId", id);
		}
		return "enterprise/enterpriseUpdate";
	}
	
	/**
	 * 保存企业创建信息到数据库
	 */
	@ResponseBody
	@PostMapping("/doUpdate")
	@Record(operationType="保存企业更新信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> doUpdate(HttpServletRequest request, Enterprise enterprise){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		String method = request.getParameter("updateReport");
		String appNum = request.getParameter("appNum");
		String s1 = "";
		String s2 = "";
		//更新时间
		enterprise.setUpdateDate(new Date());
		try {
			if ("updateReport".equals(method)) {
				Enterprise enterpriseBak = api.findById(enterprise.getId());
				enterpriseBak.setUpdateDate(enterprise.getUpdateDate());
				s1 = JsonUtils.toJson(enterpriseBak);
			}
			api.updateBySelect(enterprise);
			resultMap.put("code", 200);
			//比较修改
			if ("updateReport".equals(method)) {
				Enterprise enterprisenNew = api.findById(enterprise.getId());
				s2 = JsonUtils.toJson(enterprisenNew);
				if (!s1.equals(s2)) {
					approvalMapper.updateEditFlag(appNum, 1);
				}
			}
		}catch (Exception e){
			resultMap.put("code", 500);
			logger.error("企业更新失败！", e);
		}
		return resultMap;
	}

	/**
	 * 指标 index
	 * @param: request indexList
	 * @return map结果
	 */
	@ResponseBody
	@PostMapping("/doIndex")
	public Map<String, Object> doIndex(@RequestParam(required = true)Integer enterpriseId, 
									   @RequestParam(required = true)String indexIds,
									   @RequestParam(required = true)String ruleIds,
									   @RequestParam(required = true)String indexName,
									   @RequestParam(required = true)String ruleValue,
									   String appNum, String track, String updateReport){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//保存到数据库的指标list
		List<EnterpriseIndexRelation> batchList = new ArrayList<EnterpriseIndexRelation>();
		//指标id
		String[] indexIdsArr = indexIds.split(",");
		//规则id
		String[] ruleIdsArr = ruleIds.split(",");
		String[] nameArr = indexName.split(",");
		String[] valueArr = ruleValue.split(",");
		String s1 = "";
		String s2 = "";
		if (indexIdsArr.length == ruleIdsArr.length) {
			int len = ruleIdsArr.length;
			//更新企业id，指标id，规则id
			for (int i = 0; i < len; i++) {
				//-1是空的，去掉-1
				if (!"-1".equals(ruleIdsArr[i])) {
					EnterpriseIndexRelation relate = new EnterpriseIndexRelation();
					relate.setEnterpriseId(enterpriseId);
					relate.setRuleId(Integer.parseInt(ruleIdsArr[i]));
					relate.setIndexId(Integer.parseInt(indexIdsArr[i]));
					relate.setIndexName(nameArr[i]);
					relate.setIndexData(valueArr[i]);
					batchList.add(relate);
				}
			}
			//同时更新提交的审批
			if (StringUtils.isNotBlank(appNum)) {
				Approval approval = approvalMapper.selectByAppNo(appNum);
				if (null != approval) {
					approval.setIndexIds(indexIds);
					approval.setRuleIds(ruleIds);
					approvalMapper.updateByPrimaryKey(approval);
				}
			}
		}
		if ("updateReport".equals(updateReport)) {
			Map<String, String> map1 = api.selectEnterpriseIndexAndRules(enterpriseId);
			s1 = map1!=null?JsonUtils.toJson(map1):"";
		}
		//添加到数据库
		try{
			//更新时删除已有
			indexApi.deleteIndexRelation(enterpriseId);
			if (batchList != null && batchList.size()>0) {
				indexApi.batchInsertIndex(batchList);
			}
			resultMap.put("code", 200);
			//比较修改
			if ("updateReport".equals(updateReport)) {
				Map<String, String> map2 = api.selectEnterpriseIndexAndRules(enterpriseId);
				s2 = map2 != null ? JsonUtils.toJson(map2) : "";
				if (!s1.equals(s2)) {
					//api.updateEnterpriseEditFlag(enterpriseId, 1);
				}
			}
		}catch(Exception e){
			resultMap.put("code", 500);
			logger.error("批量添加企业指标失败!", e);
		}
		return resultMap;
	}

	/**
	 * 跳转企业创建查看页面
	 * @return
	 */
	@GetMapping("/detail")
	@Record(operationType="查询企业详细信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="查看详情")
	public String detail(HttpServletRequest request){
		//企业id
		String id = request.getParameter("id");
		if (!StringUtils.isEmpty(id)) {
			//企业主体信息
			Enterprise enterprise = new Enterprise();
			try {
				enterprise = api.findById(Integer.parseInt(id));
				if (enterprise != null){
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, api);
					request.setAttribute("enterprise", enterprise);
				}
			} catch (Exception e) {
				logger.error("企业主体信息查询失败", e);
			}
			request.setAttribute("enterpriseId", id);
		}
		request.setAttribute("method", "unSubmit");
		return "enterprise/enterpriseDetails";
	}
	
	/**
	 * 删除企业信息,报表信息，企业报表关联信息
	 * @param request
	 * @return map
	 */
	@ResponseBody
	@PostMapping("/delete")
	@Record(operationType="删除企业",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="数据录入")
	public Map<String, Object> delete(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//企业id
		String id = request.getParameter("id");
		if (!StringUtils.isEmpty(id)) {
			try {
				//逻辑删除：企业信息
				api.deleteById(Integer.parseInt(id));
				resultMap.put("code", 200);
			} catch (Exception e) {
				resultMap.put("code", 500);
				logger.error("删除企业信息,报表信息，企业报表关联信息id："+id+"失败！", e);
			}
		}
		return resultMap;
	}
	
	/**
	 * 更新企业主体信息
	 * @param request
	 * @return
	 */
	@GetMapping("/selectUpdate")
	@Record(operationType="跳转更新评级页面",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="更新评级")
    public String selectUpdate(HttpServletRequest request){
		//加入track跟踪评级标识
		request.setAttribute("track", "track");
		return "enterprise/selectEnterprise";
   }

	/**
	 * 更新企业主体信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@PostMapping("/findRatedEnterprise")
	@Record(operationType="更新评级",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="更新评级")
	public Map<String, Object> findRatedEnterprise(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		params.put("approvalStatus", 2);
		PageInfo<RiskCheckVO> ratedEnterprise = new PageInfo<>();
		try {
			ratedEnterprise = riskCheckApi.allSubject(params);
			resultMap.put("ratedEnterprise", ratedEnterprise);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("已评级企业主体查询失败", e);
		}
		return resultMap;
	}
	
}
