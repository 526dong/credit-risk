package com.ccx.credit.risk.util.excel;

import com.ccx.credit.risk.model.asset.AbsAssetsRepayment;
import com.ccx.credit.risk.util.MyRuntimeException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 导入现金流-cashFlow
 * @author Created by xzd on 2017/12/11.
 */
public class ImportCashFlow {
	/**
	 * reflect map val
	 */
	public static Map<Integer, Object> cashFlowMethod = new HashMap<>();

	/**
	 * not need file
	 */
	public static String DATA_FILE_IMPORT_ERR_TYPE_NOT_NEED_FILE = "101";
	/**
	 * 费用收入=本金+利息收入 ->  RepaymentCost=RepaymentAmount+RepaymentInterest
	 */
	public static String DATA_FILE_IMPORT_ERR_TYPE_DATA_NOT_RIGHT = "102";
	/**
	 * data type
	 */
	public static String DATA_FILE_IMPORT_ERR_TYPE_DATA_TYPE = "103";
	/**
	 * date
	 */
	public static String DATA_FILE_IMPORT_ERR_TYPE_DATE = "104";

	/**
	 * 反射提到构造函数中速度将会提升50倍
	 * @throws Exception
	 */
	public ImportCashFlow() throws Exception{
		Class<?> clazz = Class.forName("com.ccx.credit.risk.model.asset.AbsAssetsRepayment");

		//reflect data-map
		Map<Integer, String> cashFlowMap = getCashFlowMap();
		Iterator it = cashFlowMap.entrySet().iterator();

		while(it.hasNext()) {
			Map.Entry<Integer, String[]> map = (Map.Entry<Integer, String[]>) it.next();
			Integer key = map.getKey();
			Method method = null;

			if (key == 0) {
				method = clazz.getDeclaredMethod("set" + map.getValue(), String.class);
			} else {
				method = clazz.getDeclaredMethod("set" + map.getValue(), BigDecimal.class);
			}

			cashFlowMethod.put(map.getKey(), method);
		}
	}

	/**
	 * get reflect data map
	 * @return
	 */
	public Map<Integer, String> getCashFlowMap(){
		Map<Integer, String> cashFlowMap = new HashMap<>();

		//cashFlowMap put value
		cashFlowMap.put(0, "RepaymentDate");
		cashFlowMap.put(1, "RepaymentAmount");
		cashFlowMap.put(2, "RepaymentInterest");
		cashFlowMap.put(3, "RepaymentCost");

		return cashFlowMap;
	}

	/**
	 * get column title map
	 * @return
	 */
	public Map<Integer, String> getCashFlowTitleMap(){
		Map<Integer, String> cashFlowTitleMap = new HashMap<>();

		//cashFlowTitleMap put value
		cashFlowTitleMap.put(0, "支付日");
		cashFlowTitleMap.put(1, "本金");
		cashFlowTitleMap.put(2, "利息收入");
		cashFlowTitleMap.put(3, "费用收入");

		return cashFlowTitleMap;
	}

	/**
	 * 导入现金流
	 * @param sheet Excel
	 * errType 101-导入的文件不是现金流，102-现金流存在空行，103-现金流数据格式异常
	 * @return resultMap
	 * @throws Exception
	 */
	public List<AbsAssetsRepayment> importCashFlowExcel(Sheet sheet){
		//cash flow data list
		List<AbsAssetsRepayment> cashFlowDataList = new ArrayList<>();
		//title row
		Row row0 = sheet.getRow(0);
		//first row in sheet
		boolean sheetRow0Flag = sheet.getSheetName() != null && row0 != null && row0.getCell(0) != null;

		if (sheetRow0Flag) {
			//judge column title
			for (int i = 0; i < row0.getLastCellNum(); i++) {
				//one column in row
				Cell cell = row0.getCell(i);
				if (cell == null || "".equals(cell.toString())){
					//throw new MyRuntimeException("导入文件标题行第"+(i+1)+"列为空！");
					throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_NOT_NEED_FILE);
				}
				//column
				String cellValue = getCashFlowTitleMap().get(i);
				if (cellValue == null || !cell.toString().contains(cellValue)) {
					//throw new MyRuntimeException("导入文件不是现金流文件！");
					throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_NOT_NEED_FILE);
				}
			}

			//store repay date list
			List<String> dateList = new ArrayList<>();

			//cycle row and cell to get cell value
			for (int j = 1; j <= sheet.getLastRowNum(); j++) {
				//row data
				Row row = sheet.getRow(j);
				//deal row null -> jump loop
				if (row == null || "".equals(row.toString())) {
					continue;
				}
				AbsAssetsRepayment repayment = new AbsAssetsRepayment();
				//column data
				for (int k = 0; k < row.getLastCellNum(); k++) {
					//column
					Cell cell = row.getCell(k);
					//deal column null
					if (cell == null || "".equals(cell.toString())) {
						throw new MyRuntimeException("列值为空");
					}
					//reflect
					Method method = (Method) cashFlowMethod.get(k);
					//entity receive
					setCashFlowVal(cell, method, repayment, dateList);
				}
				//校验应还费用=本金+利息
				validateRepayment(repayment, j);
				//load in list
				cashFlowDataList.add(repayment);
			}
			//校验支付日期
			if (dateList != null && dateList.size() > 0) {
				validateRepayDate(dateList);
			}

		} else {
			//throw new MyRuntimeException("导入文件标题行为空！");
			throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_NOT_NEED_FILE);
		}

		return cashFlowDataList;
	}

	/**
	 * 获取现金流数据
	 * @param cell
	 * @param repayment
	 */
	public void setCashFlowVal(Cell cell, Method method, AbsAssetsRepayment repayment, List<String> dateList){
		try {
			//数值类型
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(cell)) {
					//非线程安全
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					method.invoke(repayment, sdf.format(cell.getDateCellValue()));
					dateList.add(sdf.format(cell.getDateCellValue()));
				} else {
					method.invoke(repayment, new BigDecimal(String.valueOf(cell.getNumericCellValue())));
				}
			} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				method.invoke(repayment, new BigDecimal(cell.getStringCellValue()));
			} else {
				//throw new MyRuntimeException("导入文件数据类型异常！");
				throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_DATA_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验应还费用=本金+利息
	 * @param repayment
	 * @param rowNum 抛出报错使用
	 * @return
	 */
	public void validateRepayment(AbsAssetsRepayment repayment, Integer rowNum) {
		//应还费用
		BigDecimal repaymentCost = repayment.getRepaymentCost();
		//本金
		BigDecimal repaymentAmount = repayment.getRepaymentAmount();
		//利息
		BigDecimal repaymentInterest = repayment.getRepaymentInterest();
		//本金+利息
		BigDecimal add = repaymentAmount.add(repaymentInterest);
		if (repaymentCost.compareTo(add) != 0) {
			//应还费用!=本金+利息，抛出
			throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_DATA_NOT_RIGHT + "," + rowNum);
		}
	}

	/**
	 * 校验支付日期
	 * @param dateList
	 */
	public void validateRepayDate(List<String> dateList){
		//store year and month list
		List<Integer> list = new ArrayList<>(100);
		//store year and month map
		Map<Integer, Integer> map = new HashMap<>(100);
		Iterator<String> iterator = dateList.iterator();
		while (iterator.hasNext()) {
			String next = iterator.next();
			if (next.contains("-")) {
				//去掉-
				String replace = next.replace("-", "");
				//至取到年月
				String finalStr = replace.substring(0, replace.length() - 2);
				Integer dateVal = Integer.parseInt(finalStr);
				list.add(dateVal);
				map.put(dateVal, dateVal);
			}
		}
		//只有一条记录时
		if (list.size() == 1) {
			return;
		}
		//map的长度小于list的长度时说明存在相同的月份
		if (map.size() < list.size()) {
			//抛出异常，支付日期中存在同一个月份的日期
			throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_DATE+",1");
		}
		//随着循环变化的中间变量
		Integer tempDate = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			Integer myDate = list.get(i);
			if (myDate.compareTo(tempDate) > 0) {
				tempDate = myDate;
			} else {
				//抛出异常，支付日期
				throw new MyRuntimeException(DATA_FILE_IMPORT_ERR_TYPE_DATE+",2");
			}
		}
	}
}
