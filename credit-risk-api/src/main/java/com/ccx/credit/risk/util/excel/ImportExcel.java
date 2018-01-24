package com.ccx.credit.risk.util.excel;

import java.util.List;
import java.util.Map;

import com.ccx.credit.risk.api.enterprise.EnterpriseReportSheetApi;
import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @Description 导入Excel
 * @author Created by xzd 2017/12/11.
 */
public class ImportExcel {

	/**
	 * 报表数据
	 * @param sheet
	 * @return
	 */
	public static Map<String, Object> excelToReport(EnterpriseReportSheetApi enterpriseReportSheetApi, Integer reportType, Integer sheetId,
			Map<String, Integer> modelIdsMap, Sheet sheet, FormulaEvaluator formulaEvaluator) throws Exception {
		//need to load constructor
		ImportReport report = new ImportReport();

		return report.importReportExcel(enterpriseReportSheetApi, reportType, sheetId, modelIdsMap, sheet, formulaEvaluator);
	}

	/**
	 * 导入现金流数据
	 * @param sheet
	 * @throws Exception
	 */
	public static List<AbsAssetsRepayment> excelToCashFlow(Sheet sheet) throws Exception {
		//need to load constructor
		ImportCashFlow cashFlow = new ImportCashFlow();

		return cashFlow.importCashFlowExcel(sheet);
	}
	
}
