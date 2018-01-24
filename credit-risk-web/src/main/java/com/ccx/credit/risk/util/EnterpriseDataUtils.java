package com.ccx.credit.risk.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ccx.credit.risk.api.enterprise.*;
import com.ccx.credit.risk.api.index.ElementApi;
import com.ccx.credit.risk.api.index.IndexApi;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseMapper;
import com.ccx.credit.risk.model.element.ModelElement;
import com.ccx.credit.risk.model.enterprise.*;
import com.ccx.credit.risk.model.index.IndexBean;

/**
 * 加载企业数据 
 * @author xzd
 * @date 2017/7/19
 */
public class EnterpriseDataUtils {
	/**
	 * 加载企业初始化数据：企业性质/一级行业/省 
	 * @param request
	 */
	public static void loadInitData(HttpServletRequest request, 
			EnterpriseApi api, EnterpriseIndustryApi industryApi){
		//加载企业性质
		List<EnterpriseNature> nature = new ArrayList<>(); 
		
		try {
			nature = api.findAllNature();
			
			if (nature != null) {
				request.setAttribute("nature", nature);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//加载企业一级行业
		List<EnterpriseIndustry> industry1 = new ArrayList<>();
		try {
			industry1 = industryApi.findAllIndustryByPid(0);
			
			if (industry1 != null) {
				request.setAttribute("industry1", industry1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过一级行业查询二级行业
	 * @param request
	 * @param industryApi
	 * @return resultMap
	 */
	public static Map<String, Object> loadIndustry(HttpServletRequest request, EnterpriseIndustryApi industryApi){
		Map<String, Object> resultMap = new HashMap<>();
		List<EnterpriseIndustry> industry2 = new ArrayList<>();

		//一级行业id
		String pid = request.getParameter("pid");
		//判空处理
		boolean pidflag = (pid != null && !"".equals(pid));
		
		if (pidflag) {
			try {
				//通过一级行业id查询二级行业列表
				industry2 = industryApi.findAllIndustryByPid(Integer.parseInt(pid));
			
				if (industry2 != null) {
					resultMap.put("industry2", industry2);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		return resultMap;
	}
	
	/**
	 * 企业指标查询
	 * @param indexApi
	 * @param elementApi
	 * @param params
	 * @return indexList
	 */
	public static List<IndexBean> loadIndex(EnterpriseIndustryApi industryApi, IndexApi indexApi, ElementApi elementApi,
			Map<String, Integer> params){
		//二级行业id
		int industry2Id = params.get("industry2Id");
		//企业类型
		int entType = params.get("entType");
		//因素列表
		List<Integer> elementIds = new ArrayList<>();
		//指标列表
		List<IndexBean> indexList = new ArrayList<>();

		Integer modelId = industryApi.getModelIdByIdAndEntType(industry2Id, entType);
		if (null != modelId) {
			//通过modelId查询因素idList
			List<ModelElement> elementList =  elementApi.getListByModelId(modelId);
			if (elementList.size() > 0) {
				for (ModelElement element: elementList) {
					elementIds.add(element.getId());
				}

				//通过因素查询指标
				indexList = indexApi.findNatureIndexByElementIds(elementIds);
			}
		}
		
		return indexList;
	}
	
	/**
	 * 加载企业数据:指标规则
	 * @param enterprise request
	 */
	public static void loadIndexData(HttpServletRequest request, Enterprise enterprise, EnterpriseApi api){
		//指标规则
		Map<String, String> indexMap = new HashMap<>();
		
		try {
			indexMap = api.selectEnterpriseIndexAndRules(enterprise.getId());
			
			if (indexMap != null) {
				if (StringUtils.isNotBlank(indexMap.get("indexIds")) && StringUtils.isNotBlank(indexMap.get("ruleIds"))) {
					String indexIds = indexMap.get("indexIds");
					String ruleIds = indexMap.get("ruleIds");
					
					request.setAttribute("indexIds", indexIds);
					request.setAttribute("ruleIds", ruleIds);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成流水号
	 * 由年月日时分秒+3位随机数
	 * @return cid
	 */
	public static String getCid() {
		//获取现在时间
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timeStr = sdf.format(date);

		//生成随机数
		int randomNum = (int)(Math.random()*900)+100;

		String cid = timeStr + randomNum;
		return cid;
	}

	/**
	 * 下载模板的公共方法
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	public static void downloadReportTemplate(HttpServletResponse response, File file)throws IOException{
		ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(out);

			byte[] buff = new byte[2048];
			int bytesRead ;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

}
