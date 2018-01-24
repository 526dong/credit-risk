package com.ccx.credit.risk.check;

import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.api.enterprise.EnterpriseIndustryApi;
import com.ccx.credit.risk.api.enterprise.RegionApi;
import com.ccx.credit.risk.api.index.IndexApi;
import com.ccx.credit.risk.api.riskcheck.RiskCheckApi;
import com.ccx.credit.risk.api.workflow.IWorkflowApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.mapper.enterprise.ReportMapper;
import com.ccx.credit.risk.mapper.rate.ApprovalMapper;
import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.Report;
import com.ccx.credit.risk.model.vo.ratingapply.HistoryApprovalVO;
import com.ccx.credit.risk.model.vo.ratingapply.ReportVO;
import com.ccx.credit.risk.model.vo.riskcheck.RiskCheckVO;
import com.ccx.credit.risk.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 评级申请审批
 * @author sgs
 * @date 2017/6/28
 */
@Controller
@RequestMapping(value="/riskCheck")
public class RiskCheckController extends BasicController{
	private static Logger logger = LogManager.getLogger(RiskCheckController.class);

	@Autowired
	private RiskCheckApi api;

	@Autowired
	private EnterpriseApi eApi;
	
	@Autowired
	private IWorkflowApi wfApi;
	
	/*地区字典*/
	@Autowired
	private RegionApi regionApi;
	
	/*行业字典*/
	@Autowired
	private EnterpriseIndustryApi industryApi;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;

	//不涉及事务，可注入使用
	@Autowired
	private IndexApi indexMapper;

	@Autowired
	private ApprovalMapper approvalMapper;

	@Autowired
	private ReportMapper reportMapper;

	@GetMapping("/list")
	public String list(HttpServletRequest request){
		//评级结果集合
		try {
			List rateResult = api.findRateResult();
			if (rateResult != null && rateResult.size() > 0) {
				request.setAttribute("rateResult",rateResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "riskcheck/riskCheckList";
	}
	
	//所有主体列表
	@PostMapping("/allSubject")
	@ResponseBody
	@Record(operationType="查询全部申请信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请审批")
	public Map<String, Object> allSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = api.allSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("所有主体列表信息查询失败",e);
		}

		return resultMap;
	}

	//所有主体列表
	@PostMapping("/allHistorySubject")
	@ResponseBody
	@Record(operationType="查询历史申请信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请审批")
	public Map<String, Object> allHistorySubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);

		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = api.historyAllSubject(params);
			for (RiskCheckVO vo :pages.getList()) {
				String ids = vo.getReportIds();
				List<Integer> list = new ArrayList<>();
				String[] reportIdArr = ids.split(",");
				for (String reportId : reportIdArr) {
					list.add(Integer.parseInt(reportId));
				}
				if (list.size() > 0) {
					List<Report> reportList = reportMapper.findReportListByIds(list);
					StringBuilder rateReport = new StringBuilder("");
					for (Report report : reportList) {
						rateReport.append(report.getReportTime());
						rateReport.append(report.getCal()==1?"合并":"母公司");
					}
					vo.setRateReport(rateReport.toString());
				}

			}
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("所有主体列表信息查询失败",e);
		}

		return resultMap;
	}

	//评级中主体列表
	@PostMapping("/pendingApprovalSubject")
	@ResponseBody
	@Record(operationType="查询待审核主体信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请审批")
	public Map<String, Object> pendingApprovalSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = api.allSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("评级中主体列表信息查询失败");
			e.printStackTrace();
		}

		return resultMap;
	}
	
	//已评级主体列表
	@PostMapping("/alreadyRatedSubject")
	@ResponseBody
	@Record(operationType="查询已评级主体信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请审批")
	public Map<String, Object> alreadyRatedSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = api.allSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("已评级主体列表信息查询失败");
			e.printStackTrace();
		}

		return resultMap;
	}
	
	//查看被退回主体列表开始
	@PostMapping("/beReturnedSubject")
	@ResponseBody
	@Record(operationType="查询被退回申请主体信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="评级申请审批")
	public Map<String, Object> beReturnedSubject(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<String, Object>();

		PageInfo<RiskCheckVO> pages = new PageInfo<RiskCheckVO>();
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		
		PageHelper.startPage(getPageNum(),getPageSize());

		try {
			pages = api.allSubject(params);
			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("被退回主体列表信息查询失败");
			e.printStackTrace();
		}

		return resultMap;
	}
	
	//查看已提交（评级中、已审批、被退回）主体；查看内容包括主体信息、报表信息
	@GetMapping("/commitSubjectDetail")
	@Record(operationType="查询历史审批详情",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="历史审批-查看详情")
	public  ModelAndView  commitSubjectDetail(HttpServletRequest request,
									  @RequestParam(required = true) String appId,
									  String method){
		ModelAndView mnv = new ModelAndView("ratingapply/commitSubjectDetail");
		//企业id
		int id = Integer.parseInt((String)request.getParameter("id"));
		//企业主体信息
		Enterprise enterprise = null;

		try {
			enterprise = eApi.findById(id);
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
	@Record(operationType="查询主体历史审批",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="历史审批总览")
	public String historyApproval(HttpServletRequest request){
		logger.debug("进入主体历史审批页面开始");
		
		//主体id
		Integer id = Integer.parseInt((String)request.getParameter("id"));
		//主体信息
		Enterprise enterprise = eApi.findById(id);
		request.setAttribute("enterpriseId", id);
		request.setAttribute("enterprise", enterprise);
		request.setAttribute("method", request.getParameter("method"));
		logger.debug("进入主体历史审批页面结束");
		return "ratingapply/historyApproval";
	}
	
//	@PostMapping("/getHistoryData")
//	@ResponseBody
//	public PageInfo<HistoryApprovalVO> getHistoryData(HttpServletRequest request){
//		logger.debug("查看已提交主体列表开始");
//
//		//Integer id=(Integer) request.getAttribute("enterpriseId");
//
//		//获取查询条件
//		Map<String,Object> params = ControllerUtil.requestMap(request);
//		//params.put("enterpriseId", id);
//
//		PageInfo<HistoryApprovalVO> pages = new PageInfo<HistoryApprovalVO>();
//        PageHelper.startPage(getPageNum(),getPageSize());
//        //获取历史报表List
//		pages = raApi.historyApproval(params);
//
//		logger.debug("查看已提交主体列表结束");
//
//		return pages;
//	}
	
	@GetMapping("/goApproval")
	@Record(operationType="跳转主体审批页面",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="审批")
	public String goApproval(HttpServletRequest request, @RequestParam(required = true) String appId){
		String approvalId=request.getParameter("approvalId");
		String actTaskId=request.getParameter("actTaskId");

		//企业id
		String enterpriseId = request.getParameter("id");

		if (StringUtils.isNotBlank(enterpriseId)) {
			//企业主体信息
			try {
				Enterprise enterprise = eApi.findById(Integer.parseInt(enterpriseId));
				List rateResult = api.findRateResult();
				List rateReason = api.findRateReason();


				request.setAttribute("rateResult",rateResult);
				request.setAttribute("rateReason",rateReason);


				if (enterprise != null) {
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, eApi);
					request.setAttribute("enterprise", enterprise);
					//查看行业指标
					Approval approval = approvalMapper.selectByAppNo(appId);
					request.setAttribute("approval", approval);
					logger.debug("进入主体审批页面结束");
				}
			} catch (Exception e) {
				logger.error("goApproval", e);
			}
		}

		return "riskcheck/approval";
	}
	
//	@PostMapping("/financialInformation")
//	@ResponseBody
//	public List<ReportVO> financialInformation(HttpServletRequest request){
//		logger.debug("查看主体对应的报表开始");
//
//		//审批id
//		Integer id = Integer.parseInt((String)request.getParameter("enterpriseId"));
//		//获取报表id，以数组形式存储
//		Integer [] reportIds=raApi.commitReportIds(id);
//		//获取报表List
//		List <ReportVO> reporList=raApi.commitReportList(reportIds);
//		logger.debug("查看主体对应的报表结束");
//		return reporList;
//	}
	
	@PostMapping("/ratingApproval")
	@ResponseBody
	@Record(operationType="进入评级审批页面",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="审批")
	public Map<String,Object> ratingApproval(HttpServletRequest request){
		logger.debug("进入评级审批页面");
		Map<String,Object> map = new HashMap<>();
		try {
			String degree = wfApi.saveSubmitTask(request);
			logger.debug("评级成功！");
			map.put("code", 200);
			map.put("degree", degree);
		}catch (Exception e){
			logger.error("评级失败！", e);
			if (e instanceof MyRuntimeException) {
				map.put("code", 400);
				map.put("msg", e.getMessage());
			} else if (e instanceof RateInValidRuntimeException) {
				map.put("code", 401);
				map.put("msg", e.getMessage());
			} else {
				map.put("code", 500);
			}

		}
		return map;
	}

	@PostMapping("/passApproval")
	@ResponseBody
	@Record(operationType="查看历史审批",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="审批")
	public Map<String,Object> passApproval(@RequestParam(required = true) Integer approvalId,
										   @RequestParam(required = true) String adDegree,
										   HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		try {
			wfApi.savePassApproval(approvalId, adDegree, request);
			map.put("code", 200);
		}catch (Exception e){
			logger.error("审批通过", e);
			map.put("code", 500);
		}
		return map;
	}
	
	@PostMapping("/ratingRefuse")
	@ResponseBody
	@Record(operationType="退回评级申请",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="审批")
	public  Map<String,Object> ratingRefuse(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//主体id
		try {
			wfApi.saverefuseSubmitTask(request);
			logger.debug("退回成功！");
			map.put("result", 1);
		}catch (Exception e){
			logger.error("退回失败！", e);
			map.put("result", 0);
			if (e instanceof MyRuntimeException) {
				map.put("msg", e.getMessage());
			} else {
				map.put("msg", "退回失败");
			}
		}
		
		return map;
	}
	
//	//已提交（评级中、已审批、被退回）主体列表
//	@PostMapping("/getHistoryData")
//	@ResponseBody
//	public PageInfo<HistoryApprovalVO> getHistoryData(HttpServletRequest request){
//		logger.debug("查看已提交主体列表开始");
//		
//		//Integer id=(Integer) request.getAttribute("enterpriseId");
//		
//		//获取查询条件
//		Map<String,Object> params = ControllerUtil.requestMap(request);
//		//params.put("enterpriseId", id);
//		
//		PageInfo<HistoryApprovalVO> pages = new PageInfo<HistoryApprovalVO>();
//        PageHelper.startPage(getPageNum(),getPageSize());
//        //获取历史报表List
//		pages = raApi.historyApproval(params);
//		
//		logger.debug("查看已提交主体列表结束");
//
//		return pages;
//	}
//	
//	//已提交（评级中、已审批、被退回）主体列表
//	@GetMapping("/goSubmitApproval")
//	public String goSubmitApproval(HttpServletRequest request){
//		logger.debug("进入评级申请页面");
//		
//		String id= request.getParameter("enterpriseId");
//		List<ReportVO> unRatingReport=api.unRatingReport(Integer.parseInt(id));
//		if(unRatingReport.size()>0){
//			for(int i=0;i<unRatingReport.size();i++){
//				unRatingReport.get(i).setReportOutline(unRatingReport.get(i).getReportTime()+unRatingReport.get(i).getCal()+unRatingReport.get(i).getCycle());
//			}
//		}
//		
//		request.setAttribute("enterpriseId", id);
//		request.setAttribute("unRatingReport", unRatingReport);
//		return "ratingapply/submitApproval";
//	}
	
	/**
	 * 任务管理首页显示
	 * @return
	 */
	@GetMapping("/task")
	@Record(operationType="确认，提交审批",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="审批")
	public String listTask(HttpServletRequest request){
		//使用当前用户名查询正在执行的任务表，获取当前任务的集合List<Task>
		List<Task> list = taskService.createTaskQuery()//
				.taskAssignee("approvalSubject")//指定个人任务查询
				.orderByTaskCreateTime().asc()//
				.list();
		//List<Task> list = workflowService.findTaskListByName(name); 
		request.setAttribute("list", list);
		return "riskcheck/task";
	}
	
	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 * @throws IOException 
	 */
	@GetMapping("/image")
	public void viewCurrentImage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//任务ID
		String taskId = request.getParameter("taskId");
		//使用任务ID，查询任务对象，获取流程流程实例ID
				Task task = taskService.createTaskQuery()//
								.taskId(taskId)//使用任务ID查询
								.singleResult();
				//获取流程实例ID
				String processInstanceId = task.getProcessInstanceId();
				
				ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
						.processInstanceId(processInstanceId)//使用流程实例ID查询
						.singleResult();
        if(pi==null){  
            System.out.println("流程已经结束");  
        }  
        else{  
            System.out.println("流程没有结束");  
        }  
		
		
		
		
		/**二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中*/
		Map<String, Object> map = wfApi.findCoordingByTask(taskId);
		request.setAttribute("acs", map);
		/**一：查看流程图*/
		//1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = wfApi.findProcessDefinitionByTaskId(taskId);
		//1：获取页面传递的部署对象ID和资源图片名称
		//部署对象ID
		String deploymentId = pd.getDeploymentId();
		//资源图片名称
		String imageName = pd.getDiagramResourceName();
		//2：获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
		InputStream in = wfApi.findImageInputStream(deploymentId,imageName);
		//3：从response对象获取输出流
		OutputStream out = response.getOutputStream();
		//4：将输入流中的数据读取出来，写到输出流中
		for(int b=-1;(b=in.read())!=-1;){
			out.write(b);
		}
		out.close();
		in.close();
		
		
	}
	
	/**
	 * 查看流程图
	 * @throws Exception 
	 */
//	public String viewImage() throws Exception{
//		//1：获取页面传递的部署对象ID和资源图片名称
//		//部署对象ID
//		String deploymentId = workflowBean.getDeploymentId();
//		//资源图片名称
//		String imageName = workflowBean.getImageName();
//		//2：获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
//		InputStream in = workflowService.findImageInputStream(deploymentId,imageName);
//		//3：从response对象获取输出流
//		OutputStream out = ServletActionContext.getResponse().getOutputStream();
//		//4：将输入流中的数据读取出来，写到输出流中
//		for(int b=-1;(b=in.read())!=-1;){
//			out.write(b);
//		}
//		out.close();
//		in.close();
//		//将图写到页面上，用输出流写
//		return null;
//	}

	//查找index rule
	private List<Map<String, String>> findIndexAndRule(Approval approval) {
		List<Map<String, Object>> paramList = new ArrayList<>();
		List<Map<String, String>> indexList = new ArrayList<>();
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
			indexList = indexMapper.findIndexAndRule(paramList);
		}
		return indexList;
	}
}
