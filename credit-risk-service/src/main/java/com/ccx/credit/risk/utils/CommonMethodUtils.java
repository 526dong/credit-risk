package com.ccx.credit.risk.utils;

import com.alibaba.fastjson.JSONArray;
import com.ccx.credit.risk.mapper.enterprise.EnterpriseReportDataStoreMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.enterprise.Enterprise;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportState;
import com.ccx.credit.risk.util.ControllerUtil;
import com.ccx.credit.risk.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xzd on 2017/10/12.
 * service层公共方法
 */
public class CommonMethodUtils {
    /**
     * 公共方法-保存报表数据信息
     * @param request
     * @param reportId 报表id
     * @return
     */
    public static List<EnterpriseReportDataStore> saveCommonReportSon(HttpServletRequest request, String reportId){
        //报表list
        List<EnterpriseReportDataStore> reportList = new ArrayList<>();

        //jsp map 参数集
        Map<String, String[]> parameterMap = request.getParameterMap();

        //循环遍历参数map
        Iterator it = parameterMap.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, String[]> map = (Map.Entry<String, String[]>) it.next();

            //去除非double类型字段
            boolean flag = removeNotDouble(map.getKey());

            if (flag) {
                continue;
            } else {
                //存储报表数据
                EnterpriseReportDataStore reportDataStore = new EnterpriseReportDataStore();

                //参数key
                String key = map.getKey();
                String[] valArray = map.getValue();

                //列数
                String reportSonNo = "";
                //excelName
                String excelName = "";
                //上期
                String beginBalance = "";
                //本期
                String endBalance = "";

                if (valArray.length > 0 && valArray.length == 4) {
                    reportSonNo = valArray[0];
                    excelName = valArray[1];
                    beginBalance = valArray[2];
                    endBalance = valArray[3];

                    //excel name
                    String excelColumnId = excelName.substring(1, excelName.length());
                    if (reportSonNo != "" && "1".equals(reportSonNo)) {
                        //3列
                        reportDataStore.setBeginExcelName("B" + excelColumnId);
                        reportDataStore.setEndExcelName("C" + excelColumnId);
                    } else {
                        //6列
                        reportDataStore.setBeginExcelName("E" + excelColumnId);
                        reportDataStore.setBeginExcelName("F" + excelColumnId);
                    }
                }

                //不为空
                if (!reportId.isEmpty()) {
                    reportDataStore.setReportId(Integer.parseInt(reportId));
                }

                String[] keys = key.split("_");
                String columnId = "";

                if (keys.length > 0) {
                    //列id
                    columnId = keys[keys.length-1];
                }

                reportDataStore.setReportModelId(Integer.parseInt(columnId));

                reportDataStore.setBeginBalance(CommonMethodUtils.formatDecimal(beginBalance));
                reportDataStore.setEndBalance(CommonMethodUtils.formatDecimal(endBalance));

                //创建人
                User user = ControllerUtil.getSessionUser(request);
                if (user != null) {
                    reportDataStore.setCreatorName(user.getLoginName());
                }

                reportDataStore.setCreateDate(new Date());

                reportList.add(reportDataStore);
            }
        }

        return reportList;
    }

    /**
     * 比较报表时间，选择出最新的报表时间
     * @param reportTimeList
     * @param enterpriseId
     */
    public static Map<String, Integer> compareReportTime(List<Map<String, Object>> reportTimeList, Integer enterpriseId) {
        Map<String, Integer> resultMap = new HashMap<>();

        if (reportTimeList != null && reportTimeList.size() > 0) {
            //查到1条记录的时候
            if (reportTimeList.size() == 1) {
                //不想等的话取第一条记录
                int reportId = Integer.parseInt(reportTimeList.get(0).get("reportId").toString());
                resultMap.put("lastestReprotId", reportId);
            } else if (reportTimeList.size() == 2) {
                if (reportTimeList.get(0).get("reportTime") != null && reportTimeList.get(1).get("reportTime") != null) {
                    String reportTime1 = reportTimeList.get(0).get("reportTime").toString();
                    String reportTime2 = reportTimeList.get(1).get("reportTime").toString();
                    //相等的话取合并的年份
                    if (reportTime1.equals(reportTime2)) {
                        if (reportTimeList.get(0).get("cal") != null && reportTimeList.get(1).get("cal") != null) {
                            String cal1 = reportTimeList.get(0).get("cal").toString();
                            String cal2 = reportTimeList.get(1).get("cal").toString();
                            if ("1".equals(cal1)) {
                                int reportId = Integer.parseInt(reportTimeList.get(0).get("reportId").toString());
                                resultMap.put("lastestReprotId", reportId);
                            } else if ("1".equals(cal2)) {
                                int reportId = Integer.parseInt(reportTimeList.get(1).get("reportId").toString());
                                resultMap.put("lastestReprotId", reportId);
                            }
                        }
                    } else {
                        //不相等的话取第一条记录
                        int reportId = Integer.parseInt(reportTimeList.get(0).get("reportId").toString());
                        resultMap.put("lastestReprotId", reportId);
                    }
                }
            }
        } else {
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 更新企业表相关信息
     * @param flag true:新增, false更新
     * @param enterprise
     * @param reportId
     * @param reportTimeList
     */
    public static Map<String, Object> getUpdateEnterprise(boolean flag, Enterprise enterprise, Integer reportId, List<Map<String, Object>> reportTimeList) {
        //result map
        Map<String, Object> resultMap = new HashMap<>();

        //新增的情况下需要更新关联表
        if (flag) {
            Map<String, Integer> mapParam = new HashMap<>();

            mapParam.put("enterpriseId", enterprise.getId());
            mapParam.put("reportId", reportId);

            //关联关系表
            resultMap.put("entRelation", mapParam);
        }

        //获取最新报表
        Map<String, Integer> lastedReportIdMap = compareReportTime(reportTimeList, enterprise.getId());

        if (lastedReportIdMap != null && lastedReportIdMap.get("lastestReprotId") != null) {
            //将最新的报表id更新到企业表中
            enterprise.setReportId(lastedReportIdMap.get("lastestReprotId"));
        } else {
            enterprise.setReportId(reportId);
        }

        //修改企业主体录入状态为未完成-0
        enterprise.setState(0);
        enterprise.setUpdateDate(new Date());

        resultMap.put("enterprise", enterprise);

        return resultMap;
    }

    /**
     * 逻辑删除-reportDataStore中的数据
     * reportId 报表概况id
     */
    public static void updateReportDataStoreState(EnterpriseReportDataStoreMapper reportDataStoreMapper, Integer reportId){
        List<EnterpriseReportDataStore> reportDataList = new ArrayList<>();

        //先查出来数据
        reportDataList = reportDataStoreMapper.findByReportId(reportId);

        if (reportDataList != null && reportDataList.size() > 0) {
            //通过id一条一条进行逻辑删除
            for (EnterpriseReportDataStore reportDataStore:reportDataList) {
                reportDataStoreMapper.updateDeleteFlagById(reportDataStore.getId());
            }
        }
    }

    /**
     * 更新财务报表子表完成状态
     * @param sheetIdsMap sheet id map
     * @param reportId 报表id
     * @param reportType 财务报表类型
     */
    public static List<EnterpriseReportState> getReportSonStateList(Map<String, Integer> sheetIdsMap, Integer reportId, Integer reportType){
        //状态表
        List<EnterpriseReportState> enterpriseReportStateList = new ArrayList<>();

        //循环遍历参数map
        Iterator sheetId = sheetIdsMap.entrySet().iterator();

        while(sheetId.hasNext()) {
            Map.Entry<String, Integer> map = (Map.Entry<String, Integer>) sheetId.next();

            EnterpriseReportState reportState = new EnterpriseReportState();

            reportState.setId(null);
            reportState.setReportId(reportId);
            reportState.setReportType(reportType);
            reportState.setReportSonType(map.getValue());
            //报表状态-已完成
            reportState.setState(1);

            enterpriseReportStateList.add(reportState);
        }

        return enterpriseReportStateList;
    }

    /**
     * 去除非double类型的字段
     * @param str
     * @return
     */
    public static boolean removeNotDouble(String str) {
        if ("id".equals(str) || "reportId".equals(str) || "reportType".equals(str) || "reportSonType".equals(str) || "sheetId".equals(str) || "sheetName".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 将String类型转化为BigDecimal
     * @param str
     * @return
     */
    public static BigDecimal formatDecimal(String str){
        //判断值是否为null和""
        if (!StringUtils.isEmpty(str)) {
            return new BigDecimal(str.replaceAll(",", ""));
        } else {
            return new BigDecimal(0);
        }
    }

    /**
     * 解决url中中文参数乱码问题
     * @param value 需要中文转码的值
     * @return
     */
    public static String getEncodeValue(String value){
        String backStr = "";
        try {
            backStr = new String(value.getBytes("iso-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return backStr;
    }

    /**
     * 从导出目录中找对应的文件路径
     * @param path 目录路径
     * @return 文件路径
     * @throws Exception
     */
    public static String getFilePath(String path, String exportFileName) throws Exception {
        //需要的文件路径
        String filePath = "";

        //读取目录文件路径
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File fileIndex:files){
                String fileIndexName = fileIndex.getName();

                if (!StringUtils.isEmpty(fileIndexName)) {
                    String fileName = fileIndexName.substring(0, fileIndexName.indexOf("."));

                    if (exportFileName.equals(fileName)) {
                        filePath = path + fileIndexName;
                        break;
                    }
                }
            }
        }

        return filePath;
    }

    /**
     * 从导出目录中找对应的文件路径-现金流
     * @param path 目录路径
     * @return 文件路径
     * @throws Exception
     */
    public static String getCashFlowFilePath(String path) throws Exception {
        //需要的文件路径
        String filePath = "";

        //读取目录文件路径
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();

            if (files != null && files.length == 1) {
                filePath = path + files[0].getName();
            }
        }
        return filePath;
    }

}
