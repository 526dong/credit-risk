package com.ccx.credit.risk.utils;

import com.ccx.credit.risk.mapper.index.IndexFormulaMapper;
import com.ccx.credit.risk.model.index.IndexFormula;
import com.ccx.credit.risk.util.MyRuntimeException;
import com.ccx.credit.risk.util.RateInValidRuntimeException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 企业评级工具类
 * @author sunqi
 *
 */
public class EnterpriseRatingUtils {

	private static Logger logger = LogManager.getLogger("myLog");

	private static final String SPILT_OPERATING_PATTEN = "[,()+*/-]";//分隔运算符


	public static final String[] yearArry = {"最新", "次最新", "次新"};
	/**
	 * 根据财报数据Map和指标计算公式formula 返回指标得分
	 */
	public static Double operatingIndexValueByFormula(int len,
													  String formulaContent,
													  Map<String, Object> reportSheetMap,
													  Map<String, String> formulaMap) {
		Double operationValu = null;
		ArrayList<String> functionList = new ArrayList<>();
		functionList.add("方差");
		functionList.add("根号");

		//将字段换算成数字
		//String reportColumn[] = formulaContent.split(SPILT_OPERATING_PATTEN);
		String[] reportColumn = split(formulaContent);
		for(String c: reportColumn){
			Pattern pBlank = Pattern.compile("\\s*|\t|\r|\n");
			Matcher mBlan = pBlank.matcher(c);
			//c = mBlan.replaceAll("");

			if(!"".equals(c)){
				String column = c.trim();
				Pattern pConstant = Pattern.compile("^\\d+(\\.\\d+)?$");
				Matcher mConstant = pConstant.matcher(column);

				//找到常量引用
				if (mConstant.find()) {
					continue;
				}

				//找到函数引用
				if (functionList.contains(column)) {
					continue;
				}

				//找到公式引用
				boolean continueFlag = false;
				//公式
				String formula = formulaMap.get(column+":"+len);
				if (null != formula) {
					int formulaYearLen = new Integer(formulaMap.get(column));
					if (formulaYearLen < len) {
						throw new RateInValidRuntimeException("引用的公式："+column+"定义年份："+formulaYearLen+"  不满足评级的年份："+len);
					}

					continueFlag = true;
					logger.info("/******************************/进入嵌套公式引用："+column);
					//调用递归计算
					formulaContent = formulaContent.replace(column, operatingIndexValueByFormula(len, formula, reportSheetMap, formulaMap).toString());
				}
				//递归后的无用循环结束
				if (continueFlag) {
					//因为公式虽然被替换了但是当前for循环还停在这一部
					logger.info("/******************************/退出嵌套公式替换："+column);
					continue;
				}

				//正常的取值
				String yearSheetName = getReportYearSheet(column);
				String columnName = getcolumnName(column);
				Object value =  getColumnValue(yearSheetName, columnName, reportSheetMap);
				formulaContent = formulaContent.replace(column, value.toString());
				logger.info("替换后的公式："+formulaContent);
			}
		}

		//去空格
		Pattern pBlank = Pattern.compile("\\s*|\t|\r|\n");
		Matcher mBlan = pBlank.matcher(formulaContent);
		formulaContent = mBlan.replaceAll("");
		//函数拦截
		formulaContent = operatingFunctionValueByFormula(formulaContent, functionList);

		//开始计算
		operationValu = calJs(formulaContent);
		logger.info(formulaContent);
		logger.info(operationValu.toString());
		return operationValu;
	}

	/**
	 * 根据公式里字段 获取报表年份,V1_sheet,V2_sheet
	 */
	public static String getReportYearSheet(String column){
		String name = null;
		int end1 = column.indexOf("_上期");
		int end2 = column.indexOf("_本期");

		//开始字段
		if(end1 > -1) {
			name = column.substring(0, end1);
		} else if(end2 > -1) {
			//结束字段
			name = column.substring(0, end2);
		} else if(end1 == -1 && end2 == -1) {
			throw new MyRuntimeException("公式中未发现时间字段上期或上期！");
		}

		return name;
	}

	/**
	 * 整理数据库存储公式里字段 解析成get方法名
	 */
	public static String getcolumnName(String column){
		String name = null;
		int end1 = column.indexOf("_上期");
		int end2 = column.indexOf("_本期");

		//开始字段
		if(end1 > -1) {
			name = column.substring(end1+1);
		} else if(end2 > -1) {
			//结束字段
			name = column.substring(end2+1);
		} else if(end1 == -1 && end2 == -1) {
			throw new MyRuntimeException("公式中未发现时间字段上期或上期！");
		}

		return name;
	}

	/**
	 *  根据年，表名，字段名，条件获取字段的值
	 */
	public static Object getColumnValue(String yearSheetName, String columnName, Map<String, Object> reportSheetMap){
		Object value = null;

		Map<String, String> reportColumnMap = (Map<String, String>) reportSheetMap.get(yearSheetName);
		if (null == reportColumnMap || reportColumnMap.size() == 0) {
			throw new RateInValidRuntimeException("公式["+yearSheetName+"_"+columnName+"]中的模板定义["+yearSheetName+"]未找到");
		}

		value = reportColumnMap.get(columnName);
		if (null == value) {
			//throw new RateInValidRuntimeException("公式["+yearSheetName+"_"+columnName+"]中的字段定义["+columnName+"]未找到");
			value = new Double("0.00");
		}

		return value;
	}

	/**
	 * 计算公式中的函数的值
	 */
	private static String operatingFunctionValueByFormula(String formula, List<String> functionList) {
		FindFunction find = null;
		String content = null;

		try {
			//遍历每个函数
			for (String fun : functionList) {
				//查找公式中是否出现次函数
				while (null != (find = getFunctionIndex(formula, fun, fun.length()))) {
					String functionName = null;
					String functionValue = null;
					Double value = null;

					functionName = find.getFuntionName();
					content = formula.substring(find.getFunctioinBegin() + fun.length() + 1, find.getFunctionEnd() - 1);
					for (String f : functionList) {
						int n = content.indexOf(f);
						if (n > -1) {
							//公式递归
							logger.info("进入嵌套函数引用："+content.substring(n));
							content = operatingFunctionValueByFormula(content, functionList);
						}
					}

					if ("MATH_POW_2".equals(fun)) {
						value = calJs(content);
						functionValue = new Double(Math.pow(value.doubleValue(), 1 / 2.0)).toString();
					} else if ("MATH_RAN".equals(fun)) {
						functionValue = calMathVan(content);
					}

					formula = formula.replace(functionName,functionValue);
					logger.info("函数替换后的公式："+formula);
				}

			}
		} catch (Exception e) {
			//logger.error("计算函数："+functionList+"异常:，参数", e, content);
			throw new RateInValidRuntimeException("计算函数值异常！函数："+functionList+"参数："+content);
		}
		return formula;
	}

	/*
	* 返回函数出现的后的函数的位置fun，【（】的位置，【）】的位置的
	*/
	private static FindFunction getFunctionIndex(String formula, String split, int offset) {
		FindFunction find = null;
		int i = formula.indexOf(split);

		if (i > -1) {
			find = new FindFunction();
			find.setFunctioinBegin(i);
			//查找括号的计数，
			int n = 0;
			for (int j=i+offset;j<formula.length(); j++) {
				char c = formula.charAt(j);
				if ("(".equals(String.valueOf(c))) {
					n++;
				}
				char c1 = formula.charAt(j);
				if (")".equals(String.valueOf(c1))) {
					n--;
					if (0 == n) {
						find.setFuntionName(formula.substring(i, j+1));
						find.setFunctionEnd(j+1);
						break;
					}
				}
			}
		}

		return find;
	}

	/*
	* 返回一段数学表达最终的表达式
	*/
	private static String getElementsByformula(String formula) {
		FindFunction find = null;
		String content = null;

		//括号优先级最高，先算括号
		while (null != (find = getFunctionIndex(formula, "(", 0))) {
			content = formula.substring(find.getFunctioinBegin() + 1, find.getFunctionEnd() - 1);

			boolean flag = false;
			if (content.indexOf("(") > -1) {
				//内容中还包含（
				flag = true;
				logger.info("进入括号嵌套/******************/");
				formula = formula.replace(find.getFuntionName(), getElementsByformula(content));
			}
			if (flag) {
				continue;
			}


			int i;
			for (i=0; i<content.length(); i++) {
				//乘除法优先级最高
				if ("*".equals(String.valueOf(content.charAt(i))) || "/".equals(String.valueOf(content.charAt(i)))) {
					String front = null;
					String after = null;
					String opt = null;

					opt = content.substring(i, i+1);
					//找到前一个数字
					int j;
					for (j=i-1; j>=0; j--) {
						if ("+".equals(String.valueOf(content.charAt(j)))
								|| "-".equals(String.valueOf(content.charAt(j)))
								|| "*".equals(String.valueOf(content.charAt(j)))
								|| "/".equals(String.valueOf(content.charAt(j)))) {
							front = content.substring(j+1, i);
							break;
						}
					}
					if (j <= -1) {
						front = content.substring(0, i);
					}
					System.out.print("\n合并运算："+front);

					//找到后一个数字
					for (j=i+1; j<content.length(); j++) {
						if ("+".equals(String.valueOf(content.charAt(j)))
								|| "-".equals(String.valueOf(content.charAt(j)))
								|| "*".equals(String.valueOf(content.charAt(j)))
								|| "/".equals(String.valueOf(content.charAt(j)))) {
							after = content.substring(i+1, j);
							break;
						}
					}
					if (j >= content.length()) {
						after = content.substring(i+1, content.length());
					}
					System.out.print(" "+opt+" "+after);

					String cal = front+opt+after;
					formula = formula.replace(cal, calJs(cal).toString());
					logger.info("\n合并后的公式："+formula);

				}
			}
			//没有优先级的替换括号,找到和外层相同的括号
			FindFunction f = getFunctionIndex(formula, "(", 0);
			if (null != f) {
				formula = formula.replace(f.getFuntionName(), calJs(content).toString());
			}
			logger.info("合并后的公式："+formula);
		}

		return formula;
	}

	/*
	* 计算方差
	*/
	private static String calMathVan(String formula) {
		double sum = 0.0;
		double avg = 0.0;
		double avn = 0.0;
		List<Double> list = new ArrayList<>();
		String[] elements = formula.split(",");

		for (String e: elements) {
			String s = calJs(e).toString();
			sum += Double.parseDouble(s);
			list.add(new Double(s));
		}

		avg = sum / list.size();
		for (Double d: list) {
			avn += Math.pow(Math.abs(avg - d.doubleValue()), 2);
		}

		return avn/list.size()+"";
	}

	/*
	* 计算js值
	*/
	private static Double calJs(String content) {
		Double operationValu = null;
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

		//开始计算
		try {
			String sum = jse.eval(content).toString();
			if ("Infinity".equals(sum)) {
				throw new MyRuntimeException("分母为零！");
			}
			if ("NaN".equals(sum)) {
				throw new MyRuntimeException("结果不正常！");
			}
			operationValu = new Double(sum);
		} catch (Exception e) {
			//logger.error("公式运算非法:", e);
			throw new MyRuntimeException("公式运算非法【评级公式字段值可能为0】");
		}
		return operationValu;
	}

	/*
	* 查找函数返回类
	*/
	private static class FindFunction {
		//函数的内容
		String funtionName;

		//函数的开始
		int functioinBegin;

		//函数的结束
		int functionEnd;

		public String getFuntionName() {
			return funtionName;
		}

		public void setFuntionName(String funtionConten) {
			this.funtionName = funtionConten;
		}

		public int getFunctioinBegin() {
			return functioinBegin;
		}

		public void setFunctioinBegin(int functioinBegin) {
			this.functioinBegin = functioinBegin;
		}

		public int getFunctionEnd() {
			return functionEnd;
		}

		public void setFunctionEnd(int functionEnd) {
			this.functionEnd = functionEnd;
		}
	}
	//获取所有公式
	public static Map<String, String> getAllCacheFormula(IndexFormulaMapper formulaMapper) {
		HashMap<String, String> formulaMap = new HashMap<>();
		try {
			pageGet(1, formulaMap, formulaMapper);
		} catch (Exception e) {
			logger.error("公式列表查询失败：", e);
		}
		return formulaMap;
	}

	//查询公式
	public static void pageGet(int pageNo, Map<String, String> formulaMap, IndexFormulaMapper formulaMapper) {
		PageHelper.startPage(pageNo, 1000);
		List<IndexFormula> formulaList = formulaMapper.getPageList(null ,null);
		PageInfo pageInfo = new PageInfo(formulaList);

		for (IndexFormula formula: formulaList) {
			if (null != formula.getYear()) {
				formulaMap.put(formula.getFormulaName()+":"+formula.getYear(), formula.getFormulaContent());
			} else {
				formulaMap.put(formula.getFormulaName(), formula.getYearLen()+"");
			}
		}
		if (pageInfo.getPageNum() < pageInfo.getPages()) {
			pageGet(++pageNo, formulaMap, formulaMapper);
		}

	}

	/**
	 * 公式切分
	 * @param formulaContent
	 * @return
	 */
	private static String[] split(String formulaContent){
		List<String > res=new ArrayList<>();
		//String[] strs = formulaContent.split(" , | ( | ) |\\ + |\\ * | / | - ");
		String[] strs = formulaContent.split("( , )|( \\+ )|( - )|( / )|( \\* )|( \\( )|( \\) )");
		for(String str :strs){
			if(",".equals(str)||"(".equals(str)||")".equals(str)||"+".equals(str)||"-".equals(str)||"*".equals(str)||"/".equals(str)){
				continue;
			}
			res.add(str);
		}
		String[] d=new String[res.size()];
		return res.toArray(d);
	}

	public static void main(String[] args) {
		//"".replace();
		System.out.println(calJs(" ( 1 * 1 ) / 2.5"));
	}
}
