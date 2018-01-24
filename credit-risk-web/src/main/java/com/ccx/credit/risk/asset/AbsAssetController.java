package com.ccx.credit.risk.asset;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccx.credit.risk.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ccx.credit.risk.api.asset.AbsAssetApi;
import com.ccx.credit.risk.api.asset.AbsAssetEnhanceCreditApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.asset.AbsAsset;
import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;
import com.ccx.credit.risk.model.asset.AssetCount;
import com.ccx.credit.risk.util.excel.DownloadUtil;
import com.ccx.credit.risk.util.excel.ImportExcel;
import com.ccx.credit.risk.utils.CommonMethodUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @Description 资产
 * @author Created by xzd on 2017/12/6.
 */
@Controller
@RequestMapping("/asset")
public class AbsAssetController extends BasicController{
	private static Logger logger = LogManager.getLogger(AbsAssetController.class);

	@Autowired
	private AbsAssetApi absAssetApi;

	@Autowired
	private AbsAssetEnhanceCreditApi enhanceCreditApi;

	/**
	 * 资产列表
	 * @return
	 */
	@GetMapping("/list")
	public ModelAndView list() {
		return new ModelAndView("asset/absAssetList");
	}

	/**
	 * 查询资产列表
	 * @param request
	 * @param assetType
	 * @return
	 */
	@PostMapping("/findAll")
	@ResponseBody
	@Record(operationType="查询资产列表",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="资产管理")
	public JsonResult findAll(HttpServletRequest request, @RequestParam(required = true) Integer assetType) {
		//获取查询条件
		Map<String,Object> params = ControllerUtil.requestMap(request);
		//资产类型：0-租赁债权，1-保理债权，2-贷款债权
		params.put("type", assetType);
		//分页
		PageInfo<AbsAsset> pages = new PageInfo<AbsAsset>();
		PageHelper.startPage(getPageNum(),getPageSize());
		try {
			pages = absAssetApi.getPageList(params);
			return JsonResult.ok(pages);
		} catch (Exception e) {
			logger.error("资产创建报错：资产列表分页查询失败：", e);
			return JsonResult.error("资产列表分页查询失败");
		}
	}

	/**
	 * 跳转资产创建/更新
	 * @param request
	 * assetType 资产类型：0-租赁债权，1-保理债权，2-贷款债权
	 * @return
	 */
	@GetMapping("/addOrUpdate")
	@Record(operationType="跳转资产创建页面",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="资产创建")
	public ModelAndView addOrFindData(HttpServletRequest request) {
		//资产类型
		request.setAttribute("assetType", request.getParameter("assetType"));
		//方法类型
		request.setAttribute("method", request.getParameter("method"));
		//资产id
		String id = request.getParameter("id");
		//增信措施-大类/祖父节点
		request.setAttribute("gpName", enhanceCreditApi.findAllByPid(0));

		//更新或查看
		if (!StringUtils.isEmpty(id) && !"0".equals(id)) {
			request.setAttribute("id", id);

			try {
				//资产信息
				absAssetApi.getById(request, Integer.parseInt(id));
			} catch (Exception e) {
				logger.error("资产创建报错：通过资产id查询资产信息异常", e);
			}
		}

		return new ModelAndView("asset/absAssetAddOrUpdate");
	}

	/**
	 * 查询所有评级企业
	 * @return
	 */
	@PostMapping("/findAllRateEnt")
	@ResponseBody
	public JsonResult findAllRateEnt() {
		try {
			//所有评级企业
			List<Map<String, Object>> allRateEnt = absAssetApi.findAllRateEnt();
			return JsonResult.ok(allRateEnt);
		} catch (Exception e) {
			logger.error("资产创建报错：评级企业查询异常", e);
			return JsonResult.error("评级企业查询失败！");
		}
	}

	/**
	 * 查询所有资产业务类型
	 * @return
	 */
	@PostMapping("/findAllBusinessType")
	@ResponseBody
	public JsonResult findAllBusinessType(@RequestParam(required = true) Integer assetType) {
		try {
			//所有资产业务类型
			List<Map<String, Object>> allBusinessType = absAssetApi.findAllBusinessType(assetType);
			return JsonResult.ok(allBusinessType);
		} catch (Exception e) {
			logger.error("资产创建报错：评级企业查询异常", e);
			return JsonResult.error("评级企业查询失败！");
		}
	}

	/**
	 * 查询增信措施子节点
	 * @return
	 */
	@PostMapping("/getEnhanceCredit")
	@ResponseBody
	public JsonResult getEnhanceCredit(@RequestParam(required = true) Integer pid){
		try {
			//增信措施子节点
			return JsonResult.ok(enhanceCreditApi.findAllByPid(pid));
		} catch (Exception e) {
			logger.error("资产创建报错：增信措施子节点查询异常", e);
			return JsonResult.error("增信措施子节点查询失败！");
		}
	}

	/**
	 * 下载现金流模板
	 * @param response
	 */
	@GetMapping("/downloadCashFlow")
	public void downloadCashFlow(HttpServletResponse response) {
		try {
			//文件路径
			String filePath = CommonMethodUtils.getCashFlowFilePath(PropertiesUtil.getProperty("asset_cash_flow_template_path"));
			if (filePath != null) {
				//调用共用的下载方法
				DownloadUtil.download(filePath, "现金流模板.xlsx", response);
			}
		} catch (Exception e) {
			logger.error("资产创建报错：下载现金流模板失败:", e);
		}
	}

	/**
	 * 导入现金流
	 * @param excelFile
	 * @return
	 */
	@PostMapping("/importCashFlow")
	@ResponseBody
	public String importCashFlow(@RequestParam("excelFile") MultipartFile excelFile) throws UnsupportedEncodingException {
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//repayment data list
		List<AbsAssetsRepayment> repaymentList = new ArrayList<>();
		//null deal
		if (excelFile == null || excelFile.isEmpty()) {
			logger.error("资产创建报错：上传Excel文件报错，错误原因============>没有上传文件");
			resultMap.put("code", 401);
			return JSON.toJSONString(resultMap);
		}

		try {
			//init one workbook
			Workbook wb = null;
			try {
				wb = new HSSFWorkbook(excelFile.getInputStream());
			} catch (Exception e) {
				wb = new XSSFWorkbook(excelFile.getInputStream());
				e.printStackTrace();
			}

			//get wanted data
			repaymentList = ImportExcel.excelToCashFlow(wb.getSheetAt(0));
			resultMap.put("code", 200);
			resultMap.put("data", repaymentList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof MyRuntimeException) {
				resultMap.put("code", e.getMessage());
				logger.error("资产创建报错："+e.getMessage());
			} else {
				resultMap.put("code", 500);
				logger.error("资产创建报错："+e);
			}
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 校验资产名称唯一
	 * @param name
	 * @return
	 */
	@PostMapping("/validateName")
	@ResponseBody
	public JsonResult validateName(@RequestParam(required = true) String name){
		//当前name在数据库中是否存在
		int count = absAssetApi.validateName(name);
		return (count <= 0) ? JsonResult.ok(null) : JsonResult.error("资产名称已存在，请重新添加！");
	}

	/**
	 * 校验资产编号唯一
	 * @param code
	 * @return
	 */
	@PostMapping("/validateCode")
	@ResponseBody
	public JsonResult validateCode(@RequestParam(required = true) String code){
		//当前code在数据库中是否存在
		int count = absAssetApi.validateCode(code);
		return (count <= 0) ? JsonResult.ok(null) : JsonResult.error("资产编号已存在，请重新添加！");
	}

	/**
	 * 保存资产
	 * @param asset
	 * @return
	 */
	@PostMapping("/doAddOrUpdate")
	@ResponseBody
	@Record(operationType="保存资产",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="资产创建")
	public JsonResult doAddOrUpdate(String asset, String baseEnt, String enhanceEnt, String enhanceCredit, String cashFlow) {
		try {
			absAssetApi.addOrUpdate(asset, baseEnt, enhanceEnt, enhanceCredit, cashFlow);
			return JsonResult.ok(null);
		} catch (Exception e) {
			logger.error("资产创建报错：资产保存异常：", e);
			return JsonResult.error("资产保存失败！");
		}
	}

	/**
	 * 跳转资产查看详情
	 * @param request
	 * assetType 资产类型：0-租赁债权，1-保理债权，2-贷款债权
	 * @return
	 */
	@GetMapping("/detail")
	@Record(operationType="查看资产详情",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="资产详情")
	public ModelAndView detail(HttpServletRequest request) {
		//资产类型
		request.setAttribute("assetType", request.getParameter("assetType"));
		//资产id
		String id = request.getParameter("id");
		//空判断
		if (StringUtils.isEmpty(id)) {
			throw new RuntimeException("资产创建报错：资产id为空");
		}
		//查看
		try {
			//资产信息
			absAssetApi.getById(request, Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("资产创建报错：通过资产id查询资产信息异常");
		}

		return new ModelAndView("asset/absAssetDetail");
	}

	/**
	 * 逻辑删除资产信息
	 * @param id
	 * @return
	 */
	@PostMapping ("/delete")
	@ResponseBody
	@Record(operationType="删除资产",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="资产管理")
	public JsonResult delete(@RequestParam(required = true) Integer id) {
		try {
			absAssetApi.deleteById(id);
			return JsonResult.ok(null);
		} catch (Exception e) {
			logger.error("资产创建报错：资产删除异常", e);
			return JsonResult.error("资产删除失败！");
		}
	}
	
	/**
	 * 跳转页面：资产风险管理-统计分析
	 * @return
	 */
	@GetMapping("/analyseCount")
	@Record(operationType="查看资产风险统计",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="查看")
	public ModelAndView analyseCount() {
		return new ModelAndView("asset/analyseCount");
	}
	
	/**
	 * 根据资产类型和统计维度统计笔数等分布
	 * @param assetCount
	 * @return
	 */
	@PostMapping ("/analyseDimCount")
	@ResponseBody
	public Map<String, Object> analyseCountAll(@Param("AssetCount")AssetCount assetCount) {
		Map<String, Object> result = absAssetApi.analyseDimCount(assetCount);
		return result;
	}
	
	/**
	 * 根据资产类型统计资产基本信息
	 * @param assetCount
	 * @return
	 */
	@PostMapping ("/assetMessCount")
	@ResponseBody
	@Record(operationType="统计资产信息",
			operationBasicModule="资产风险管理",
			operationConcreteModule ="查看")
	public Map<String, Object> assetMessCount(@Param("AssetCount")AssetCount assetCount) {
		Map<String, Object> result = absAssetApi.assetMessCount(assetCount);
		return result;
	}
}
