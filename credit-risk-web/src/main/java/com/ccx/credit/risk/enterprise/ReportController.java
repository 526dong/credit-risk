package com.ccx.credit.risk.enterprise;

import com.alibaba.fastjson.JSON;
import com.ccx.credit.risk.api.enterprise.*;
import com.ccx.credit.risk.manager.report.CommonGainReport;
import com.ccx.credit.risk.manager.report.CommonGainReportValue;
import com.ccx.credit.risk.manager.report.ComonReportPakage;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseMapper;
import com.ccx.credit.risk.mapper.rate.ApprovalMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.approval.Approval;
import com.ccx.credit.risk.model.enterprise.*;
import com.ccx.credit.risk.util.*;
import com.ccx.credit.risk.util.excel.ImportExcel;
import com.ccx.credit.risk.utils.CommonMethodUtils;
import com.ccx.credit.risk.utils.ExportReportExcel;
import org.apache.commons.collections.map.HashedMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业报表
 * @author xzd
 * @date 2017/6/15
 */
@Controller
@RequestMapping(value = "/report")
public class ReportController {
	private static Logger logger = LogManager.getLogger(ReportController.class);

	@Autowired
	private EnterpriseApi enterpriseApi;

	@Autowired
	private EnterpriseMapper enterpriseMapper;
	
	@Autowired
	private ReportApi reportApi;

	@Autowired
	private ApprovalMapper approvalMapper;

	/*报表数据*/
	@Autowired
	private CommonGainReport commonGainReport;

	/*报表数据*/
	@Autowired
	private EnterpriseReportCheckApi enterpriseReportCheckApi;

	/*报表数据*/
	@Autowired
	private CommonGainReportValue commonGainReportValue;

	/*报表类型*/
	@Autowired
	EnterpriseReportTypeApi enterpriseReportTypeApi;

	/*报表模板*/
	@Autowired
	EnterpriseReportModelApi enterpriseReportModelApi;

	/*报表sheet*/
	@Autowired
	EnterpriseReportSheetApi enterpriseReportSheetApi;

	/*存储报表*/
	@Autowired
	private EnterpriseReportDataStoreApi enterpriseReportDataStoreApi;

	/**
	 * 查询报表列表详情
	 * @param enterpriseId
	 * @return Map<String, Object>
	 */
	@ResponseBody
	@PostMapping("/reportList")
	public Map<String, Object> reportList(HttpServletRequest request, @RequestParam(required = true)Integer enterpriseId){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		List<Report> reportBean = new ArrayList<>();

		try {
			//通过企业id查询企业报表概况信息
			reportBean = reportApi.findByEnterpriseId(enterpriseId);
			resultMap.put("reportBean", reportBean);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("查询企业报表概况信息失败", e);
			e.printStackTrace();
		}

		return resultMap;
	}

	@ResponseBody
	@PostMapping("/submitReportList")
	public Map<String, Object> submitReportList(@RequestParam(required = true)String reportIds){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			//通过企业id查询企业报表概况信息
			List<Integer> idList = new ArrayList<>();
			if (StringUtils.isNotBlank(reportIds)) {
				String[] id = reportIds.split(",");
				for (int i = 0; i < id.length; i++) {
					idList.add(Integer.parseInt(id[i]));
				}
			}
			if (idList.size() > 0) {
				List<Report> list = reportApi.submitReportList(idList);
				resultMap.put("list", list);
			} else {
				resultMap.put("list", new ArrayList<>());
			}

			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("查询企业报表概况信息失败", e);
		}
		return resultMap;
	}

	/**
	 * 通过确定的行业报表对应确定的报表类型来加载报表模板
	 * @param request
	 * @param reportType 报表类型
	 * @return
	 */
	@ResponseBody
	@PostMapping("/getReportModel")
	public Map<String, Object> getReportModel(HttpServletRequest request, @RequestParam(required = true)Integer reportType) {
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			//加载默认报表数据
			List<ComonReportPakage.Modle> reportData = commonGainReport.getModles(reportType);
			resultMap.put("code", 200);
			resultMap.put("reportData", reportData);
		} catch (Exception e) {
			resultMap.put("code", 500);
			e.printStackTrace();
			logger.error("查询报表数据失败！", e);
		}

		return resultMap;
	}

	/**
	 * 通过财务报表类型加载报表联表公式
	 * @param request
	 * @param reportType 报表类型
	 * @return
	 */
	@ResponseBody
	@PostMapping("/getReportCheckFormula")
	public Map<String, Object> getReportCheckFormula(HttpServletRequest request, @RequestParam(required = true)Integer reportType) {
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			//通过财务报表类型加载报表联表公式
			List<EnterpriseReportCheck> reportCheckList = enterpriseReportCheckApi.getByReportType(reportType);
			resultMap.put("code", 200);
			resultMap.put("reportCheckList", reportCheckList);
		} catch (Exception e) {
			resultMap.put("code", 500);
			e.printStackTrace();
			logger.error("查询报表数据失败！", e);
		}

		return resultMap;
	}

	/**
	 * 单个报表概况信息
	 * @param request
	 * @return Report
	 */
	@ResponseBody
	@PostMapping("/mainReport")
	@Record(operationType="查看报表详情",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="查看详情")
	public Map<String, Object> mainReport(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//报表概况id
		String reportId = request.getParameter("id");

		try {
			//通过报表id查询企业报表概况信息
			ComonReportPakage.BaseValues reportData = commonGainReportValue.getBaseValue(Integer.parseInt(reportId));

			if (reportData != null && reportData.getReport() != null) {
				Report report = reportData.getReport();
				if (!"".equals(report.getType())) {
					String reportName = enterpriseReportTypeApi.getNameById(report.getType());

					if (!StringUtils.isEmpty(reportName)) {
						report.setReportName(reportName);

						reportData.setReport(report);
					}
				}
			}

			resultMap.put("reportData", reportData);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("查询企业报表信息失败", e);
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 保证同一年份只有一张合并和一张非合并报表
	 * @param request report
	 * @return map结果
	 */
	@ResponseBody
	@PostMapping("/validateReportTimeAndCal")
	public Map<String, Object> validateReportTimeAndCal(HttpServletRequest request, @RequestParam(required = true) Integer enterpriseId,
			@RequestParam(required = true) Integer reportId, @RequestParam(required = true) String reportTime, @RequestParam(required = true) Integer cal){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		String calName = "";
		if (cal == 0) {
			calName = "母公司";
		} else if (cal == 1) {
			calName = "合并";
		}

		try {
			int count = reportApi.findByReportTimeAndCal(enterpriseId, reportTime, cal);

			//更新
			if (reportId > 0) {
				if (count == 1) {
					Report report = reportApi.findById(reportId);
					//更新时，数据库里查询到一个，说明是当前输入的，可以保存
					if (reportTime.equals(report.getReportTime()) && cal.equals(report.getCal())) {
						resultMap.put("code", 200);
					} else {
						//不是当前查询到的，重复，不保存
						resultMap.put("code", 500);
						resultMap.put("msg", reportTime +"年"+ calName +"报表已经存在，请重新添加！");
						return resultMap;
					}
				} else {
					resultMap.put("code", 200);
				}
			} else {
				//新增
				if (count > 0) {
					resultMap.put("code", 500);
					resultMap.put("msg", reportTime +"年"+ calName +"报表已经存在，请重新添加！");
					return resultMap;
				} else {
					resultMap.put("code", 200);
				}
			}

		} catch (Exception e) {
			resultMap.put("code", 500);
			e.printStackTrace();
			logger.error("通过报表年份和报表口径查询报表失败！", e);
		}

		return resultMap;
	}

	/**
	 * 保存单个报表概况信息-暂存到缓存中，并不进行入库
	 * @param request report
	 * @return map结果
	 */
	@ResponseBody
	@PostMapping("/doMainReport")
	@Record(operationType="保存报表信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> doMainReport(HttpServletRequest request, Report report){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//企业id
		String enterpriseId = request.getParameter("enterpriseId");
		//企业表-录入状态
		String state = request.getParameter("state");
		//报表概况id
		String reportId = request.getParameter("reportId");

		if (report == null) {
			resultMap.put("code", 500);
		} else {
			if (!StringUtils.isEmpty(enterpriseId)) {
				//录入状态：0-未完成，1-已完成
				if (!StringUtils.isEmpty(state)){
					report.setState(Integer.parseInt(state));
				} else {
					report.setState(0);
				}

				if (StringUtils.isEmpty(reportId) || "0".equals(reportId)) {
					//创建人
					User user = ControllerUtil.getSessionUser(request);
					if (user != null) {
						report.setCreatorName(user.getLoginName());
					}
				}

				//企业id
				report.setEnterpriseId(Integer.parseInt(enterpriseId));

				//将报表概况数据放到session中
				request.getSession().setAttribute("report", report);
				resultMap.put("code", 200);
			}
		}

		return resultMap;
	}

	/**
	 * 保存报表子表信息到session中
	 * @return map结果
	 */
	@ResponseBody
	@PostMapping("/saveReportSon")
	@Record(operationType="保存报表信息",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> saveReportSon(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//获取session中的reportSonDataMap
		Map<String, List<EnterpriseReportDataStore>> reportSonDataMap =
				(Map<String, List<EnterpriseReportDataStore>>) request.getSession().getAttribute("reportSonDataMap");
		if (reportSonDataMap == null) {
			reportSonDataMap = new HashMap<>();
		}

		//获取session中的sheetIdsMap
		Map<String, Integer> sheetIdsMap = (Map<String, Integer>) request.getSession().getAttribute("sheetIdsMap");
		if (sheetIdsMap == null) {
			sheetIdsMap = new HashMap<>();
		}

		//报表子表数据list
		List<EnterpriseReportDataStore> reportList = CommonMethodUtils.saveCommonReportSon(request, "");

		if (reportList != null && reportList.size() > 0) {
			resultMap.put("code", 200);
			//子表数据放入map中
			reportSonDataMap.put(request.getParameter("sheetName"), reportList);
			//map数据放入到session中
			request.getSession().setAttribute("reportSonDataMap", reportSonDataMap);

			//sheetId
			sheetIdsMap.put(request.getParameter("sheetName"), Integer.parseInt(request.getParameter("sheetId")));
			request.getSession().setAttribute("sheetIdsMap", sheetIdsMap);
		} else {
			resultMap.put("code", 500);
		}

		return resultMap;
	}

	/**
	 * 保存所有数据
	 * @return
	 */
	@ResponseBody
	@PostMapping("/doAllReport")
	@Record(operationType="保存所有报表修改",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> doAllReport(HttpServletRequest request, @RequestParam(required = true) Integer enterpriseId,
		   @RequestParam(required = true) Integer reportType, String track){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		String updateReport = request.getParameter("updateReport");
		String reportIds = request.getParameter("reportIds");

		//报表概况信息数据map
		Report report = (Report)request.getSession().getAttribute("report");

		//报表sheetIds
		Map<String, Integer> sheetIdsMap = (Map<String, Integer>) request.getSession().getAttribute("sheetIdsMap");

		//报表子表信息数据map
		Map<String, List<EnterpriseReportDataStore>> reportSonDataMap =
				(Map<String, List<EnterpriseReportDataStore>>)request.getSession().getAttribute("reportSonDataMap");

		if (report != null) {
			try {
				reportApi.saveReport(request, enterpriseId, report, reportType, sheetIdsMap, reportSonDataMap);
				resultMap.put("code", 200);
			} catch (Exception e) {
				resultMap.put("code", 500);
				logger.error("保存报表信息异常", e);
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	/**
	 * 删除报表概况信息,同时删除关联表信息
	 * @return map
	 */
	@ResponseBody
	@PostMapping("/delete")
	@Record(operationType="删除报表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> delete(@RequestParam(required = true) Integer enterpriseId, @RequestParam(required = true) Integer reportId){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			//逻辑删除：报表概况 report
			reportApi.deleteById(enterpriseId, reportId);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("删除报表概况,关联信息,附表信息id："+reportId+"失败！", e);
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 暂存-保存单个报表概况信息-保存到数据库
	 * @param request report
	 * @return map结果
	 */
	@ResponseBody
	@PostMapping("/doMomentReport")
	@Record(operationType="暂存报表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public Map<String, Object> doMomentReport(HttpServletRequest request, Report report) {
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//企业id
		String enterpriseId = request.getParameter("enterpriseId");
		//报表id
		String reportId = request.getParameter("reportId");
		//报表id
		report.setId(Integer.parseInt(reportId));

		if (!StringUtils.isEmpty(enterpriseId)) {
			try {
				reportApi.momentSaveOrUpdate(request, report, Integer.parseInt(enterpriseId));
				resultMap.put("code", 200);
			} catch (NumberFormatException e) {
				resultMap.put("code", 500);
				e.printStackTrace();
				logger.error("企业报表概况添加或更新失败", e);
			}
		}
		return resultMap;
	}

	/**
	 * 暂存-存储报表子表信息
	 */
	@ResponseBody
	@PostMapping("/doMomentReportSon")
	public Map<String, Object> doMomentReportSon(HttpServletRequest request){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//报表id
		String reportId = request.getParameter("reportId");
		//报表类型
		String reportType = request.getParameter("reportType");
		//报表子表类型
		String reportSonType = request.getParameter("reportSonType");

		//sheet id map
		Map<String, Integer> sheetIdMap = new HashMap<>();
		sheetIdMap.put(request.getParameter("sheetName"), Integer.parseInt(reportSonType == "" ? "0" : reportSonType));

		//保存报表数据
		List<EnterpriseReportDataStore> reportList = CommonMethodUtils.saveCommonReportSon(request, reportId);

		//批量插入报表数据
		try {
			if (!StringUtils.isEmpty(reportId)) {
				enterpriseReportDataStoreApi.batchInsert(sheetIdMap, reportList,
						Integer.parseInt(reportId), Integer.parseInt(reportType == "" ? "0" : reportType));
			}
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			logger.error("批量插入报表数据失败！", e);
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 是否添加了新的报表
	 */
	@ResponseBody
	@PostMapping("/isHaveNewReport")
	public Map<String, Object> isHaveNewReport(@RequestParam(required = true) Integer enterpriseId){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			int count = reportApi.findNewReportByEnt(enterpriseId);

			if (count > 0) {
				resultMap.put("code", 200);
			} else {
				resultMap.put("code", 500);
				resultMap.put("msg", "未添加新报表，请添加");
			}
		} catch (Exception e) {
			resultMap.put("code", 500);
			e.printStackTrace();
			logger.error("通过企业id查询是否添加新的报表失败", e);
		}

		return resultMap;
	}

	/**
	 * 判断报表中的录入状态，修改企业主体中的录入状态
	 */
	@ResponseBody
	@PostMapping("/updateState")
	public Map<String, Object> updateState(HttpServletRequest request, @RequestParam(required = true) Integer enterpriseId, String track){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//存放报表的录入状态
		List<Integer> stateList = new ArrayList<>();

		Enterprise enterprise = new Enterprise();

		String updateReport = request.getParameter("updateReport");
		String reportIds = request.getParameter("reportIds");
		String appId = request.getParameter("appId");

		//1、查询企业主体信息
		try {
			enterprise = enterpriseApi.findById(enterpriseId);
		} catch (Exception e) {
			logger.error("查询企业主体数据异常！", e);
			e.printStackTrace();
		}

		//指标完成标识
		boolean indexFlag = true;

		//2、查询企业主体关联指标
		try {
			Map<String, String> indexMap = enterpriseApi.selectEnterpriseIndexAndRules(enterpriseId);

			//定量指标数量
			int regularCount = enterpriseApi.selectRegularIndex(enterpriseId);

			if (indexMap == null || StringUtils.isBlank(indexMap.get("indexIds")) || StringUtils.isBlank(indexMap.get("ruleIds"))) {
				if (regularCount == 0) {
					indexFlag = false;
					resultMap.put("msg", "没有配置可选择的指标！");
					resultMap.put("code", 500);
					return resultMap;
				}
			}
		} catch (Exception e) {
			logger.error("查询指标信息失败！", e);
			e.printStackTrace();
		}

		//3、查询企业主体关联报表
		try {
			List<Report> reportList =  reportApi.findByEnterpriseId(enterpriseId);

			if (reportList != null && reportList.size()>0) {
				for (Report report:reportList) {
					if (report != null) {
						stateList.add(report.getState());
					}
				}
			}
		}catch (Exception e){
			logger.error("更新企业录入状态失败！", e);
			e.printStackTrace();
		}

		//报表完成标识
		boolean reportFlag = true;

		//3.2 判断state值
		if (stateList != null && stateList.size()>0) {
			for (Integer state:stateList) {
				if (state != 1) {
					reportFlag = false;
					break;
				}
			}
		} else {
			reportFlag = false;
		}

		if (!reportFlag) {
			resultMap.put("msg", "财务报表信息未录入完成，请重新录入！");
			resultMap.put("code", 500);
			return resultMap;
		} else {
			if (indexFlag && reportFlag) {
				//录入状态：已完成 state=1
				enterprise.setState(1);

				//跟踪评级标识
				if (!StringUtils.isEmpty(track)) {
					//将评级状态改为跟踪评级
					enterprise.setType(1);
					//审批状态置为未提交
					enterprise.setApprovalState(0);
				}

				resultMap.put("code", 200);
			}

			//4 同步更新企业主体中的state值
			try {
				enterpriseApi.updateBySelect(enterprise);
				if ("updateReport".equals(updateReport)) {
					//判读修改状态
					Approval approval = approvalMapper.selectByPrimaryKey(Integer.parseInt(appId));
					int codebak = approval.getReportHashCode();
					int codeNew = reportApi.calReportHashCode(reportIds);

					if (codebak != codeNew || 1 == approval.getEditFlag()) {
                        approvalMapper.updateEditFlag(approval.getRatingApplyNum(), 1);
					} else {
						resultMap.put("code", 500);
						resultMap.put("msg", "必须修改申请信息后才能录入完成！");
					}
				}
			} catch (Exception e) {
				resultMap.put("code", 500);
				resultMap.put("msg", "企业主体录入状态更新失败");
				logger.error("企业主体录入状态更新失败！", e);
			}
		}

		return resultMap;
	}

	/*****************导入报表、下载报表模板、导出报表 start******************/

	/**
	 * 导入报表数据
	 * errType 001-文件为空，002-文件解析出错
	 * @return
	 */
	@ResponseBody
	@PostMapping("/importReportDataExcel")
	@Record(operationType="导入报表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public String importReportDataExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam("excelFile") MultipartFile excelFile){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		//财务报表类型
		Integer reportType = Integer.parseInt(request.getParameter("reportType") == "" ? "0" : request.getParameter("reportType"));
		//财务报表子表类型
		Integer reportSonType = Integer.parseInt(request.getParameter("reportSonType") == "" ? "0" : request.getParameter("reportSonType"));
		//report map
		Map<String, Object> reportMap = new HashMap<>();
		//report data list
		List<EnterpriseReportSheet> reportAllList = new ArrayList<>();

		if (excelFile.isEmpty()) {
			resultMap.put("code", 500);
			resultMap.put("msg", 401);
			logger.error("上传Excel文件报错，错误原因============>没有上传文件");
			return JSON.toJSONString(resultMap);
		}

		//财务报表类型
		if (reportType == 0) {
			resultMap.put("code", 500);
			resultMap.put("msg", 401);
			logger.error("上传Excel文件报错，错误原因============>报表类型为空");
			return JSON.toJSONString(resultMap);
		}

		if (excelFile != null) {
			try {
				// 初始化一个工作簿
				Workbook wb = null;
				FormulaEvaluator formulaEvaluator = null;
				try {
					InputStream inputStream = excelFile.getInputStream();
					wb = new HSSFWorkbook(inputStream);
					formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) wb);
				} catch (Exception e) {
					InputStream inputStream = excelFile.getInputStream();
					wb = new XSSFWorkbook(inputStream);
					formulaEvaluator = new XSSFFormulaEvaluator((XSSFWorkbook) wb);
					e.printStackTrace();
				}

				//模板中的字段id
				List<Map<String, Integer>> modelIds = new ArrayList<>();
				List<EnterpriseReportSheet> enterpriseReportSheets = new ArrayList<>();
				EnterpriseReportSheet enterpriseReportSheet = new EnterpriseReportSheet();

				if (reportSonType == 0) {
					//全部导入
					enterpriseReportSheets = enterpriseReportSheetApi.selectByReportType(reportType);

					for (EnterpriseReportSheet reportSheet:enterpriseReportSheets) {
						Map<String, Integer> modelIdMap = putReportModelId(reportType, reportSheet.getId());
						modelIds.add(modelIdMap);
					}
				} else {
					//单个导入
					enterpriseReportSheet = enterpriseReportSheetApi.getById(reportSonType);
					Map<String, Integer> modelIdMap = putReportModelId(reportType, reportSonType);

					modelIds.add(modelIdMap);
				}

				//循环Excel中的sheet
				for (int i = 0;i < wb.getNumberOfSheets();i++) {
					EnterpriseReportSheet reportSheet = new EnterpriseReportSheet();
					Sheet sheet = wb.getSheetAt(i);
					reportMap = ImportExcel.excelToReport(enterpriseReportSheetApi, reportType, reportSonType, modelIds.get(i), sheet, formulaEvaluator);

					if (reportMap != null && !reportMap.isEmpty()) {
						if (reportMap.get("code") != null) {
							String code = String.valueOf(reportMap.get("code"));
							if ("200".equals(code)) {
								if (reportMap.get("reportList") != null) {
									resultMap.put("code", 200);

									if (enterpriseReportSheets.size() == 0) {
										reportSheet.setId(enterpriseReportSheet.getId());
									} else {
										reportSheet.setId(enterpriseReportSheets.get(i).getId());
									}

									reportSheet.setReportDataList((List<EnterpriseReportDataStore>) reportMap.get("reportList"));

									reportAllList.add(reportSheet);
								} else {
									resultMap.put("code", 500);
									resultMap.put("msg", 102);
									break;
								}
							} else if ("500".equals(code)) {
								resultMap.put("code", 500);
								resultMap.put("msg", reportMap.get("msg"));
								break;
							}
						}
					}
				}

				resultMap.put("reportData", reportAllList);
			} catch (Exception e) {
				resultMap.put("code", 500);
				resultMap.put("msg", 402);
				logger.error("文件解析出错！", e);
				e.printStackTrace();
			}
		}

		return JSON.toJSONString(resultMap);
	}

	/**
	 * 将模板中的字段id放入到map中
	 * @param reportType 财务报表类型
	 * @param reportSonType 财务报表子表类型
	 * @return
	 */
	public Map<String, Integer> putReportModelId(Integer reportType, Integer reportSonType){
		//通过财务报表类型和财务报表子表类型查询报表模板
		List<EnterpriseReportModel> enterpriseReportModels = enterpriseReportModelApi.selectByReportType(reportType, reportSonType);

		Map<String, Integer> map = new HashedMap();

		for (EnterpriseReportModel reportModel:enterpriseReportModels) {
			map.put(reportModel.getFinancialSubject(), reportModel.getId());
		}

		return map;
	}

	/**
	 * 导出报表
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/downloadOrExportReportDataExcel")
	@Record(operationType="导出报表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="录入信息")
	public void downloadOrExportReportDataExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//报表id
		Integer reportId = Integer.parseInt(request.getParameter("reportId") == "" ? "0" : request.getParameter("reportId"));
		//是下载报表模板还是导出报表数据
		String reportModel = request.getParameter("reportModel");
		//财务报表类型
		Integer reportType = Integer.parseInt(request.getParameter("reportType") == "" ? "0" : request.getParameter("reportType"));
		//财务报表子表类型
		Integer reportSonType = Integer.parseInt(request.getParameter("reportSonType") == "" ? "0" : request.getParameter("reportSonType"));
		//财务报表第几张子表
		Integer reportIndex = Integer.parseInt(request.getParameter("reportIndex") == "" ? "0" : request.getParameter("reportIndex"));

		//导出的报表名称
		String exportFileName = "" + reportType;

		//Excel模板所在的路径
		String filePath = null;

		try {
			filePath = CommonMethodUtils.getFilePath(PropertiesUtil.getProperty("enterprise_report_template_path"), exportFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//保证路径是一个文件不是一个目录
		if (!StringUtils.isEmpty(filePath) && filePath.contains(".")) {
			File file = new File(filePath);

			// 初始化一个工作簿
			Workbook wb = getWorkBook(file);

			if (!reportModel.isEmpty()) {
				//文件名称
				String fileName = dealFileNameEncode(request, reportType, reportSonType);

				//设置response参数，可以打开下载页面
				response.reset();
				response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + ".xls\"");
				response.setContentType("application/msexcel;charset=UTF-8");// 设置类型

				OutputStream out = response.getOutputStream();

				//导出Excel
				ExportReportExcel exportExcel = new ExportReportExcel();

				if (wb != null) {
					if ("0".equals(reportModel)) {
						if (reportId != 0) {
							//通过报表id查询报表数据
							ComonReportPakage.BaseValues baseValue = commonGainReportValue.getBaseValue(reportId);

							if (baseValue != null) {
								//报表中子表数据
								List<ComonReportPakage.Values> values = baseValue.getValues();

								if (values != null && values.size() > 0) {
									try {
										//导出报表数据
										exportExcel.doReportExcel(out, wb, values, reportIndex);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					} else if ("1".equals(reportModel)) {
						//下载报表模板
						exportExcel.doReportExcel(out, wb, null, reportIndex);
					}
				}
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 从路径中将流数据读取到Excel中
	 * @param file
	 * @return Workbook
	 * @throws Exception
	 */
	public Workbook getWorkBook(File file) throws Exception {
		// 初始化一个工作簿
		Workbook wb = null;

		try {
			//通过流读取文件
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			wb = new HSSFWorkbook(bis);

			bis.close();
		} catch (Exception e) {
			e.printStackTrace();

			//通过流读取文件
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			wb = new XSSFWorkbook(bis);

			bis.close();
		}

		return wb;
	}

	/**
	 * 解决导出文件名称乱码问题
	 * @param request
	 * @param reportType
	 * @param reportSonType
	 * @return
	 */
	public String dealFileNameEncode(HttpServletRequest request, Integer reportType, Integer reportSonType){
		//文件名称
		String fileName = null;

		//处理IE乱码
		String userAgent = request.getHeader("user-agent");

		//通过报表获取文件名称
		String str = getFileName(reportType, reportSonType);

		if (userAgent != null && userAgent.indexOf("Firefox") >= 0 || userAgent.indexOf("Chrome") >= 0 || userAgent.indexOf("Safari") >= 0) {
			try {
				fileName = new String(str.getBytes("UTF-8"),"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileName= URLEncoder.encode(str, "UTF8"); //其他浏览器
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return fileName;
	}

	/**
	 * 获取报表子表sheet名称-即文件名称
	 * @param reportType
	 * @param reportSonType
	 * @return
	 */
	public String getFileName(Integer reportType, Integer reportSonType){
		String sheetName = "";

		if (reportSonType == 0) {
			//1、全表导出
			//通过报表类型查询报表名称
			EnterpriseReportType enterpriseReportType = enterpriseReportTypeApi.getById(reportType);

			if (enterpriseReportType != null) {
				sheetName = enterpriseReportType.getName();
			}
		} else {
			//2、单表导出
			//通过报表类型和报表子表类型查询报表子表名称
			EnterpriseReportSheet reportSheet = enterpriseReportSheetApi.getById(reportSonType);

			if (reportSheet != null) {
				sheetName = reportSheet.getName();
			}
		}

		return sheetName;
	}

	/*****************导入报表、下载报表模板、导出报表 end******************/

	/*
	* 最终审批
	*/
	@GetMapping("/submitRate")
	@ResponseBody
	public Map<String, Object> submitRate(String rateNo, Integer industryId2) {
		Map<String, Object> resultMap = null;

		try {
			resultMap = reportApi.submiRate(rateNo, industryId2);
			resultMap.put("code", 200);
		} catch (Exception e) {
			logger.error("提交报告评级异常：", e);
			resultMap =	new HashMap<String, Object>();
			resultMap.put("code", 400);
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}
}