package com.ccx.credit.risk.main;

import com.ccx.credit.risk.api.AbsOperationRecordApi;
import com.ccx.credit.risk.base.BasicController;
import com.ccx.credit.risk.model.AbsOperationRecord;
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
 * 日志操作
 * @author ymj
 */
@Controller
@RequestMapping(value="/record")
public class OperationRecordController extends BasicController{
	private static Logger logger = LogManager.getLogger(OperationRecordController.class);

	/**注入日志操作对象*/
	@Autowired
	AbsOperationRecordApi absOperationRecordApi;


	/**
	 * 跳转日志列表页
	 * @author
	 */
	@GetMapping("/manager")
	@Record(operationType="查看日志列表页",
			operationBasicModule="系统管理",
			operationConcreteModule ="日志管理")
	public String list(){
		return "record/recordList";
	}


	/**
	 * 查询日志列表
	 * @return
	 * @author
	 */
	@RequestMapping("/findAllRecord")
	@ResponseBody
	public Map<String, Object> findAllRecord(HttpServletRequest request){
		//返回结果
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获取请求参数
		Map<String, Object> paramMap = ControllerUtil.requestMap(request);
		//页面信息
		PageInfo<AbsOperationRecord> pages = new PageInfo<AbsOperationRecord>();
		PageHelper.startPage(getPageNum(), getPageSize());

		try {
			List<AbsOperationRecord> allRecordList = absOperationRecordApi.findAllRecord(paramMap);

			pages = new PageInfo<AbsOperationRecord>(allRecordList);

			resultMap.put("pages", pages);
			resultMap.put("code", 200);
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", "日志记录查询失败");
			logger.error("日志记录查询失败", e);
			e.printStackTrace();
		}
		return resultMap;
	}
	
}
