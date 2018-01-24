package com.ccx.credit.risk.service.impl.workflow;

import com.ccx.credit.risk.api.enterprise.ReportApi;
import com.ccx.credit.risk.api.workflow.IWorkflowApi;
import com.ccx.credit.risk.manager.report.CommonGainReportValue;
import com.ccx.credit.risk.manager.report.ComonReportPakage;
import com.ccx.credit.risk.mapper.element.ModelElementMapper;
import com.ccx.credit.risk.mapper.enterprise.*;
import com.ccx.credit.risk.mapper.index.IndexFormulaMapper;
import com.ccx.credit.risk.mapper.index.IndexMapper;
import com.ccx.credit.risk.mapper.index.IndexRuleMapper;
import com.ccx.credit.risk.mapper.rate.ApprovalMapper;
import com.ccx.credit.risk.mapper.rate.IndexModelMapper;
import com.ccx.credit.risk.mapper.rate.RateDataMapper;
import com.ccx.credit.risk.mapper.rate.RateResultMapper;
import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.element.ModelElement;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.Report;
import com.ccx.credit.risk.model.index.*;
import com.ccx.credit.risk.model.rate.RateData;
import com.ccx.credit.risk.model.rate.RateResult;
import com.ccx.credit.risk.util.*;
import com.ccx.credit.risk.utils.EnterpriseRatingUtils;
import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("workflowApi")
public class WorkflowServiceImpl implements IWorkflowApi {

	Logger rateLogger = LogManager.getLogger("myLog");
	@Autowired
	private EnterpriseMapper enterpriseMapper;
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;

	@Autowired
	private ApprovalMapper approvalMapper;

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private IndexMapper indexMapper;

	@Autowired
	private ModelElementMapper elementMapper;

	@Autowired
	private IndexRuleMapper ruleMapper;

	@Autowired
	private RateResultMapper resultMapper;

	@Autowired
	private EnterpriseIndustryMapper industryMapper;

	@Autowired
	private IndexFormulaMapper formulaMapper;

	@Autowired
	private IndexModelMapper indexModelMapper;

	@Autowired
	private RateDataMapper rateDataMapper;

	@Autowired
	private CommonGainReportValue commonGainReportValue;

	@Autowired
	private ReportApi reportApi;


	/**更新主体状态，启动流程实例，让启动的流程实例关联业务*/
	@Override
	public Map<String,Object> saveStartProcess(HttpServletRequest request) {
		Map<String,Object>  resultMap = new HashMap<>();

		//获取主体ID，使用主体ID，查询主体的对象Enterprise
		Integer enterpriseId =Integer.parseInt(request.getParameter("enterpriseId"));
		//获取前台传过来的reportIds
		String mergerReportIds = request.getParameter("mergerReportIds");
		String notMergerReportIds = request.getParameter("notMergerReportIds");
		//退回再提交的评级编号
		String appNum = request.getParameter("appNum");

		//设置流程定义的key（流程定义的key要与.bpmn文件中的id一致）
//		String key = "subjectApprovalProcess";
//
//		Map<String, Object> variables = new HashMap<String,Object>();
//		variables.put("approval", "approvalSubject");//所有的审批任务只有一个标识。
//		/**
//		 *(1)使用流程变量设置字符串（格式：subjectApprovalProcess.id的形式），通过设置，让启动的流程（流程实例）关联业务
//   		 *(2)使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
//		 */
//		//格式：EnterpriseId.id的形式（使用流程变量）
//		String objId = key+"."+enterpriseId;
//		variables.put("objId", objId);
//		//6：使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
//		ExecutionEntity processInstance = (ExecutionEntity) runtimeService.startProcessInstanceByKey(key,objId,variables);
//		List<TaskEntity> tasks = processInstance.getTasks();
//		String taskId = tasks.get(0).getId();


		//根据BusinessKey获取Execution对象(加入事务控制后上面的插入语句，下面无法查询到未提交的事务，因此改为上面的方式)
	    //Execution execution=runtimeService.createExecutionQuery().processInstanceBusinessKey(objId).list().get(0);
	    //根据Execution的id获取Task对象
		//Task task=taskService.createTaskQuery().executionId(execution.getId()).singleResult();

		Enterprise enterpriseBak = enterpriseMapper.findById(enterpriseId);
		if (null == enterpriseBak) {
			throw new MyRuntimeException("未找到主体信息！");
		}

		if (StringUtils.isBlank(mergerReportIds) && StringUtils.isBlank(notMergerReportIds)) {
			throw new MyRuntimeException("主体无法提交！");
		}

		//提交审批预评级
		if (StringUtils.isNotBlank(mergerReportIds)) {
			Approval approval = this.myGetApproval(enterpriseId, mergerReportIds, appNum, request, enterpriseBak);
			approval.setCal(1);
			this.myPreRate(approval, enterpriseBak, resultMap);
			approvalMapper.insert(approval);
			//approval ids(1,2,3)
			approval.setReportIds("("+approval.getReportIds()+")");
			enterpriseMapper.updateEnterpriseRe(approval);
		}
		if (StringUtils.isNotBlank(notMergerReportIds)) {
			Approval approval = this.myGetApproval(enterpriseId, notMergerReportIds, appNum, request, enterpriseBak);
			approval.setCal(0);
			this.myPreRate(approval, enterpriseBak, resultMap);
			approvalMapper.insert(approval);
			//approval ids(1,2,3)
			approval.setReportIds("("+approval.getReportIds()+")");
			enterpriseMapper.updateEnterpriseRe(approval);

		}

		//更新主体提交状态
		List<Report> list1 = reportMapper.findApprovalReportByEntId(enterpriseId, 0, null);
		List<Report> list2 = reportMapper.findApprovalReportByEntId(enterpriseId, 3, null);

		if (0 == list1.size() && 0 == list2.size()) {
			enterpriseBak.setApprovalState(1);
		}
		enterpriseMapper.updateApprovalStatus(enterpriseBak);

		resultMap.put("name", enterpriseBak.getName());
		resultMap.put("creditCode", enterpriseBak.getCreditCode());
		resultMap.put("scale", enterpriseBak.getScale());

		return resultMap;
	}

	//生产Approval
	private Approval myGetApproval(Integer enterpriseId, String reportIds, String appNum, HttpServletRequest request, Enterprise enterpriseBak) {
		Approval approval = new Approval();

		//向审批表新增一条数据
		approval.setEnterpriseId(enterpriseId);
		approval.setReportIds(reportIds);
		//评级申请编号规则为 MMdd+6位随机数
		String ratingApplyNum = new SimpleDateFormat("MMdd").format(new Date())+(int)((Math.random()*9+1)*100000);
		approval.setRatingApplyNum(ratingApplyNum);
		enterpriseBak.setRatingApplyNum(ratingApplyNum);
		approval.setApprovalStatus(1);
		//把activi的taskId插入到审批表中，以便以后审批的时候用到
		//approval.setActTaskId(taskId);
		approval.setInitiator((ControllerUtil.getSessionUser(request).getLoginName()));
		approval.setEnterpriseJson(JsonUtils.toJson(enterpriseBak));
		approval.setRatingType(enterpriseBak.getType());
		approval.setInitiateTime(new Date());

		if (StringUtils.isNotBlank(appNum)) {
			//把退回修改的指标保存到提交的任务中
			Approval approvalBak = approvalMapper.selectByAppNo(appNum);
			approval.setIndexIds(approvalBak.getIndexIds());
			approval.setRuleIds(approvalBak.getRuleIds());
			approvalMapper.updateApprovalStatus(appNum, 4);
		} else {
			//把评级指标保存到提交的任务中用于计算
			Map<String, String> indexMap = enterpriseMapper.selectEnterpriseIndexAndRules(enterpriseId);
			if (null == indexMap) {
				//throw new MyRuntimeException("主体尚未管理指标信息！");
				approval.setApprovalIndexNameAndValueJson("[]");
			} else {
				if (StringUtils.isNotBlank(indexMap.get("indexIds"))
						&& StringUtils.isNotBlank(indexMap.get("ruleIds"))
						&& StringUtils.isNotBlank(indexMap.get("names"))
						&& StringUtils.isNotBlank(indexMap.get("valuesa"))) {
					String indexIds = indexMap.get("indexIds");
					String ruleIds = indexMap.get("ruleIds");
					String names = indexMap.get("names");
					String valus = indexMap.get("valuesa");
					approval.setIndexIds(indexIds);
					approval.setRuleIds(ruleIds);

					//保存提交的指标
					String jsonBuid = "";
					String[] idArr = indexIds.split(",");
					String[] ruleIdArr = ruleIds.split(",");
					String[] nameArr = names.split(",");
					String[] valueArr = valus.split(",");
					for (int i = 0; i < ruleIdArr.length; i++) {
						String id = idArr[i];
						String ruleId = ruleIdArr[i];
						String name = nameArr[i];
						String value = valueArr[i];
						if (i ==0) {
							jsonBuid += "{\"id\":"+id+", \"name\":\""+name+"\", \"value\":\""+value+"\"}";
						} else {
							jsonBuid += ", {\"id\":"+id+", \"name\":\""+name+"\", \"value\":\""+value+"\"}";
						}
					}
					approval.setApprovalIndexNameAndValueJson("["+jsonBuid+"]");
				} else {
					//throw new MyRuntimeException("主体中还未录入指标信息，不能申请提交");
					approval.setApprovalIndexNameAndValueJson("[]");
				}
			}
		}

		return approval;
	}

	//
	private void myPreRate(Approval approval, Enterprise enterpriseBak, Map<String, Object> resultMap) {
		//评分模型
		IndexModel model = this.findRateMode(approval, enterpriseBak.getIndustry2(), new Integer(enterpriseBak.getScale()), 0);
		//v1,2,3_报表名称和字段的map
		Map<String, Object> reportSheetMap = new HashMap<>();
		//查需要评级的报告
		int len = this.findAllReport(model.getReportTypeId(), approval, reportSheetMap, 0);
		//提交的指标and规则map
		Map<String, String> indexRuleMap = this.findRateIndexRuleMap(approval);
		//因素list
		List<ModelElement> elementList = this.findRateElementList(model.getId());
		//公式map
		Map<String, String> formulaMap = this.findRateFormulaMap();
		//保存评级
		String degree = this.doRate(len, null, enterpriseBak.getId(), model, indexRuleMap, elementList, reportSheetMap, formulaMap, 0);
		if (1 == approval.getCal()) {
			resultMap.put("mergerDegree", degree);
		} else {
			resultMap.put("notMergerDegree", degree);
		}
		approval.setPreRatingResult(degree);
		approval.setModelId(model.getId());
	}

	@Override
	public String saveSubmitTask(HttpServletRequest request) {
		//获取任务ID
		//String taskId = request.getParameter("actTaskId");
		//主体id
		Integer enterpriseId = Integer.parseInt(request.getParameter("enterpriseId"));
		//主体的审批id
		Integer approvalId = Integer.parseInt(request.getParameter("approvalId"));
		//
		Integer industryId2 = Integer.parseInt(request.getParameter("industryId2"));
		Integer type = Integer.parseInt(request.getParameter("entType"));
		//评级类型
		String rateType = request.getParameter("type");

		//查提交审批记录
		Approval approval = approvalMapper.selectByPrimaryKey(approvalId);
		if (null == approval) {
			throw new MyRuntimeException("未查询到审批记录！");
		}
		if (2 == approval.getApprovalStatus() || 3 == approval.getApprovalStatus()) {
			throw new MyRuntimeException("当前审批已完成，请不要重复提交！");
		}

		//更新提交数据
		prepareRate(request, approval, 1);

		String rateNo = approval.getRatingApplyNum();
		//v1,2,3_报表名称和字段的map
		Map<String, Object> reportSheetMap = new HashMap<>();
		//评分模型
		IndexModel model = this.findRateMode(approval, industryId2, type, 1);
		//查需要评级的报告
		int len = this.findAllReport(model.getReportTypeId(), approval, reportSheetMap, 1);
		//提交的指标and规则map用户作答
		Map<String, String> indexRuleMap = this.findRateIndexRuleMap(approval);

		//因素list
		List<ModelElement> elementList = this.findRateElementList(model.getId());
		//公式map
		Map<String, String> formulaMap = this.findRateFormulaMap();
		//保存评级
		String degree = this.doRate(len, rateNo, enterpriseId, model, indexRuleMap, elementList, reportSheetMap, formulaMap, Integer.parseInt(rateType));
		try {
			//影子评级调整
			this.doSaveShadowRate(approvalId, enterpriseId, degree);
		} catch (Exception e) {
			throw new RateInValidRuntimeException("评级数据库有问题");
		}


		/*****************************************************************************/
		/**
		 * 在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
		 */
		//使用任务ID，查询任务对象，获取流程流程实例ID
//		Task task = taskService.createTaskQuery()//
//						.taskId(taskId)//使用任务ID查询
//						.singleResult();
//		//获取流程实例ID
//		String processInstanceId = task.getProcessInstanceId();
//
//		taskService.addComment(taskId, processInstanceId, "任务结束");
//
//		Map<String, Object> variables = new HashMap<String,Object>();
//		variables.put("taskEnd", "taskEnd");
//
//		//使用任务ID，完成任务，同时流程变量
//		taskService.complete(taskId, variables);
		
		/**
		 * 在完成任务之后，判断流程是否结束
   		 * 如果流程结束了，更新主体表的状态从1变成2审核完成（已评级）并更新审批表的状态从1变成2
		 */
//		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
//						.processInstanceId(processInstanceId)
//						.singleResult();
//		//流程结束了
//		if(pi==null){
		if ("1".equals(rateType)) {
			approval.setRatingResult(degree);
			this.myUpdateApproval(approval, request);
		}
//		}

		return degree;
	}

	@Override
	public void saverefuseSubmitTask(HttpServletRequest request) {
		//获取任务ID
		//String taskId = request.getParameter("actTaskId");
		//主体id
		Integer enterpriseId = Integer.parseInt(request.getParameter("enterpriseId"));
		//主体的审批id
		Integer approvalId = Integer.parseInt(request.getParameter("approvalId"));

		//查提交审批记录
		Approval approval = approvalMapper.selectByPrimaryKey(approvalId);
		if (null== approval) {
			throw new MyRuntimeException("未查询到提交评级的报告记录！");
		}
		//更新提交数据
		prepareRate(request, approval, 0);

		int hcode = reportApi.calReportHashCode(approval.getReportIds());
		approval.setReportHashCode(hcode);

		/***********************************************************************************/
		/**
		 * 1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
		 */
		//使用任务ID，查询任务对象，获取流程流程实例ID
//		Task task = taskService.createTaskQuery()//
//						.taskId(taskId)//使用任务ID查询
//						.singleResult();
//		//获取流程实例ID
//		String processInstanceId = task.getProcessInstanceId();
//
//		taskService.addComment(taskId, processInstanceId, "任务结束");
//		Map<String, Object> variables = new HashMap<String,Object>();
//		variables.put("taskEnd", "taskEnd");
//
//		//使用任务ID，完成任务，同时流程变量
//		taskService.complete(taskId, variables);

		/**
		 * 在完成任务之后，判断流程是否结束
   		 * 如果流程结束了，更新主体表的状态从1变成3审核完成（拒绝）并更新审批表的状态从1变成3
		 */
//		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
//				.processInstanceId(processInstanceId)
//				.singleResult();
//			//流程结束了
//			if(pi==null){
		Enterprise enterprise = new Enterprise();
		//Approval approval = new Approval();
		enterprise.setId(enterpriseId);
		enterprise.setRefuseFlag(1);
		enterpriseMapper.updateRefuseFlag(enterprise);
		approval.setId(approvalId);
		approval.setApprovalStatus(3);
		approval.setApprovalTime(new Date());
		approval.setApprover((ControllerUtil.getSessionUser(request).getLoginName()));
		approval.setRefuseReason(request.getParameter("refuseReason"));
		approval.setActTaskId("");
		approvalMapper.updateByPrimaryKeySelective(approval);
		approval.setReportIds("("+approval.getReportIds()+")");
		enterpriseMapper.updateEnterpriseRe(approval);
//		}
	}

	/**1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象*/
	@Override
	public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义ID
		String processDefinitionId = task.getProcessDefinitionId();
		//查询流程定义的对象
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()//创建流程定义查询对象，对应表act_re_procdef 
					.processDefinitionId(processDefinitionId)//使用流程定义ID查询
					.singleResult();
		return pd;
	}
	
	/**使用部署对象ID和资源图片名称，获取图片的输入流*/
	@Override
	public InputStream findImageInputStream(String deploymentId,
			String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}

	@Override
	public void savePassApproval(Integer approvalId, String adDegree, HttpServletRequest request) {
		Integer enterpriseId = Integer.parseInt(request.getParameter("enterpriseId"));
		Approval approval = new Approval();
		approval.setId(approvalId);
		approval.setRatingResult(adDegree);

		try {
			//影子评级调整
			this.doSaveShadowRate(approvalId, enterpriseId, adDegree);
		} catch (Exception e) {
			rateLogger.info(e);
			throw new RateInValidRuntimeException("评级数据库有问题");
		}
		this.myUpdateApproval(approval, request);
	}

	@Override
	public Map<String, Object> findCoordingByTask(String taskId) {
		//存放坐标
		Map<String, Object> map = new HashMap<String,Object>();
		//使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
					.taskId(taskId)//使用任务ID查询
					.singleResult();
		//获取流程定义的ID
		String processDefinitionId = task.getProcessDefinitionId();
		//获取流程定义的实体对象（对应.bpmn文件中的数据）
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinitionId);
		//流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//使用流程实例ID，查询正在执行的执行对象表，获取当前活动对应的流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//创建流程实例查询
					.processInstanceId(processInstanceId)//使用流程实例ID查询
					.singleResult();
		//获取当前活动的ID
		String activityId = pi.getActivityId();
		//获取当前活动对象
		ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);//活动ID
		//获取坐标
		map.put("x", activityImpl.getX());
		map.put("y", activityImpl.getY());
		map.put("width", activityImpl.getWidth());
		map.put("height", activityImpl.getHeight());
		return map;
	}

	//预备评级
	private void prepareRate(HttpServletRequest request, Approval approval, int rateFlag) {
		String indexIds = request.getParameter("indexIds");
		String ruleIds = request.getParameter("ruleIds");
		String adjustIndexIds = request.getParameter("adjustIndexIds");
		String adjustRuleContent = request.getParameter("adjustRuleContent");
		String adjustChange = request.getParameter("adjustChange");
		String indexNameAndValue = request.getParameter("approvalIndexNameAndValue");
		StringBuilder adjustContext = new StringBuilder();
		StringBuilder changeContext = new StringBuilder();

		//调整的指标理由
		if (StringUtils.isNotBlank(adjustIndexIds) || StringUtils.isNotBlank(adjustRuleContent)) {
			String[]  adjustIndexId = adjustIndexIds.split(",");
			String[]  content = adjustRuleContent.split(",");
			String[]  change = adjustChange.split(",");
			for (int i=0; i<content.length; i++) {
				if (i == 0) {
					adjustContext.append(adjustIndexId[i]+":"+content[i]);
					changeContext.append(adjustIndexId[i]+":"+change[i]);
				} else {
					adjustContext.append(","+adjustIndexId[i]+":"+content[i]);
					changeContext.append(","+adjustIndexId[i]+":"+change[i]);
				}

			}
			approval.setAdjustContent(adjustContext.toString());
			approval.setAdjustChange(changeContext.toString());
		}

		//保存提交的指标
		if (StringUtils.isNotBlank(indexIds) && StringUtils.isNotBlank(ruleIds)) {
			approval.setIndexIds(indexIds);
			approval.setRuleIds(ruleIds);
			approval.setApprovalIndexNameAndValueJson(indexNameAndValue);
			approvalMapper.updateByPrimaryKey(approval);
		} else {
			if (1 == rateFlag) {
				throw new MyRuntimeException("定性指标数据不存在！");
			}
		}
	}

	//找到需要评级的报告，提交的报告不能全部评级
	private int findAllReport(Integer reportTypeId, Approval approval, Map<String, Object> reportSheetMap, int preOrRate) {
		/*if (null == reportTypeId) {
			throw new RateInValidRuntimeException("评分卡还未匹配报表类型，无法评级");
		}*/

		StringBuilder rateReportIds = new StringBuilder();
		//未筛选的报告列表
		List<Report> reportList = null;

		//查报告
		if (0 == preOrRate) {
			//查全部报告
			reportList = reportMapper.findApprovalReportByEntId(approval.getEnterpriseId(), null, approval.getCal());
		} else {
			//查审批的报告
			List<Integer> idList = new ArrayList<>();
			String[] idArray = approval.getRateReportIds().split(",");
			for (String id: idArray) {
				if (StringUtils.isNotBlank(id)) {
					idList.add(Integer.parseInt(id));
				}
			}
			if (idList.size()>0) {
				reportList = reportMapper.findReportListByIds(idList);
			}
		}

		if (null == reportList || reportList.size() == 0) {
			throw new MyRuntimeException("未查询到需要评级的报告！");
		}

		//符合要求的报表有几年
		int n = 0;
		//年份
		int reportYearLast = Integer.parseInt(reportList.get(0).getReportTime()) - 1;
		for (int i = 0; i < reportList.size(); i++) {
			Report report = reportList.get(i);
			Integer reportId = report.getId();

			if (0 ==preOrRate) {
				String reportYear = report.getReportTime();
				int thisYear = Integer.parseInt(reportYear);
				int thisTypeId = report.getType();

				if (thisYear - reportYearLast != 1 || reportTypeId != thisTypeId || ++n > 3) {
					//取最近连续的，模板和评分卡相同的报告，最多三年
					break;
				}
				reportYearLast = thisYear;

				if (0 == i) {
					//记录下需要真正评级的报告
					approval.setRateReport(reportYear+(approval.getCal()==1?"合并":"母公司"));
					rateReportIds.append(reportId);
				} else {
					rateReportIds.append(",").append(reportId);
				}
			} else {
				n++;
			}

			ComonReportPakage.BaseValues baseValue = commonGainReportValue.getBaseValue(reportId);
			for (ComonReportPakage.Values values: baseValue.getValues()) {
				String sheetName = values.getSheetName();
				//字段和值map
				Map<String, String> reportColumnMap = new HashMap<>();
				List<ComonReportPakage.ValueField> fields = values.getFields();

				if (null == fields) {
					continue;
				}

				for (ComonReportPakage.ValueField field : fields) {
					String columnName = field.getModleField().getName();

					//字段begin_total的map
					reportColumnMap.put("上期_"+columnName, field.getBeginBalance());
					reportColumnMap.put("本期_"+columnName, field.getEndBalance());
				}
				//报表v1_name和字段map
				reportSheetMap.put(EnterpriseRatingUtils.yearArry[i]+"_"+sheetName, reportColumnMap);
			}
		}

		if (0 == preOrRate) {
			approval.setRateReportIds(rateReportIds.toString());
		}
		if (0 == n) {
			throw new MyRuntimeException("当前提交的报表类型已过期，请更新报表中的类型");
		}

		return n;
	}

	//找到最终需要评级的报告(废弃)
//	private int findTFinalNeedReport(	List<Report> reportList,
//										 Map<String, String> sortContentMap,
//										 Map<String, String> yearVersionMap,
//										 Map<String, Object> reportMap) {
//		int yearlen = 0;
//		Integer yearV1 = null;
//		Integer yearV2 = null;
//		Integer yearV3 = null;
//
//		//上传多张报告
//		HashMap<String, Object> mergerMapV1 = new HashMap<>();
//		HashMap<String, Object> notMergerV1 = new HashMap<>();
//		HashMap<String, Object> mergerMapV2 = new HashMap<>();
//		HashMap<String, Object> notMergerV2 = new HashMap<>();
//		HashMap<String, Object> mergerMapV3 = new HashMap<>();
//		HashMap<String, Object> notMergerV3 = new HashMap<>();
//
//		//
//		Iterator<String> iterator = sortContentMap.keySet().iterator();
//		if (iterator.hasNext()) {
//			yearV1 = Integer.parseInt(iterator.next());
//		}
//		if (iterator.hasNext()) {
//			yearV2 = Integer.parseInt(iterator.next());
//		}
//		if (iterator.hasNext()) {
//			yearV3 = Integer.parseInt(iterator.next());
//		}
//
//		//进行countMap
//		for (Report report : reportList) {
//			Integer cal = report.getCal();
//			if (1 == cal && "V1".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!mergerMapV1.containsKey("merger")) {
//					mergerMapV1.put("merger", report);
//				} else {
//					Report reportQuery = (Report) mergerMapV1.get("merger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						mergerMapV1.put("merger", report);
//					}
//				}
//			} else if (1 == cal && "V2".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!mergerMapV2.containsKey("merger")) {
//					mergerMapV2.put("merger", report);
//				} else {
//					Report reportQuery = (Report) mergerMapV2.get("merger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						mergerMapV2.put("merger", report);
//					}
//				}
//			}else if (1 == cal && "V3".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!mergerMapV3.containsKey("merger")) {
//					mergerMapV3.put("merger", report);
//				} else {
//					Report reportQuery = (Report) mergerMapV3.get("merger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						mergerMapV3.put("merger", report);
//					}
//				}
//			}  else if (0 == cal && "V1".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!notMergerV1.containsKey("notMerger")) {
//					notMergerV1.put("notMerger", report);
//				} else {
//					Report reportQuery = (Report) notMergerV1.get("notMerger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						notMergerV1.put("notMerger", report);
//					}
//				}
//			} else if (0 == cal && "V2".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!notMergerV2.containsKey("notMerger")) {
//					notMergerV2.put("notMerger", report);
//				} else {
//					Report reportQuery = (Report) notMergerV2.get("notMerger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						notMergerV2.put("notMerger", report);
//					}
//				}
//			} else if (0 == cal && "V3".equals(yearVersionMap.get( report.getReportTime()))) {
//				if (!notMergerV3.containsKey("notMerger")) {
//					notMergerV3.put("notMerger", report);
//				} else {
//					Report reportQuery = (Report) notMergerV3.get("notMerger");
//					if (report.getCreateDayeTypeDate().getTime() > reportQuery.getCreateDayeTypeDate().getTime()) {
//						notMergerV3.put("notMerger", report);
//					}
//				}
//			}
//
//		}
//
//		Report reportQueryV1 = null;
//		Integer queryIdV1 = null;
//		Report reportQueryV2 = null;
//		Integer queryIdV2 = null;
//		Report reportQueryV3 = null;
//		Integer queryIdV3 = null;
//		List<Integer> AssetIdList = new ArrayList<>();
//		List<Integer> CashFlowIdList = new ArrayList<>();
//		List<Integer> AdditionalCashFlowIdList = new ArrayList<>();
//		List<Integer> LiabilitiesEquitesIdList = new ArrayList<>();
//		List<Integer> NotesFinancialStatemenIdList = new ArrayList<>();
//		List<Integer> reportProfitLossIdList = new ArrayList<>();
//
//		//判断是最新年否有2个口径
//		if (null != mergerMapV1.get("merger") && null != notMergerV1.get("notMerger")) {
//			//有2个口径，先取第一年合并
//			yearlen = 1;
//			reportQueryV1 = (Report) mergerMapV1.get("merger");
//			queryIdV1 = reportQueryV1.getId();
//			rateLogger.info("1评级的报告： 最新年合并");
//			//有2个口径
//			if (null != mergerMapV2.get("merger") && 1 == Math.abs(yearV1.intValue() - yearV2.intValue())) {
//				//次年也有合并，取2年合并
//				yearlen = 2;
//				reportQueryV2 = (Report) mergerMapV2.get("merger");
//				queryIdV2 = reportQueryV2.getId();
//				rateLogger.info("2评级的报告： 次最新年合并");
//
//				if (null != mergerMapV3.get("merger") && 1 == Math.abs(yearV2.intValue() - yearV3.intValue())) {
//					yearlen = 3;
//					reportQueryV3 = (Report) mergerMapV3.get("merger");
//					queryIdV3 = reportQueryV3.getId();
//					rateLogger.info("3评级的报告： 次新年合并");
//
//				}
//			}
//		} else {
//			//最新报告只有一个口径
//			if (null != mergerMapV1.get("merger") && null != mergerMapV2.get("merger")) {
//				//先取1年合并口径
//				yearlen = 1;
//				reportQueryV1 = (Report) mergerMapV1.get("merger");
//				queryIdV1 = reportQueryV1.getId();
//				rateLogger.info("1评级的报告： 最新年合并");
//
//				if (1 == Math.abs(yearV1.intValue() - yearV2.intValue())) {
//					//合并口径相同，取2年合并口径
//					yearlen = 2;
//					reportQueryV2 = (Report) mergerMapV2.get("merger");
//					queryIdV2 = reportQueryV2.getId();
//					rateLogger.info("2评级的报告： 次最新年合并");
//
//					if (null != mergerMapV3.get("merger") && 1 == Math.abs(yearV2.intValue() - yearV3.intValue())) {
//						//第三年有并且连续
//						yearlen = 3;
//						reportQueryV3 = (Report) mergerMapV3.get("merger");
//						queryIdV3 = reportQueryV3.getId();
//						rateLogger.info("3评级的报告： 次新年合并");
//					}
//				}
//			} else if (null != notMergerV1.get("notMerger") && null != notMergerV2.get("notMerger")) {
//				//先取1年非合并口径
//				yearlen = 1;
//				reportQueryV1 = (Report) notMergerV1.get("notMerger");
//				queryIdV1 = reportQueryV1.getId();
//				rateLogger.info("1评级的报告： 最新年非合并");
//
//				if (1 == Math.abs(yearV1.intValue() - yearV2.intValue())) {
//					//非合并口径相同，取2年非合并口径
//					yearlen = 2;
//					reportQueryV2 = (Report) notMergerV2.get("notMerger");
//					queryIdV2 = reportQueryV2.getId();
//					rateLogger.info("2评级的报告： 次最新年非合并");
//
//					if (null != notMergerV3.get("notMerger") && 1 == Math.abs(yearV2.intValue() - yearV3.intValue())) {
//						yearlen = 3;
//						reportQueryV3 = (Report) notMergerV3.get("notMerger");
//						queryIdV3 = reportQueryV3.getId();
//						rateLogger.info("3评级的报告： 次新年非合并");
//					}
//
//				}
//
//			} else {
//				//口径不相同取最新的1年报告
//				yearlen = 1;
//				if (null != mergerMapV1.get("merger")) {
//					reportQueryV1 = (Report) mergerMapV1.get("merger");
//					queryIdV1 = reportQueryV1.getId();
//					rateLogger.info("1评级的报告： 最新年合并");
//				} else if ( null != notMergerV1.get("notMerger")) {
//					reportQueryV1 = (Report) notMergerV1.get("notMerger");
//					queryIdV1 = reportQueryV1.getId();
//					rateLogger.info("1评级的报告： 最新年非合并");
//				}
//			}
//		}
//
//		AssetIdList.add(queryIdV1);
//		CashFlowIdList.add(queryIdV1);
//		AdditionalCashFlowIdList.add(queryIdV1);
//		LiabilitiesEquitesIdList.add(queryIdV1);
//		NotesFinancialStatemenIdList.add(queryIdV1);
//		reportProfitLossIdList.add(queryIdV1);
//		if (null != queryIdV2) {
//			AssetIdList.add(queryIdV2);
//			CashFlowIdList.add(queryIdV2);
//			AdditionalCashFlowIdList.add(queryIdV2);
//			LiabilitiesEquitesIdList.add(queryIdV2);
//			NotesFinancialStatemenIdList.add(queryIdV2);
//			reportProfitLossIdList.add(queryIdV2);
//		}
//		if (null != queryIdV3) {
//			AssetIdList.add(queryIdV3);
//			CashFlowIdList.add(queryIdV3);
//			AdditionalCashFlowIdList.add(queryIdV3);
//			LiabilitiesEquitesIdList.add(queryIdV3);
//			NotesFinancialStatemenIdList.add(queryIdV3);
//			reportProfitLossIdList.add(queryIdV3);
//		}
//
//		reportMap.put("ASSETS", reportAssetMapper.findByReportIds(AssetIdList));
//		reportMap.put("LIABILITIES_EQUITIES", reportLiabilitiesEquitesMapper.findByReportIds(LiabilitiesEquitesIdList));
//		reportMap.put("PROFIT_LOSS", reportProfitLossMapper.findByReportIds(reportProfitLossIdList));
//		reportMap.put("CASH_FLOW", reportCashFlowMapper.findByReportIds(CashFlowIdList));
//		reportMap.put("ADDITIONAL_CASH_FLOW", reportAdditionalCashFlowMapper.findByReportIds(AdditionalCashFlowIdList));
//		reportMap.put("NOTES_FINANCIAL_STATEMENT", reportNotesFinancialStatementMapper.findByReportIds(NotesFinancialStatemenIdList));
//
//		return yearlen;
//	}


	//查评分卡
	private IndexModel findRateMode(Approval approval, Integer industryId2, Integer type, int preOrRate) {
		Integer modelId = industryMapper.getModelIdByIdAndEntType(industryId2, type);;


		if (null == modelId) {
			//当前后台未匹配评分卡
			if (0 == preOrRate) {
				//预评级
				throw new RateInValidRuntimeException("未发现和行业相匹配的可用评分卡！");
			} else {
				//已提交
				modelId= approval.getModelId();
				IndexModel rateModel =indexModelMapper.selectByPrimaryKey(modelId);
				if (null == rateModel) {
					throw new RateInValidRuntimeException("评分卡不存在！");
				}
				return rateModel;
			}
		} else {
			//后台匹配了新评分卡
			IndexModel currentModel = indexModelMapper.selectByPrimaryKey(modelId);
			IndexModel rateModel = indexModelMapper.selectByPrimaryKey(approval.getModelId());

			if (0 == preOrRate) {
				//预评级
				if (null == currentModel) {
					throw new RateInValidRuntimeException("评分卡不存在！");
				}
				return currentModel;
			} else {
				//已提交
				if (null == rateModel) {
					throw new RateInValidRuntimeException("评分卡不存在！");
				}
				if (currentModel.getReportTypeId().equals(rateModel.getReportTypeId())){
					//模型还是当前类型
					return currentModel;
				} else {
					//模型变化
					return rateModel;
				}
			}
		}
	}

	//查评级的因素List
	private List<ModelElement> findRateElementList(Integer modelId) {
		//查因素
		List<ModelElement>  elementList = elementMapper.getListByModelId(modelId);
		if (elementList.size() == 0) {
			throw new RateInValidRuntimeException("未发现和行业相匹可用因素！");
		}
		return elementList;
	}

	//查评级的indexList
	private List<IndexBean> findRateIndexList(Integer eleId) {

		//查指标列表
		List<IndexBean> indexList = indexMapper.findIndexWithoutRuleByElementId(eleId);
		/*if (indexList.size() == 0) {
			throw new MyRuntimeException("");
		}*/
		return indexList;
	}

	private List<IndexBean> findRateNatureIndexList(Integer eleId) {

		//查指标列表
		List<IndexBean> indexList = indexMapper.findNatureIndexWithoutRuleByElementId(eleId);
		/*if (indexList.size() == 0) {
			throw new MyRuntimeException("");
		}*/
		return indexList;
	}

	//查提交评级的指标答案
	private Map<String, String> findRateIndexRuleMap(Approval approval) {

		//查提交的定性指标和提交的指标答案
		String indexIds =  approval.getIndexIds();
		String ruleIds = approval.getRuleIds();
		Map<String, String> indexRuleMap = new HashMap<>();
		if (StringUtils.isNotBlank(indexIds) && StringUtils.isNotBlank(ruleIds)) {
			String[] indexId = indexIds.split(",");
			String[] ruleId = ruleIds.split(",");
			for (int i=0; i<indexId.length; i++){
				indexRuleMap.put(indexId[i], ruleId[i]);
			}
		} else {
			throw new MyRuntimeException("请选择定性指标信息！");
		}
		return indexRuleMap;
	}

	//查公式
	private Map<String, String> findRateFormulaMap() {
		//查公式列表
		Map<String, String> formulaMap =EnterpriseRatingUtils.getAllCacheFormula(formulaMapper);
		if (formulaMap.size() == 0) {
			throw new RateInValidRuntimeException("评级公式库暂无数据！");
		}
		return formulaMap;
	}

	//开始评级
	private String doRate(int reportLen,
						  String rateNo,
						  Integer enterpriseId,
						  IndexModel model,
						  Map<String, String> indexRuleMap,
						  List<ModelElement> elementList,
						  Map<String, Object> reportSheetMap,
						  Map<String, String> formulaMap, int preOrRate)  {
		//评级结果
		List<RateResult> resultList = new ArrayList<>();
		//format
		DecimalFormat decimalFormat = new DecimalFormat("0.###");

		for (ModelElement element: elementList) {
			//以因素为单位保存评级结果
			List<IndexBean> indexList = new ArrayList<IndexBean>();
			indexList  = this.findRateIndexList(element.getId());
			for (IndexBean index: indexList) {

				Object ans = null;
				Integer score = null;
				IndexRule rule = null;
				String degree = null;
				if (index.getRegularIndexFlag().equals("0")) {
					//定量计算
					//公式父id
					Integer formulId = index.getFormulaId();
					if (null == formulId) {
						throw new RateInValidRuntimeException("指标【"+index.getIndexName()+"】中未指定评级公式信息！");
					}

					IndexFormula formula1Bak = formulaMapper.selectByPrimaryKey(formulId);
					if (null == formula1Bak) {
						throw new RateInValidRuntimeException("公式已被删除或者不存在无法评级！");
					}

					//指标中计算的年份
					int indexYearLen = index.getAveYears();
					//公式定义的年份
					int formulaYearLen = formula1Bak.getYearLen();
					if (formulaYearLen < indexYearLen) {
						throw new RateInValidRuntimeException("公式："+formula1Bak.getFormulaName()+"定义年份："+formulaYearLen+"  小于指标中定义的年份："+indexYearLen+"无法评级！");
					}

					rateLogger.info("提交的报告数量："+reportLen);

					int actuallyYeanLen = 0;
					//取公式
					String formula = null;
					if (reportLen < indexYearLen) {
						actuallyYeanLen = reportLen;
						//example：指标定义要用3年的报告评级，但是提交的只有一年报告,所以要报表的1年去找公式的1年公式评级
						rateLogger.info(index.getIndexName()+"需要评级的年份："+indexYearLen+" 实际评级的年份："+reportLen);
						formula = formulaMapper.getByParentIdAndYear(formulId, reportLen);
					} else {
						actuallyYeanLen = indexYearLen;
						rateLogger.info(index.getIndexName()+"需要评级的年份："+indexYearLen+" 实际评级的年份："+indexYearLen);
						//example:指标定义1年，但是提交的有2年，所以按照指标定义的1年去找公式定义的1年进行评级
						formula = formulaMapper.getByParentIdAndYear(formulId, indexYearLen);
					}

					if (null == formula) {
						throw new RateInValidRuntimeException("公式已被删除或者不存在无法评级！");
					}

					//计算该指标得分，不包括权重计算
					ans = EnterpriseRatingUtils.operatingIndexValueByFormula(actuallyYeanLen, formula, reportSheetMap, formulaMap);
					rule = ruleMapper.getByRangeAns(index.getId(), new Double(ans.toString()));
					if (null == rule) {
						throw new RateInValidRuntimeException("指标的得分在指标规则中未找到！  详细：【指标名称：" + index.getIndexName() + " 得分：" + ans + "】");
					}
					score = rule.getScore();
					degree = rule.getDegree();
				} else {
					//定性计算
					String ruleIdStr = indexRuleMap.get(index.getId()+"");
					if (null == ruleIdStr) {
						ans = "";
						score = 0;
						degree = "";
					} else {
						Integer ruleId = Integer.parseInt(indexRuleMap.get(index.getId().toString()));
						rule = ruleMapper.selectByPrimaryKey(ruleId);
						if (null != rule) {
							ans = rule.getValue();
							score = rule.getScore();
							degree = rule.getDegree();
						} else {
							ans = "";
							score = 0;
							degree = "";
						}
					}
				}

				RateResult result = new RateResult();
				result.setRatingApplyNum(rateNo);
				result.setElementName(element.getName());
				result.setIndexName(index.getIndexName());

				if (index.getRegularIndexFlag().equals("0")) {
					//String format = String.format("%.3f", new Double(ans.toString()));
					String format = decimalFormat.format(new Double(ans.toString()));
					result.setIndexData(format);
				} else {
					result.setIndexData(ans.toString());
				}

				Double doubleVale = index.getIndexWeight().doubleValue() * score.intValue();
				//result.setDoubleValue(new Double(String.format("%.3f", doubleVale)));
				result.setDoubleValue(new Double(decimalFormat.format(doubleVale)));
				result.setWeight(index.getIndexWeight());
				result.setValue(score);
				result.setDegree(degree);
				result.setEntId(enterpriseId);
				result.setFinalFlag("0");
				resultList.add(result);
			}
		}

		//模型匹配
		return matchModel(resultList, enterpriseId, rateNo, model, preOrRate);
	}

	//影子评级
	private void doSaveShadowRate(Integer approvalId, final Integer enterpriseId, String degree) {
		Approval approval = new Approval();
		approval.setId(approvalId);
		Date currentRateTime = new Date();
		Enterprise enterprise = enterpriseMapper.findById(enterpriseId);

		if (null != enterprise) {
			String name = enterprise.getName();
			RateData rateData = rateDataMapper.selectLastRateData(name);

			if (null != rateData) {
				//有公开评级
				Date shadowRateTime = rateData.getRateTime();
				long dayDiff = (currentRateTime.getTime() - shadowRateTime.getTime()) / (24*60*60*1000L);

				if (dayDiff <= 365) {
					//一年内公开有评级
					Integer priority = rateDataMapper.selectInstitutionPriorityByRateDataId(rateData.getId());
					if (null == priority) {
						throw new RateInValidRuntimeException("评级数据库有问题:评级机构为空");
					}

					if (priority.intValue() <= 1) {
						//中诚信评级
						String shadowRateResult = rateData.getRateResultName();
						approval.setShadowRatingResult(shadowRateResult);
						approval.setShadowApprovalTime(shadowRateTime);

						approvalMapper.updateByPrimaryKeySelective(approval);

						//使用影子评级
						rateDataMapper.updateShadow(rateData.getId());
					} else {
						//其他评级取最小的评级结果作为影子评级
						int sort = rateDataMapper.selectResultIdByName(degree);
						int shadowSort = rateData.getRateResult();
						String shadowRateResult = degree;
						approval.setShadowApprovalTime(currentRateTime);

						if (shadowSort > sort) {
							//其他评级机构的结果低，作为影子评级
							shadowRateResult = rateData.getRateResultName();
							approval.setShadowApprovalTime(shadowRateTime);

							//使用影子评级
							rateDataMapper.updateShadow(rateData.getId());
						}
						approval.setShadowRatingResult(shadowRateResult);
						approvalMapper.updateByPrimaryKeySelective(approval);
					}
				} else {
					//一年内公没有公开评级
					approval.setShadowRatingResult(degree);
					approval.setShadowApprovalTime(currentRateTime);
					approvalMapper.updateByPrimaryKeySelective(approval);
				}

			} else {
				//没有公开评级
				approval.setShadowRatingResult(degree);
				approval.setShadowApprovalTime(currentRateTime);
				approvalMapper.updateByPrimaryKeySelective(approval);
			}
		}
	}

	//模型匹配
	private String matchModel(List<RateResult> resultList,
							Integer enterpriseId,
							String rateNo,
							IndexModel model,
						  	int preOrRate) {
		Integer modelId = model.getId();
		//计算总分
		double sumValue = 0.0;
		for (RateResult result: resultList) {
			sumValue = DoubleUtil.add(sumValue, result.getDoubleValue());
		}
		IndexModelRule rule = indexModelMapper.getResultByModelIdAndValue(sumValue, modelId);
		if (null == rule) {
			throw new RateInValidRuntimeException("评级后的得分再评分卡中未找到！  详细信息【等分："+sumValue+"  评分卡："+model.getName()+"】");
		}

		//评分总结果
		RateResult finalResult = new RateResult();
		finalResult.setFinalFlag("1");
		finalResult.setRatingApplyNum(rateNo);
		finalResult.setEntId(enterpriseId);
		finalResult.setRatingApplyNum(rateNo);
		finalResult.setDegree(rule.getDegree());
		finalResult.setDoubleValue(sumValue);

		resultList.add(finalResult);
		if (1 == preOrRate) {
			//提交审批不存数据，只给预评级结果，审批时保存数据
			resultMapper.insertList(resultList);
		}
		return finalResult.getDegree();
	}

	//更新提交
	private void myUpdateApproval(Approval approval, HttpServletRequest request) {
		approval.setApprovalStatus(2);
		approval.setApprovalTime(new Date());
		approval.setApprover(ControllerUtil.getSessionUser(request).getLoginName());
		approval.setRefuseReason(request.getParameter("refuseReason"));
		approval.setActTaskId("");
		approvalMapper.updateByPrimaryKeySelective(approval);
		approval.setReportIds("("+approval.getReportIds()+")");
		enterpriseMapper.updateEnterpriseRe(approval);
	}
}
