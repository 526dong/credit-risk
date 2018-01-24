package com.ccx.credit.risk.util.excel;

import com.ccx.credit.risk.api.enterprise.EnterpriseReportSheetApi;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportSheet;
import com.ccx.credit.risk.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by xzd
 * 导入报表-Report
 */
public class ImportReport {
	//反射map val
	public static Map<Integer, Object> reportMethod = new HashMap<>();

	/**
	 * 反射提到构造函数中速度将会提升50倍
	 * @throws Exception
	 */
	public ImportReport() throws Exception{
		Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore");

		//数据-map
		Map<Integer, String> reportMap = getReportMap();

		//数据
		Iterator it = reportMap.entrySet().iterator();

		while(it.hasNext()) {
			Map.Entry<Integer, String[]> map = (Map.Entry<Integer, String[]>) it.next();

			Method method = clazz.getDeclaredMethod("set" + map.getValue(), BigDecimal.class);
			reportMethod.put(map.getKey(), method);
		}
	}

	//获取反射数据map
	public static Map<Integer, String> getReportMap(){
		Map<Integer, String> reportMap = new HashMap<>();

		//reportMap put value
		reportMap.put(1, "BeginBalance");
		reportMap.put(2, "EndBalance");

		reportMap.put(4, "BeginBalance");
		reportMap.put(5, "EndBalance");

		return reportMap;
	}

	/**
	 * 导入资产类数据报表
	 * @param sheet Excel
	 * @param formulaEvaluator 公式
	 * errType 101-导入的报表不是资产类财务报表，102-资产类财务报表存在空行，103-资产类财务报表数据格式异常
	 * @return resultMap
	 * @throws Exception
	 */
	public Map<String, Object> importReportExcel(EnterpriseReportSheetApi enterpriseReportSheetApi, Integer reportType, Integer sheetId,
			 Map<String, Integer> modelIdsMap, Sheet sheet, FormulaEvaluator formulaEvaluator){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();
		//数据集合
		List<EnterpriseReportDataStore> reportDataList = new ArrayList<>();
		//sheet判断
		boolean sheetNameFlag = sheet.getSheetName() != null && sheet.getRow(0) != null && sheet.getRow(0).getCell(0) != null;

		if (sheetNameFlag) {
			//通过当前sheet名称查询
			EnterpriseReportSheet enterpriseReportSheet = enterpriseReportSheetApi.selectByReportSonName(reportType, sheet.getSheetName());

			if (enterpriseReportSheet == null) {
				resultMap.put("code", 500);
				resultMap.put("msg", 101);
			} else {
				//判断是否为指定的上传文件
				if (sheetId == 0 || sheetId.equals(enterpriseReportSheet.getId())) {
					Map<String, Object> importReportDataList = getImportReportDataList(sheet, modelIdsMap, formulaEvaluator);

					if (importReportDataList != null && importReportDataList.size() > 0) {
						if (importReportDataList.get("code") != null) {
							String code = String.valueOf(importReportDataList.get("code"));
							if ("200".equals(code)) {
								if (importReportDataList.get("data") != null){
									reportDataList = (List<EnterpriseReportDataStore>) importReportDataList.get("data");
									resultMap.put("code", 200);
									resultMap.put("reportList", reportDataList);
								}
							} else if ("500".equals(code)) {
								resultMap.put("code", 500);
								resultMap.put("msg", importReportDataList.get("msg"));
							}
						}
					}
				} else {
					resultMap.put("code", 500);
					resultMap.put("msg", 101);
				}
			}
		} else {
			resultMap.put("code", 500);
			resultMap.put("msg", 101);
		}

		return resultMap;
	}

	/**
	 * 获取报表数据
	 * @param sheet
	 * @param modelIdsMap
	 * @param formulaEvaluator
	 * @return
	 */
	public Map<String, Object> getImportReportDataList(Sheet sheet, Map<String, Integer> modelIdsMap, FormulaEvaluator formulaEvaluator) {
		//result map
		Map<String, Object> resultMap = new HashMap<>();

		List<EnterpriseReportDataStore> reportDataList = new ArrayList<>();

		//行数据-一行一行循环进行解析
		for (int i = 1;i <= sheet.getLastRowNum();i++) {
			//获取一行数据
			Row row = sheet.getRow(i);

			if (row != null && !"".equals(row.toString().trim())){
				//行长度
				int rowLength = row.getLastCellNum();

				//报表数据-存储前3列数据
				EnterpriseReportDataStore reportDataStore = new EnterpriseReportDataStore();
				//报表数据-存储后3列数据
				EnterpriseReportDataStore reportDataStore2 = new EnterpriseReportDataStore();

				for (int j = 0; j < rowLength; j++) {
					//标题列
					if (j == 0 || j == 3) {
						//列头-带有标题
						Cell cellTitle = row.getCell(j);

						if (cellTitle != null) {
							Map<String, Object> cellTitleMap = getCellTitle(cellTitle, modelIdsMap);

							if ((Boolean) cellTitleMap.get("flag")) {
								if (cellTitleMap.get("data") != null) {
									/*//列标题-去除空格
									String columnTitle = cellTitleMap.get("data").toString().replaceAll("\\s*|\t|\n|\r", "");*/

									String columnTitle = cellTitleMap.get("data").toString();

									//模板中的id
									Integer modelId = modelIdsMap.get(columnTitle);

									if (j == 0) {
										//存在该模板字段-前3列
										reportDataStore.setReportModelId(modelId);
									} else {
										//存在该模板字段-后3列
										reportDataStore2.setReportModelId(modelId);
									}
								}
								continue;
							} else {
								if (j == 3) {
									break;
								} else {
									continue;
								}
							}
						}
					}

					if (j > 3) {
						//后3列
						Cell cellRight = row.getCell(j);

						//反射
						Method methodRight = (Method) reportMethod.get(j);

						if (cellRight != null && !"".equals(cellRight.toString().trim())) {
							//一行报表数据map
							Map<String, Object> reportDataMap2 = setReportVal(cellRight, methodRight, formulaEvaluator, reportDataStore2);

							if (reportDataMap2 != null && !reportDataMap2.isEmpty()) {
								if (reportDataMap2.get("code") != null) {
									String code = String.valueOf(reportDataMap2.get("code"));
									if ("200".equals(code)) {
										if (reportDataMap2.get("reportData") != null) {
											reportDataStore2 = (EnterpriseReportDataStore) reportDataMap2.get("reportData");
										}
									} else if ("500".equals(code)) {
										resultMap.put("code", 500);
										resultMap.put("msg", reportDataMap2.get("msg"));

										return resultMap;
									}
								}
							}
						}
					} else {
						//前3列
						Cell cellLeft = row.getCell(j);

						//反射
						Method methodLeft = (Method) reportMethod.get(j);

						if (cellLeft != null && !"".equals(cellLeft.toString().trim())) {
							//一行报表数据map
							Map<String, Object> reportDataMap = setReportVal(cellLeft, methodLeft, formulaEvaluator, reportDataStore);

							if (reportDataMap != null && !reportDataMap.isEmpty()) {
								if (reportDataMap.get("code") != null) {
									String code = String.valueOf(reportDataMap.get("code"));
									if ("200".equals(code)) {
										if (reportDataMap.get("reportData") != null) {
											reportDataStore = (EnterpriseReportDataStore) reportDataMap.get("reportData");
										}
									} else if ("500".equals(code)) {
										resultMap.put("code", 500);
										resultMap.put("msg", reportDataMap.get("msg"));

										return resultMap;
									}
								}
							}
						}
					}
				}

				if (reportDataStore.getReportModelId() != null) {
					//3列标识
					reportDataStore.setReportSonNo(1);
					reportDataList.add(reportDataStore);
				}

				if (reportDataStore2.getReportModelId() != null) {
					//6列标识
					reportDataStore2.setReportSonNo(2);
					reportDataList.add(reportDataStore2);
				}
			} else {
				resultMap.put("code", 500);
				resultMap.put("msg", 102);
			}
		}

		//返回map处理
		if (reportDataList != null && reportDataList.size() > 0) {
			resultMap.put("code", 200);
			resultMap.put("data", reportDataList);
		} else {
			resultMap.put("code", 500);
			resultMap.put("msg", 102);
		}

		return resultMap;
	}

	/**
	 * 获取列标题并且判断列标题是否和数据库中的模板匹配
	 * @param cell
	 * @param modelIdsMap
	 * @return
	 */
	public Map<String, Object> getCellTitle(Cell cell, Map<String, Integer> modelIdsMap){
		//返回map结果集合
		Map<String, Object> resultMap = new HashMap<>();

		boolean flag = false;

		String val = cell.getStringCellValue();
		//处理*和空格
		String replaceVal = val.replace("*", "").trim();

		if (!replaceVal.isEmpty()) {
			//返回处理后的cell值
			resultMap.put("data", replaceVal);
			//存在该模板字段
			if (!String.valueOf(modelIdsMap.get(replaceVal)).isEmpty()) {
				flag = true;
			}
		}

		resultMap.put("flag", flag);

		return resultMap;
	}

	/**
	 * 获取report中的上期数据和本期数据
	 * @param cell
	 * @param formulaEvaluator
	 * @param reportDataStore
	 */
	public Map<String, Object> setReportVal(Cell cell, Method method, FormulaEvaluator formulaEvaluator, EnterpriseReportDataStore reportDataStore){
		//result jsp map
		Map<String, Object> resultMap = new HashMap<>();

		try {
			//数值类型
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				method.invoke(reportDataStore, new BigDecimal(cell.getNumericCellValue()));
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				method.invoke(reportDataStore, new BigDecimal(cell.getStringCellValue()));
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				method.invoke(reportDataStore, new BigDecimal(formulaEvaluator.evaluate(cell).getNumberValue()));
			}

			resultMap.put("code", 200);
			resultMap.put("reportData", reportDataStore);
		} catch (Exception e) {
			resultMap.put("code", 500);
			resultMap.put("msg", 103);
			e.printStackTrace();
		}

		return resultMap;
	}
}
