package com.ccx.credit.risk.financialAnaly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccx.credit.risk.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.ccx.credit.risk.api.enterprise.EnterpriseApi;
import com.ccx.credit.risk.api.enterprise.EnterpriseIndustryApi;
import com.ccx.credit.risk.api.financialAnaly.FinancialAnalyApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.manager.report.CommonGainReport;
import com.ccx.credit.risk.manager.report.CommonGainReportValue;
import com.ccx.credit.risk.manager.report.ComonReportPakage;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseIndustry;
import com.ccx.credit.risk.model.enterprise.FinancialAnaly;
import com.ccx.credit.risk.model.enterprise.Report;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping("/financialAnaly")
public class FinancialAnalyController extends BasicController{

	private static Logger logger = LogManager.getLogger(FinancialAnalyController.class);
	private static FormulaEvaluator evaluator;
	private static final String EXCEL_XLS = "xls";  
    private static final String EXCEL_XLSX = "xlsx"; 
	
	@Autowired
	private CommonGainReport commonGainReport;
	@Autowired
    private CommonGainReportValue commonGainReportValue;
	@Autowired
	private EnterpriseIndustryApi industryApi;
	@Autowired
	private FinancialAnalyApi financialAnalyApi;
	@Autowired
	private EnterpriseApi enterpriseApi;
	

	/**
	 * 企业财务分析页面
	 */
	@RequestMapping(value = "/companyMsgList", method = RequestMethod.GET)
	public String manager() {
		return "/financialAnalysis/companyMsgList";
	}
	
	/**
	 * 
	 * @Title: findInsFirstIndustry  
	 * @author: WXN
	 * @Description: 查询机构行业
	 * @param: @param map
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
    @RequestMapping(value="/findInsFirstIndustry",method=RequestMethod.POST,produces = "application/json; charset=utf-8")
	@ResponseBody
	public String findInsFirstIndustry(@RequestBody Map<String,Object> map){
    	List<EnterpriseIndustry> insNutureList = industryApi.findAllIndustryByPid(0);
		return JSON.toJSONString(insNutureList);
    }
    /**
	 * 异步加载二级行业
	 */
	@ResponseBody
	@PostMapping("/getIndustry")
	public Map<String, Object> getIndustry(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<>();
		
		//加载二级行业
		resultMap = EnterpriseDataUtils.loadIndustry(request, industryApi);
		
		return resultMap;
	}
	
	
	@PostMapping("/findCompanyMsgList")
	@ResponseBody
	@Record(operationType="查询企业信息列表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="企业财务分析")
	public PageInfo<Enterprise> findCompanyMsgList(HttpServletRequest request) {
		PageInfo<Enterprise> pages = new PageInfo<Enterprise>();
		// 获取查询条件
		Map<String, Object> params = ControllerUtil.requestMap(request);
		User user =ControllerUtil.getSessionUser(request);
		if(UsedUtil.isNotNull(user)){
    		int insId = user.getInstitutionId();
    		params.put("insId", insId);
    	}
		String industryFirstStr = (String) params.get("industryFirstStr");
		if(UsedUtil.isNotNull(industryFirstStr)){
			if("0000"==industryFirstStr||"0000".equals(industryFirstStr)){
				params.put("industryFirstStr", "");
			}
		}
		String industrySecondStr = (String) params.get("industrySecondStr");
		if(UsedUtil.isNotNull(industrySecondStr)){
			if("0000"==industrySecondStr||"0000".equals(industrySecondStr)){
				params.put("industrySecondStr", "");
			}
		}
		String city = (String) params.get("city");
		if(UsedUtil.isNotNull(city)){
			if("0000"==city||"0000".equals(city)){
				params.put("city", "");
			}
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
		pages = financialAnalyApi.findCompanyMsgList(params);
		return pages;
	}
	
	/**
	 * 
	 * @Title: toEnterpriseAndReportDetail  
	 * @author: WXN
	 * @Description: 查看企业信息以及报表信息
	 * @param: @param request
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	@RequestMapping(value="/toEnterpriseAndReportDetail",method=RequestMethod.GET)
	@Record(operationType="查看企业详情",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="查看详情")
	public String toEnterpriseAndReportDetail(HttpServletRequest request){
		//企业id
		String comId = null==request.getParameter("comId")?"":request.getParameter("comId").trim();
		//加载企业初始化数据
		EnterpriseDataUtils.loadInitData(request, enterpriseApi, industryApi);
		if (!StringUtils.isEmpty(comId)) {
			try {
				//企业主体信息
				Enterprise enterprise = enterpriseApi.findById(Integer.parseInt(comId));
				if (null != enterprise){
					//加载企业数据：指标规则
					EnterpriseDataUtils.loadIndexData(request, enterprise, enterpriseApi);
					request.setAttribute("enterprise", enterprise);
					//区别是否有录入完成字段
					request.setAttribute("method", "finance");
				}
			} catch (Exception e) {
				logger.error("查询企业主体信息失败", e);
				e.printStackTrace();
			}
			request.setAttribute("enterpriseId", comId);
			request.setAttribute("enterpriseFlag", "financialLook");
		}
		return "enterprise/enterpriseDetails";
	}
	
	/**
	 * 
	 * @Title: checkOutReportType  
	 * @author: WXN
	 * @Description: 根据开始时间确定此时间的报表类型 决定能选择的 结束时间
	 * @param: @param request
	 * @param: @return      
	 * @return: int      
	 * @throws
	 */
	@RequestMapping(value="/checkOutReportType",method=RequestMethod.POST)
	@ResponseBody
	public int checkOutReportType(HttpServletRequest request,HttpServletResponse response){
		//主体id
		String financialID = null==request.getParameter("financialID")?"":request.getParameter("financialID").trim();
		//报表开始时间
		String reportStartTime = null==request.getParameter("reportStartTime")?"":request.getParameter("reportStartTime").trim();
		//报表口径
		String koujing = null==request.getParameter("koujing")?"":request.getParameter("koujing").trim();
		//当前reportStartTime年份的类型
		int courrentType = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int result = Integer.parseInt(sdf.format(new Date()));
		if(UsedUtil.isNotNull(financialID)&&UsedUtil.isNotNull(reportStartTime)&&UsedUtil.isNotNull(koujing)){
			//第一步：根据主体id获取到与他关联的报表List
			List<Report> reportList = financialAnalyApi.findReportList(Integer.parseInt(financialID),reportStartTime,"",koujing);
			if (null != reportList && !reportList.isEmpty()) {
				//确定第一条类型
				courrentType = reportList.get(0).getType();
				String firstReportStartTime = reportList.get(0).getReportTime();
				if (-1 != courrentType) {
					for (int i = 1; i < reportList.size(); i++) {
						//报表类型id
						int reportType = reportList.get(i).getType();
						if (courrentType != reportType) {
							result = Integer.parseInt(reportList.get(i-1).getReportTime());
							break;
						}
					}
				} else {
					result = Integer.parseInt(firstReportStartTime);
				}
			} else {
				result = Integer.parseInt(sdf.format(new Date()));
			}
		}else{
			result = Integer.parseInt(reportStartTime);
		}
		//将开始时间与结束时间控制在三年以内
		if(result-Integer.parseInt(reportStartTime)>2){
			result = Integer.parseInt(reportStartTime)+2;
		}
		return result;
	}
	
	/**
	 * 
	 * @Title: exportFinancialReport  
	 * @author: WXN
	 * @Description: 导出财务分析报表 
	 * @param: @param request      
	 * @return: void      
	 * @throws
	 */
	@RequestMapping(value="/exportFinancialReport",method=RequestMethod.POST,produces = "text/html;charset=utf-8")
	@ResponseBody
	@Record(operationType="导出财务报表",
			operationBasicModule="大中型企业内部评级",
			operationConcreteModule ="查看详情")
	public String exportFinancialReport(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> mapResult = new HashMap<String, Object>();
		String result = "999";
		String resultMsg = "错误原因";
		//主体id
		String financialID = null==request.getParameter("financialID")?"":request.getParameter("financialID").trim();
		//报表开始时间
		String reportStartTime = null==request.getParameter("reportStartTime")?"":request.getParameter("reportStartTime").trim();
		//报表结束时间
		String reportEndTime = null==request.getParameter("reportEndTime")?"":request.getParameter("reportEndTime").trim();
		//报表口径
		String koujing = null==request.getParameter("koujing")?"":request.getParameter("koujing").trim();
		if(UsedUtil.isNotNull(financialID)&&UsedUtil.isNotNull(reportStartTime)&&UsedUtil.isNotNull(reportEndTime)&&UsedUtil.isNotNull(koujing)){
			//第一步：根据主体id获取到与他关联的报表List,并且list中的所有报表类型id必须一致，要不然会导致导出多张财务报表
			List<Report> reportList = financialAnalyApi.findReportList(Integer.parseInt(financialID),reportStartTime,reportEndTime,koujing);
			if (null != reportList && !reportList.isEmpty()) {
				//由于list中的所有报表类型id必须一致，所以直接去除第一个list中的报表类型id，获取报表类型以及分析财务模板，可以避免多次调用浪费性能
				//获取财务模板中的 数据类型名称 以及所在行
	            List<ComonReportPakage.Modle> dataTypelist = null;
	            try {
	            	dataTypelist = commonGainReport.getModles(reportList.get(0).getType());
				} catch (Exception e) {
					logger.info("根据报表类型id获取报表类型的类型名称异常，异常原因："+e);
					resultMsg = "未查找到对应的财务报表";
					mapResult.put("result", result);
					mapResult.put("resultMsg", resultMsg);
					return JSON.toJSONString(mapResult);
				}
	            //解析财务模板 确定数据类型以及子属性的行号
//	            Map<String, Map<String, Integer>> financialMap = new LinkedHashMap<String, Map<String, Integer>>();
	            //通过报表类型获取文件路径以及文件名信息
				FinancialAnaly financialAnaly = financialAnalyApi.getFinancialTemByType(reportList.get(0).getType());
				if(null != dataTypelist && !dataTypelist.isEmpty()){
	            	if(null != financialAnaly){
	            		//将模板复制到临时文件夹中进行更新操作，完成下载后进行删除，防止文件占用空间
	            		//由于原模板不能改动，所以复制模板这一步至关重要
	            		//使用state表示成功与否。666表示成功，999表示失败。一旦失败，则不能进行后续操作
	            		financialAnaly = copyFinancialTemplate(financialAnaly);
	            		int stateFlag = financialAnaly.getState();
	            		if(666 == stateFlag){
	            			//解析财务模板 确定数据类型以及子属性的行号
//		            		financialMap = analyFinancialTemplate(financialAnaly,dataTypelist);
		            		//更新操作
							boolean updateState = updateExcel(reportList,financialAnaly,dataTypelist);
							if(!updateState){
								logger.info("模板更新数据失败");
								resultMsg = "财务分析报表生成失败";
								mapResult.put("result", result);
								mapResult.put("resultMsg", resultMsg);
								return JSON.toJSONString(mapResult);
							}
	            		}else{
	            			logger.info("模板复制失败，不能进行后续更新操作");
							resultMsg = "财务分析报表生成失败";
							mapResult.put("result", result);
							mapResult.put("resultMsg", resultMsg);
							return JSON.toJSONString(mapResult);
	            		}
						resultMsg = "导出成功";
						result = "1000";
						mapResult.put("result", result);
						mapResult.put("resultMsg", resultMsg);
						return JSON.toJSONString(mapResult);
	            	}else{
	            		logger.info("根据报表类型id没有获取到财务报表模板信息");
	            		resultMsg = "未获取到对应的财务报表";
						mapResult.put("result", result);
						mapResult.put("resultMsg", resultMsg);
						return JSON.toJSONString(mapResult);
	            	}
	            }else{
            		logger.info("根据报表类型id没有获取到报表类型的类型名称");
            		resultMsg = "未获取到对应的财务报表";
					mapResult.put("result", result);
					mapResult.put("resultMsg", resultMsg);
					return JSON.toJSONString(mapResult);
            	}
			} else {
				logger.info("导出财务分析报表失败，失败原因======》》》》》》根据主体id没有获取到与他关联的报表id列表");
				resultMsg = "未获取到对应的财务报表";
				mapResult.put("result", result);
				mapResult.put("resultMsg", resultMsg);
				return JSON.toJSONString(mapResult);
			}
		}else{
			logger.info("导出财务分析报表失败，失败原因======》》》》》》传参错误");
			resultMsg = "传参错误";
			mapResult.put("result", result);
			mapResult.put("resultMsg", resultMsg);
			return JSON.toJSONString(mapResult);
		}
	}
	
	/**
	 * 
	 * @Title: analyFinancialTemplate  
	 * @author: WXN
	 * @Description: 解析财务模板 确定数据类型以及子属性的行号
	 * @param: @param dataTypelist
	 * @param: @return      
	 * @return: Map<String,Map<String,Integer>>      
	 * @throws
	 */
	public Map<String, Map<String, Integer>> analyFinancialTemplate(FinancialAnaly financialAnaly,List<ComonReportPakage.Modle> dataTypelist){
		//返回的结果
		Map<String, Map<String, Integer>> resultMap = new LinkedHashMap<String, Map<String, Integer>>();
		//xls文件进行测试，2003格式的，非xlsx
		String filePath = financialAnaly.getPath();//文件路径
//		String fileName = financialAnaly.getName()+".xls";//文件名称
		String template_name = financialAnaly.getTemplateName();//模板在服务器中的别名
        //建立数据的输入管道
        FileInputStream fileInputStream = null;
        //初始化一个工作簿
        Workbook wb = null;
        try {
        	File file = new File(filePath+template_name);
            fileInputStream = new FileInputStream(file);
			wb = new HSSFWorkbook(fileInputStream);
			 //获取第二个sheet页-原始数据 合并报表-固定
	        Sheet sheet = wb.getSheetAt(1);
	        /**
	         * 第一步 获取上传财务模板中的所有报表类型  即sheet页中的第一列以及所在行号
	         */
	        //定义第一列属性的list
	        List<String> cellList = new ArrayList<String>();
	        //定义第一列行号的list
	        List<Integer> cellNumList = new ArrayList<Integer>();
	        //excel行数 并不一定都是有效行 即可能存在空行
	        int lastRowNum = sheet.getLastRowNum();
	        logger.info("sheet表行数为：" + lastRowNum);
	        for(int i=0; i<lastRowNum; i++){
	            Row row = sheet.getRow(i);//取第一行的列
	            if(UsedUtil.isNotNull(row)){
	            	Cell cell = row.getCell(0);//取第一列的单元格
	            	if(UsedUtil.isNotNull(row)){
	                    String cellValue = getCellValue(cell);//取单元格内容
	                    if(UsedUtil.isNotNull(cellValue)){
	                    	cellList.add(cellValue); //第一列所有的属性的list 不包括空的单元格
	                    	cellNumList.add(i);//第一列所有的行号的list 不包括空的单元格  属性与行号一一对应
	                    }
	            	}
	            }
	        }
	        logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列属性list为=========》》》" + cellList);
	        logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列行号list为=========》》》" + cellNumList);
	        //确定财务模板中的类型表名以及所在行
	        Map<String, Integer> shuxingMap = new LinkedHashMap<String, Integer>();
	        //循环数据类型名称和财务sheet中的属性 当属性相同时 确定该字表所在的行 从而找出该字表所拥有的子属性
	        for (int i = 0; i < dataTypelist.size(); i++) {
	        	String dataTypeName = dataTypelist.get(i).getSheetName();//数据类型名称
	        	//循环财务sheet页中的map
	        	for (int j = 0; j < cellList.size(); j++) {
	        		String cellListN = cellList.get(j);
	        		if(UsedUtil.isNotNull(cellListN)){
	        			if(cellListN == dataTypeName || cellListN.equals(dataTypeName)){
	        				shuxingMap.put(cellListN, cellNumList.get(j));
	        			}
	        		}
				}
			}
	        if (null == shuxingMap || 0 == shuxingMap.size() || shuxingMap.isEmpty()) {
	        	logger.info("解析财务分析模板,获取财务类型以及所在行为空");
	            return resultMap;
			}
	        logger.info("第二步>>>>>>>>数据类型名称以及所在行为=========》》》" + shuxingMap);
	        /**
	         * 第三步 获取财务模板中每一个 报表类型下的所有属性 
	         * 封装结构为 {报表类型1:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},报表类型2:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},...}
	         */
	        //找出数据类型名称以及他下面的子属性
	        //找出数据类型名称以及他下面的子属性的位置
	        List<String> temList = new ArrayList<String>();
			List<Integer> temList2 = new ArrayList<Integer>();
	        for (int i = 0; i < cellList.size(); i++) {
				String cellListname = cellList.get(i);
				//遍历map中的键  
				for (String key : shuxingMap.keySet()) {  
				    if(key==cellListname || cellListname.equals(key)){
				    	temList.add(key);
				    	temList2.add(i);
				    }  
				}
			}
	        Map<String, Map<String, Integer>> caiwushuxingMap = new LinkedHashMap<String, Map<String, Integer>>();
	        for (int i = 0; i < temList2.size(); i++) {
	        	int temI = 0;
	        	int temJ = 0;
	        	if(i<temList2.size()-1){
	        		temI = temList2.get(i)+1;
	        		temJ = temList2.get(i+1);
				}else{
					temI = temList2.get(i)+1;
	        		temJ = cellList.size();
				}
//	        	List<Map<String, Integer>> zishuxingtemList = new ArrayList<Map<String, Integer>>();
	        	Map<String, Integer> zishuxingMap = new LinkedHashMap<String, Integer>();
	        	for (int j = temI; j < temJ; j++) {
	        		zishuxingMap.put(cellList.get(j), cellNumList.get(j));
//	        		zishuxingtemList.add(zishuxingMap);
				}
	        	caiwushuxingMap.put(temList.get(i), zishuxingMap);
			}
	        if (null == caiwushuxingMap || 0 == caiwushuxingMap.size() || caiwushuxingMap.isEmpty()) {
	        	logger.info("解析财务分析模板,获取数据类型名称以及其下所有属性名称和所在行号为空");
	            return resultMap;
			}
	        logger.info("第三步>>>>>>>>数据类型名称以及其下所有属性名称和所在行号=========》》》" + caiwushuxingMap);
	        resultMap = caiwushuxingMap;
	        fileInputStream.close();
		} catch (IOException e) {
			logger.info("解析财务分析模板失败，失败原因======》》》》》》"+e);
			return resultMap;
		}
        return resultMap;
	}
	
	/**
     * 对Excel的各个单元格的格式进行判断并转换
     */
    private static String getCellValue(Cell cell) {
        //判断是否为null或空串
        if (cell==null || cell.toString().trim().equals("")) {
            return "";
        }
        String cellValue = "";
        int cellType=cell.getCellType();
        if(cellType == Cell.CELL_TYPE_FORMULA){ //表达式类型
            cellType= evaluator.evaluate(cell).getCellType();
        }
        switch (cellType) {
        case Cell.CELL_TYPE_STRING: //字符串类型
            cellValue= cell.getStringCellValue().trim();
            cellValue=StringUtils.isEmpty(cellValue) ? "" : cellValue; 
            break;
        case Cell.CELL_TYPE_BOOLEAN:  //布尔类型
            cellValue = String.valueOf(cell.getBooleanCellValue()); 
            break; 
        case Cell.CELL_TYPE_NUMERIC: //数值类型
             if (HSSFDateUtil.isCellDateFormatted(cell)) {  //判断日期类型
            	 Date d = cell.getDateCellValue();
                 DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                 cellValue = formater.format(d);
             } else {  //否
                 cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue()); 
             } 
            break;
        default: //其它类型，取空串吧
            cellValue = "";
            break;
        }
        return cellValue.trim();
    }
    
    /**
     * 
     * @Title: exportFinancialTemplate  
     * @author: WXN
     * @Description: 下载财务分析
     * @param: @param financialAnaly
     * @param: @param request
     * @param: @param response      
     * @return: void      
     * @throws
     */
    public void exportFinancialTemplate(FinancialAnaly financialAnaly,HttpServletRequest request,HttpServletResponse response) {
    	InputStream inputStream = null;
		OutputStream os = null;
		if(null != financialAnaly){
			try {
				String filePath = financialAnaly.getPath();//文件路径
				String template_name = financialAnaly.getTemplateName();//模板在服务器中的别名
				String fileExt = template_name.substring(template_name.lastIndexOf("."),template_name.length());
    			String fileName = financialAnaly.getName()+fileExt;//文件名称
    			String ZipName=new String(fileName.getBytes(), "iso8859-1");//Zip名称 中文转码
    			response.setContentType("application/vnd.ms-excel");
    			response.setHeader("Content-Disposition", "attachment;fileName="+ZipName);
    			inputStream = new FileInputStream(filePath+template_name);
    			System.err.println("下载文件路径======"+filePath+template_name);
    			os = response.getOutputStream();
    			byte[] b = new byte[2048];
    			int length;
    			while ((length = inputStream.read(b)) > 0) {
    				os.write(b, 0, length);
    			}
    			// 这里主要关闭。
    			os.flush();
    			os.close();
    			inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("下载Excel文件异常");
			}
		}
    }
    
    /**
     * 
     * @Title: copyFinancialTemplate  
     * @author: WXN
     * @Description: 用文件通道的方式来进行文件复制,效率更高一些
     * 由于复制这一步至关重要，所以用state表示复制成功与否。666表示成功，999表示失败
     * @param: @param financialAnaly
     * @param: @return      
     * @return: FinancialAnaly      
     * @throws
     */
    public FinancialAnaly copyFinancialTemplate(FinancialAnaly financialAnaly) {
    	FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		String oldPath = "";
		String newPath = "";
		try {
			String filePath = financialAnaly.getPath();//文件路径
			String template_name = financialAnaly.getTemplateName();//模板在服务器中的别名
			oldPath = filePath+template_name;
			String linshiPath = PropertiesUtil.getProperty("financial_analy_template_temporary_path");
			if (!linshiPath.endsWith("/")) {// 结尾是否以"/"结束
				linshiPath = linshiPath + File.separator;
			}
			File saveDirFile = new File(linshiPath);
	    	if (!saveDirFile.exists()) {
	    		saveDirFile.mkdirs();
	    	}
			newPath = linshiPath+template_name;
			System.err.println("更新目标文件源路径======"+oldPath);
		    System.err.println("更新目标文件路径======="+newPath);
			File oldFile = new File(oldPath); 
			File newFile = new File(newPath); 
			if (oldFile.exists()) { //文件存在时 
				fi = new FileInputStream(oldFile);
				fo = new FileOutputStream(newFile);
				in = fi.getChannel();// 得到对应的文件通道
				out = fo.getChannel();// 得到对应的文件通道
				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
				//关闭流
				fi.close();
				in.close();
				fo.close();
				out.close();
				financialAnaly.setPath(linshiPath);
				financialAnaly.setState(666);
			}else{
				logger.info("复制文件操作出错,错误信息：源文件不存在,即路径指向错误"); 
				financialAnaly.setState(999);
				return financialAnaly;
			}
		} catch (Exception e) {
			logger.info("复制文件操作出错,复制信息：\n源文件："+oldPath+";\n目标文件："+newPath,e); 
			financialAnaly.setState(999);
			return financialAnaly;
		}
		return financialAnaly;
    }
    
    /**
     * 
     * @Title: deleteLinshiFile  
     * @author: WXN
     * @Description: 删除临时文件
     * @param: @param financialAnaly
     * @param: @return      
     * @return: boolean      
     * @throws
     */
    public boolean deleteLinshiFile(FinancialAnaly financialAnaly) {
    	String filePath = financialAnaly.getPath();//文件路径
		String template_name = financialAnaly.getTemplateName();//模板在服务器中的别名
		String fileName = filePath+template_name;
		System.err.println("删除文件路径======"+filePath+template_name);
    	File file = new File(fileName); 
    	// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除 
    	if(file.exists() && file.isFile()) { 
    		if(file.delete()) { 
    			logger.info("删除单个文件" + fileName + "成功！"); 
    			return true; 
    		} else { 
    			logger.info("删除单个文件" + fileName + "失败！"); 
    			return false; 
    		} 
	   } else { 
		   logger.info("删除单个文件失败：" + fileName + "不存在！"); 
		   return false; 
	  } 
   }
    
    /**
	  * 
	  * @描述：更新Excel
	  * @创建时间：  2017年9月22日
	  * @作者：武向楠
	  * @参数： @param var:报表循环变量
	  * @参数： @param yearNum:报表的个数，即要导出几年的报表
	  * @参数： @param FinancialAnaly:财务模板信息，包含模板在服务器路径，名称等等
	  * @参数： @param reportId:报表id
	  * @参数： @param financialMap:报表模板中的类型以及属性名称和所在行数
	  * @return void     
	  * @throws
	  */
   public boolean updateExcel(List<Report> reportList,FinancialAnaly financialAnaly,List<ComonReportPakage.Modle> dataTypelist){
   	boolean resultFlag = false;
   	try {
   		String filePath = financialAnaly.getPath();//文件路径
		String template_name = financialAnaly.getTemplateName();//模板在服务器中的别名
//		String linshiPath = PropertiesUtil.getProperty("financial_analy_template_temporary_path");
//		if (!linshiPath.endsWith("/")) {// 结尾是否以"/"结束
//			linshiPath = linshiPath + File.separator;
//		}
		filePath = filePath+template_name;
       	File file = new File(filePath);
       	System.err.println("模板更新文件路径=================="+filePath);
		//建立数据的输入管道
       	FileInputStream bis = new FileInputStream(file);
       	//初始化一个工作簿
        Workbook wb = null;
        if(template_name.endsWith(EXCEL_XLS)){ //Excel 2003  xls
        	wb = new HSSFWorkbook(bis);  
        } if(template_name.endsWith(EXCEL_XLSX)){  // Excel 2007/2010  xlsx
            wb = new XSSFWorkbook(bis);  
        } 
       	//解析财务模板 确定数据类型以及子属性的行号
       	Map<String, Map<String, Integer>> financialMap = new LinkedHashMap<String, Map<String, Integer>>();
		financialMap = analyFinancialTemplate2(wb,dataTypelist);

		//获得第二个工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
		Sheet sheet = wb.getSheetAt(1);
		//循环报表更新
		//第二步：循环报表list获取报表类型
		for (int i = 0; i < reportList.size(); i++) {
			//报表id
			int reportId = reportList.get(i).getId();
			int yearNum = reportList.size();//几张报表，即导出几年的财务分析。一张报表对应一年财务数据
			if (null != financialMap && 0 != financialMap.size() && !financialMap.isEmpty()) {
				//遍历map中的键  根据key查找报表中的信息
				for (String key : financialMap.keySet()) {
					//根据报表id,报表类型 获取该类型下的所有属性的数据
					Map<String,String> reportDataMap = null;
					try {
						reportDataMap = commonGainReportValue.getSheetValueOther(reportId,CommonGainReportValue.SheetKeyEnum.SHEET_NAME,key, CommonGainReportValue.SpecialKeyEnum.FIELD_NAME);
	            	}catch(Exception e) {
						logger.info("根据报表id,报表类型 获取该类型下的所有属性的数据失败，失败原因======》》》》》》"+e);
					}
					//报表类型 的所有属性的数据 不为空的 情况下
					if (null != reportDataMap && 0 != reportDataMap.size() && !reportDataMap.isEmpty()) {
						//财务报表在该类型（key）下的所有属性以及所在行
						Map<String, Integer> caiwuzishuxingMap = financialMap.get(key);
						if (null != caiwuzishuxingMap && 0 != caiwuzishuxingMap.size() && !caiwuzishuxingMap.isEmpty()) {
							//遍历map中的键  根据key查找报表中的信息
							for (String caiwukey : caiwuzishuxingMap.keySet()) {
								//caiwukey:该数据类型下的属性名称
		        				//caiwuzishuxingMap.get(caiwukey):该数据类型下的属性所在行号
								int hangshu = caiwuzishuxingMap.get(caiwukey);
								String beginData = "0.00";//年初金额
		        				String endData = "0.00";//年末金额
		        				if(reportDataMap.containsKey(caiwukey+"$begin")){
		        					beginData = reportDataMap.get(caiwukey+"$begin");
		        				}
		        				if(reportDataMap.containsKey(caiwukey+"$end")){
		        					endData = reportDataMap.get(caiwukey+"$end");
		        				}
		        				logger.info("数据类型为"+key+"，属性为"+caiwukey+"的数据为：beginData="+beginData+";endData="+endData);
		        				//更新
		        				Row row = sheet.getRow(hangshu);//获取指定行
		        				if(UsedUtil.isNotNull(row)){
		        					//如果报表只有一年的话，应更新第6,7列
			        				if(yearNum == 1){
			        					int j = 2*(i+1)+3;
			        					//需要赋值的列 第6列
		        		    			Cell cell1 = row.getCell(j);
		        		    			//更改excel单元格的值
		        		    			cell1.setCellValue(Double.parseDouble(beginData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+1)+"列单元格值被更新为-----"+Double.parseDouble(beginData));
		        		    			//需要赋值的列 第7列
		        		    			Cell cell2 = row.getCell(j+1);
		        		    			//更改excel单元格的值
		        		    			cell2.setCellValue(Double.parseDouble(endData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+2)+"列单元格值被更新为-----"+Double.parseDouble(endData));
			        				}
			        				//如果报表只有两年的话，应更新第4,5,6,7列
			        				if(yearNum == 2){
			        					int j = 2*(i+1)+1;
			        					//需要赋值的列 第6列
		        		    			Cell cell1 = row.getCell(j);
		        		    			//更改excel单元格的值
		        		    			cell1.setCellValue(Double.parseDouble(beginData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+1)+"列单元格值被更新为-----"+Double.parseDouble(beginData));
		        		    			//需要赋值的列 第7列
		        		    			Cell cell2 = row.getCell(j+1);
		        		    			//更改excel单元格的值
		        		    			cell2.setCellValue(Double.parseDouble(endData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+2)+"列单元格值被更新为-----"+Double.parseDouble(endData));
			        				}
			        				//如果报表只有三年的话，应更新第2,3,4,5,6,7列
			        				if(yearNum == 3){
			        					int j = 2*(i+1)-1;
			        					//需要赋值的列 第6列
		        		    			Cell cell1 = row.getCell(j);
		        		    			//更改excel单元格的值
		        		    			cell1.setCellValue(Double.parseDouble(beginData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+1)+"列单元格值被更新为-----"+Double.parseDouble(beginData));
		        		    			//需要赋值的列 第7列
		        		    			Cell cell2 = row.getCell(j+1);
		        		    			//更改excel单元格的值
		        		    			cell2.setCellValue(Double.parseDouble(endData));
		        		    			System.err.println("第"+(hangshu+1)+"行第"+(j+2)+"列单元格值被更新为-----"+Double.parseDouble(endData));
			        				}
		        				}
							}
						}
					}
				}
			}
		}
    	//强制刷新sheet中的公式 如果没有这一步公式不会生效
    	for (int i = 0; i < wb.getNumberOfSheets(); i++) {
    		//获得工作表对象(ecxel中sheet的编号从0开始,0,1,2,3,....)
			Sheet sheett = wb.getSheetAt(i);
			sheett.setForceFormulaRecalculation(true);
		}
    	bis.close();//关闭文件输入流
        FileOutputStream fos=new FileOutputStream(file);
        wb.write(fos);
        fos.close();//关闭文件输出流
        resultFlag = true;
   	}catch (Exception e) {
   		logger.info("更新财务模板失败，失败原因======》》》》》》"+e);
   		resultFlag = false;
   		return resultFlag;
		}
   	return resultFlag;
   }
    
   public Map<String, Map<String, Integer>> analyFinancialTemplate2(Workbook wb,List<ComonReportPakage.Modle> dataTypelist){
		//返回的结果
		Map<String, Map<String, Integer>> resultMap = new LinkedHashMap<String, Map<String, Integer>>();
       try {
			 //获取第二个sheet页-原始数据 合并报表-固定
	        Sheet sheet = wb.getSheetAt(1);
	        /**
	         * 第一步 获取上传财务模板中的所有报表类型  即sheet页中的第一列以及所在行号
	         */
	        //定义第一列属性的list
	        List<String> cellList = new ArrayList<String>();
	        //定义第一列行号的list
	        List<Integer> cellNumList = new ArrayList<Integer>();
	        //excel行数 并不一定都是有效行 即可能存在空行
	        int lastRowNum = sheet.getLastRowNum();
	        logger.info("sheet表行数为：" + lastRowNum);
	        for(int i=0; i<lastRowNum; i++){
	            Row row = sheet.getRow(i);//取第一行的列
	            if(UsedUtil.isNotNull(row)){
	            	Cell cell = row.getCell(0);//取第一列的单元格
	            	if(UsedUtil.isNotNull(row)){
	                    String cellValue = getCellValue(cell);//取单元格内容
	                    if(UsedUtil.isNotNull(cellValue)){
	                    	cellList.add(cellValue); //第一列所有的属性的list 不包括空的单元格
	                    	cellNumList.add(i);//第一列所有的行号的list 不包括空的单元格  属性与行号一一对应
	                    }
	            	}
	            }
	        }
	        logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列属性list为=========》》》" + cellList);
	        logger.info("第一步>>>>>>>>财务模板第二个sheet页中第一列行号list为=========》》》" + cellNumList);
	        //确定财务模板中的类型表名以及所在行
	        Map<String, Integer> shuxingMap = new LinkedHashMap<String, Integer>();
	        //循环数据类型名称和财务sheet中的属性 当属性相同时 确定该字表所在的行 从而找出该字表所拥有的子属性
	        for (int i = 0; i < dataTypelist.size(); i++) {
	        	String dataTypeName = dataTypelist.get(i).getSheetName();//数据类型名称
	        	//循环财务sheet页中的map
	        	for (int j = 0; j < cellList.size(); j++) {
	        		String cellListN = cellList.get(j);
	        		if(UsedUtil.isNotNull(cellListN)){
	        			if(cellListN == dataTypeName || cellListN.equals(dataTypeName)){
	        				shuxingMap.put(cellListN, cellNumList.get(j));
	        			}
	        		}
				}
			}
	        if (null == shuxingMap || 0 == shuxingMap.size() || shuxingMap.isEmpty()) {
	        	logger.info("解析财务分析模板,获取财务类型以及所在行为空");
	            return resultMap;
			}
	        logger.info("第二步>>>>>>>>数据类型名称以及所在行为=========》》》" + shuxingMap);
	        /**
	         * 第三步 获取财务模板中每一个 报表类型下的所有属性 
	         * 封装结构为 {报表类型1:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},报表类型2:{属性1:行号,属性1:行号,属性1:行号,属性1:行号,...},...}
	         */
	        //找出数据类型名称以及他下面的子属性
	        //找出数据类型名称以及他下面的子属性的位置
	        List<String> temList = new ArrayList<String>();
			List<Integer> temList2 = new ArrayList<Integer>();
	        for (int i = 0; i < cellList.size(); i++) {
				String cellListname = cellList.get(i);
				//遍历map中的键  
				for (String key : shuxingMap.keySet()) {  
				    if(key==cellListname || cellListname.equals(key)){
				    	temList.add(key);
				    	temList2.add(i);
				    }  
				}
			}
	        Map<String, Map<String, Integer>> caiwushuxingMap = new LinkedHashMap<String, Map<String, Integer>>();
	        for (int i = 0; i < temList2.size(); i++) {
	        	int temI = 0;
	        	int temJ = 0;
	        	if(i<temList2.size()-1){
	        		temI = temList2.get(i)+1;
	        		temJ = temList2.get(i+1);
				}else{
					temI = temList2.get(i)+1;
	        		temJ = cellList.size();
				}
//	        	List<Map<String, Integer>> zishuxingtemList = new ArrayList<Map<String, Integer>>();
	        	Map<String, Integer> zishuxingMap = new LinkedHashMap<String, Integer>();
	        	for (int j = temI; j < temJ; j++) {
	        		zishuxingMap.put(cellList.get(j), cellNumList.get(j));
//	        		zishuxingtemList.add(zishuxingMap);
				}
	        	caiwushuxingMap.put(temList.get(i), zishuxingMap);
			}
	        if (null == caiwushuxingMap || 0 == caiwushuxingMap.size() || caiwushuxingMap.isEmpty()) {
	        	logger.info("解析财务分析模板,获取数据类型名称以及其下所有属性名称和所在行号为空");
	            return resultMap;
			}
	        logger.info("第三步>>>>>>>>数据类型名称以及其下所有属性名称和所在行号=========》》》" + caiwushuxingMap);
	        resultMap = caiwushuxingMap;
		} catch (Exception e) {
			logger.info("解析财务分析模板失败，失败原因======》》》》》》"+e);
			return resultMap;
		}
       return resultMap;
	} 
   
   /**
    * 
   * @Title: exportFinancialReport2 
   * @Description: 下载模板
   * @param @param request
   * @param @param response    设定文件 
   * @return void    返回类型 
   * @throws
    */
   @RequestMapping(value="/exportFinancialReport2", method=RequestMethod.GET)
   @Record(operationType="下载模板",
		   operationBasicModule="大中型企业内部评级",
		   operationConcreteModule ="企业财务分析")
  	public void exportFinancialReport2(HttpServletRequest request,HttpServletResponse response) {
		 //主体id
		String financialID = null==request.getParameter("financialID")?"":request.getParameter("financialID").trim();
		//报表开始时间
		String reportStartTime = null==request.getParameter("reportStartTime")?"":request.getParameter("reportStartTime").trim();
		//报表结束时间
		String reportEndTime = null==request.getParameter("reportEndTime")?"":request.getParameter("reportEndTime").trim();
		//报表口径
		String koujing = null==request.getParameter("koujing")?"":request.getParameter("koujing").trim();
	 	if(UsedUtil.isNotNull(financialID)&&UsedUtil.isNotNull(reportStartTime)&&UsedUtil.isNotNull(reportEndTime)&&UsedUtil.isNotNull(koujing)){
	 		//第一步：根据主体id获取到与他关联的报表List,并且list中的所有报表类型id必须一致，要不然会导致导出多张财务报表
 			List<Report> reportList = financialAnalyApi.findReportList(Integer.parseInt(financialID),reportStartTime,reportEndTime,koujing);
 			if (null != reportList && !reportList.isEmpty()) {
 				//由于list中的所有报表类型id必须一致，所以直接去除第一个list中的报表类型id，获取报表类型以及分析财务模板，可以避免多次调用浪费性能
 	            //通过报表类型获取文件路径以及文件名信息
 				FinancialAnaly financialAnaly = financialAnalyApi.getFinancialTemByType(reportList.get(0).getType());
 				String linshiPath = PropertiesUtil.getProperty("financial_analy_template_temporary_path");
 				if (!linshiPath.endsWith("/")) {// 结尾是否以"/"结束
 					linshiPath = linshiPath + File.separator;
 				}
 				financialAnaly.setPath(linshiPath);
 				//导出excel
				exportFinancialTemplate(financialAnaly,request,response);
				//删除临时文件，删除之前判断是否复制成功，没成功不用删除。判断依据为state=666
				deleteLinshiFile(financialAnaly);
 			}
 		}
   }
   
   
}
