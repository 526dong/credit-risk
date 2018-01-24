package com.ccx.credit.risk.ratingapply;

import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.api.enterprise.EnterpriseIndustryApi;
import com.ccx.credit.risk.api.riskcheck.RiskCheckApi;
import com.ccx.credit.risk.api.workflow.IWorkflowApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.mapper.enterprise.ReportMapper;
import com.ccx.credit.risk.mapper.index.IndexMapper;
import com.ccx.credit.risk.mapper.rate.ApprovalMapper;
import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.Report;
import com.ccx.credit.risk.model.vo.ratingapply.ReportVO;
import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;
import com.ccx.credit.risk.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评级申请提交
 * @author sgs
 * @date 2017/6/22
 */
@Controller
@RequestMapping(value="/ratingApply")
public class RatingApplyController extends BasicController{
	private static Logger logger = LogManager.getLogger(RatingApplyController.class);
	
	@Autowired
	private EnterpriseApi enterpriseApi;
	
	@Autowired
	private IWorkflowApi workflowApi;

	@Autowired
	private IndexMapper indexMapper;

	@Autowired
	private ApprovalMapper approvalMapper;

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private RiskCheckApi riskCheckApi;

	@Autowired
	private EnterpriseIndustryApi industryApi;

	/**
	 * 评级申请提交页面
	 * @param request
	 * @return
	 */
	@GetMapping("/list")
	public String list(HttpServletRequest request){
		//评级结果集合
		try {
			List rateResult = riskCheckApi.findRateResult();
			if (rateResult != null && rateResult.size() > 0) {
				request.setAttribute("rateResult",rateResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "ratingapply/ratingApplyList";
	}
	
	/**
	 * 未提交的企业主体信息
	 * @param request
	 * @return
	 */
	@PostMapping("/uncommitSubject")
	@ResponseBody
	@Record(operationType="查看未提交企业信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请提交")
	public Map<String, Object> uncommitSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		PageInfo<Enterprise> pages = new PageInfo<Enterprise>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = enterpriseApi.findAllUnCommit(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("未提交主体列表查询失败");
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	/**
	 * 已提交（评级中、已审批、被退回）主体列表
	 * @param request
	 * @return
	 */
	@PostMapping("/commitSubject")
	@ResponseBody
	@Record(operationType="查看已提交企业信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请提交")
	public Map<String, Object> commitSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		PageInfo<RiskCheckVO> pages = new PageInfo<>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = riskCheckApi.allSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("已提交主体列表查询失败");
			e.printStackTrace();
		}

		return resultMap;
	}
	
	/**
	 * 查看未提交
	 * @param request
	 * @return
	 */
	@GetMapping("/unCommitSubjectDetail")
	@Record(operationType="查看未提交企业详情",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="未提交—查看详情")
	public String unCommitSubjectDetail(HttpServletRequest request, String method){
		//企业id
		int id = Integer.parseInt((String)request.getParameter("id"));
		
		//企业主体信息
		Enterprise enterprise = new Enterprise();
		
		try {
			enterprise = enterpriseApi.findById(id);
			
			if (enterprise != null){
				//加载企业数据：指标规则
				EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseApi);
				request.setAttribute("enterprise", enterprise);
				request.setAttribute("method", method);
			}
		} catch (Exception e) {
			logger.error("企业主体查看未提交信息查询失败");
		}
		
		return "ratingapply/commitSubjectDetail";
	}
	
	/**
	 * 查看已提交（评级中、已审批、被退回）主体；查看内容包括主体信息、报表信息
	 * @param request
	 * @param appId
	 * @return
	 */
	@GetMapping("/commitSubjectDetail")
	@Record(operationType="查看已提交企业详情",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="已提交—查看详情")
	public ModelAndView commitSubjectDetail(HttpServletRequest request,
											@RequestParam(required = true) String appId,
											String method){
		ModelAndView mnv = new ModelAndView("ratingapply/commitSubjectDetail");
		//企业id
		int id = Integer.parseInt((String)request.getParameter("id"));
		//企业主体信息
		Enterprise enterprise = null;
		
		try {
			enterprise = enterpriseApi.findById(id);
			if (enterprise != null){
				mnv.addObject("enterprise", enterprise);
				mnv.addObject("method", method);

				Approval approval = approvalMapper.selectByAppNo(appId);
				if (null != approval) {
					//查看提交的指标信息
					List<Map<String, String>> indexRuleList = this.findIndexAndRule(approval);
					mnv.addObject("indexRuleList", indexRuleList);
					mnv.addObject("approval", approval);
				}
			}
		} catch (Exception e) {
			logger.error("commitSubjectDetail查询失败", e);
		}
		return mnv;
	}
	
	//查看主体历史审批；查看内容包括主体信息、报表信息
	@GetMapping("/historyApproval")
	@Record(operationType="查看企业历史审批",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="历史审批总览")
	public String historyApproval(HttpServletRequest request){
		//主体id
		String id = request.getParameter("id");
		//主体信息
		if (StringUtils.isNotBlank(id)) {
			Enterprise enterprise = new Enterprise();
			try {
				enterprise = enterpriseApi.findById(Integer.parseInt(id));
				
				if (enterprise != null){
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseApi);
					
					request.setAttribute("enterprise", enterprise);
					request.setAttribute("method", request.getParameter("method"));
				}
			} catch (Exception e) {
				logger.error("企业主体信息查询失败", e);
			}
			request.setAttribute("enterpriseId", id);
		}
		
		return "ratingapply/historyApproval";
	}

	//所有主体列表
	@PostMapping("/allHistorySubject")
	@ResponseBody
	public Map<String, Object> allHistorySubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);

		PageHelper.startPage(super.getPageNum(), super.getPageSize());

		try {
			pages = riskCheckApi.historyAllSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("所有主体列表信息查询失败", e);
		}

		return resultMap;
	}
	
//	@GetMapping("/approvalDetail")//(废弃)
//	public String approvalDetail(HttpServletRequest request){
//		logger.debug("查看审批详情开始");
//
//		//企业id
//		Integer id = Integer.parseInt((String)request.getParameter("id"));
//
//		//审批id
//		Integer approvalId = Integer.parseInt((String)request.getParameter("approvalId"));
//
//		//审批信息
//		RatingApplyVO approvalResult = applyApi.historyApprovalResult(approvalId);
//		//企业主体信息
//		Enterprise enterprise = enterpriseApi.findById(id);
//
//		//获取报表id，以数组形式存储
//		Integer [] reportIds = applyApi.historyReportIds(approvalId);
//		//获取报表List
//		List <ReportVO> reporList = applyApi.commitReportList(reportIds);
//
//		request.setAttribute("enterpriseId", id);
//		request.setAttribute("approvalResult", approvalResult);
//		request.setAttribute("enterpriseBean", enterprise);
//		request.setAttribute("reporList", reporList);
//		logger.debug("查看主体详情结束");
//		return "ratingapply/approvalDetail";
//	}
	
//	@GetMapping("/subjectReportDetail")//(废弃)
//	public String subjectReportDetail(HttpServletRequest request){
//		//主体id
//		String id = request.getParameter("id");
//		//主体信息
//		if (StringUtils.isNotBlank(id)) {
//			Enterprise enterprise = new Enterprise();
//			try {
//				enterprise = enterpriseApi.findById(Integer.parseInt(id));
//
//				if (enterprise != null){
//					//加载企业数据：指标规则
//					EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseApi);
//
//					request.setAttribute("enterprise", enterprise);
//				}
//			} catch (Exception e) {
//				logger.error("企业主体信息查询失败");
//				e.printStackTrace();
//			}
//
//			request.setAttribute("enterpriseId", id);
//
//			//审批信息
//			RatingApplyVO approvalResult = applyApi.approvalResult(Integer.parseInt(id));
//
//			request.setAttribute("approvalResult", approvalResult);
//		}
//
//		return "ratingapply/commitSubjectDetail";
//	}
	
	
	
	//已提交（评级中、已审批、被退回）主体列表
//	@GetMapping("/goSubmitApproval")
//	public String goSubmitApproval(HttpServletRequest request, String appNum){
//		logger.debug("进入评级申请页面");
//
//		String id = request.getParameter("enterpriseId");
//		List<ReportVO> unRatingReport = applyApi.unRatingReport(Integer.parseInt(id));
//		if(unRatingReport.size()>0){
//			for(int i=0;i<unRatingReport.size();i++){
//				unRatingReport.get(i).setReportOutline(unRatingReport.get(i).getReportTime()+unRatingReport.get(i).getCal()+unRatingReport.get(i).getCycle());
//			}
//		}
//
//		request.setAttribute("enterpriseId", id);
//		request.setAttribute("appNum", appNum);
//		request.setAttribute("unRatingReport", unRatingReport);
//		return "ratingapply/submitApproval";
//	}

	@PostMapping(value = "/queryApprovalReport", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Map<String, Object> queryApprovalReport(@RequestParam(required = true) Integer enterpriseId) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Report> listAll = new ArrayList<>();

		try {
			List<Report> list1 = reportMapper.findApprovalReportByEntId(enterpriseId, 0, null);
			List<Report> list2 = reportMapper.findApprovalReportByEntId(enterpriseId, 3, null);
			listAll.addAll(list1);
			listAll.addAll(list2);
			resultMap.put("list",  listAll);
			resultMap.put("code", 200);
		} catch (Exception e) {
			logger.error("查评级申请报告异常：", e);
			resultMap.put("code", 500);
		}
		return resultMap;
	}
	
	@GetMapping("/updateReport")
	public ModelAndView updateReport(HttpServletRequest request,
											@RequestParam(required = true) String appId,
											String method){
		ModelAndView mnv = new ModelAndView("enterprise/enterpriseUpdate");
		//企业id
		int id = Integer.parseInt((String)request.getParameter("id"));
		//企业主体信息
		Enterprise enterprise = null;

		//加载企业初始化数据
		EnterpriseDataUtils.loadInitData(request, enterpriseApi, industryApi);

		try {
			enterprise = enterpriseApi.findById(id);
			if (enterprise != null){
				mnv.addObject("enterprise", enterprise);
				mnv.addObject("method", method);

				//企业中的指标
				EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseApi);

				Approval approval = approvalMapper.selectByAppNo(appId);
				if (null != approval) {
					//查看提交的指标信息
					List<Map<String, String>> indexRuleList = this.findIndexAndRule(approval);
					mnv.addObject("indexRuleList", indexRuleList);
					mnv.addObject("approval", approval);
				}
			}
		} catch (Exception e) {
			logger.error("updateReport", e);
		}
		return mnv;
	}

//	@RequestMapping(value = "/saveIndexChange", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public Map<String, Object> saveIndexChange(HttpServletRequest request) {
//		Map<String, Object> resultMap = null;
//
//		try {
//			return enterpriseApi.saveIndexChange(request);
//		} catch (Exception e) {
//			logger.error("修改指标异常：", e);
//			resultMap = new HashMap<>();
//			resultMap.put("code", 500);
//		}
//
//		return resultMap;
//	}

//	@ResponseBody
//	@PostMapping("/approvalReportList")
//	public List<ReportVO> reportList(HttpServletRequest request){
//
//		//企业id
//		Integer id = Integer.parseInt((String)request.getParameter("enterpriseId"));
//
//		//获取报表id，以数组形式存储
//		Integer [] reportIds = applyApi.commitReportIds(id);
//
//		//获取报表List
//		List <ReportVO> reporList = applyApi.commitReportList(reportIds);
//
//		return reporList;
//	}
	
	
	@PostMapping("/submitApproval")
	@ResponseBody
	public Map<String,Object> submitApproval(HttpServletRequest request){
		logger.debug("进入评级申请页面");
		Map<String,Object> map = new HashMap<>(); 
		try {
			map = workflowApi.saveStartProcess(request);
			logger.debug("成功！");
			map.put("code", 200);
		}catch (Exception e){
			if (e instanceof MyRuntimeException) {
				map.put("code", 400);
				map.put("msg", e.getMessage());
			} else if (e instanceof RateInValidRuntimeException) {
				map.put("code", 401);
				map.put("msg", e.getMessage());
			} else {
				map.put("code", 500);
			}
			logger.error("失败！",e);
		}
		
		return map;
	}
	
	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 */
//	public String viewCurrentImage(){
//		//任务ID
//		String taskId = workflowBean.getTaskId();
//		/**一：查看流程图*/
//		//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
//		ProcessDefinition pd = workflowService.findProcessDefinitionByTaskId(taskId);
//		//workflowAction_viewImage?deploymentId=<s:property value='#deploymentId'/>&imageName=<s:property value='#imageName'/>
//		ValueContext.putValueContext("deploymentId", pd.getDeploymentId());
//		ValueContext.putValueContext("imageName", pd.getDiagramResourceName());
//		/**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
//		Map<String, Object> map = workflowService.findCoordingByTask(taskId);
//		ValueContext.putValueContext("acs", map);
//		return "image";
//	}

	//查找index rule
	private List<Map<String, String>> findIndexAndRule(Approval approval) {
		List<Map<String, Object>> paramList = new ArrayList<>();
		List<Map<String, String>> findIndexAndRule = new ArrayList<>();
		if (null != approval) {
			String indexIds = approval.getIndexIds();
			String ruleIds = approval.getRuleIds();

			if (StringUtils.isNotBlank(indexIds) && StringUtils.isNotBlank(ruleIds)) {
				String[] indexId = indexIds.split(",");
				String[] ruleId = ruleIds.split(",");
				for (int i = 0; i < indexId.length; i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("indexId", Integer.parseInt(indexId[i]));
					map.put("ruleId", Integer.parseInt(ruleId[i]));
					paramList.add(map);
				}
			}
		}
		if (paramList.size() > 0) {
			findIndexAndRule = indexMapper.findIndexAndRule(paramList);
		}
		return findIndexAndRule;
	}

}
