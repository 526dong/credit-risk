package com.ccx.credit.risk.utils;

import com.ccx.credit.risk.manager.report.ComonReportPakage;
import com.ccx.credit.risk.model.enterprise.EnterpriseReportDataStore;
import com.ccx.credit.risk.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 财务报表导出
 * @author zhaodongXie
 * @date 2016/10/31
 */
public class ExportReportExcel {
    public void doReportExcel(OutputStream out, Workbook wb, List<ComonReportPakage.Values> reportDataList, Integer reportIndex){
        //excel sheet number
        Integer sheetNum = wb.getNumberOfSheets();
        //null deal
        if (sheetNum == null) {
            throw new RuntimeException("报表模块报错：导出报表模板为空");
        }
        if (reportIndex == 0) {
            //reportDataList null的话就是下载整套报表模板
            if (reportDataList != null) {
                //循环Excel中的sheet
                for (int i = 0;i < sheetNum;i++) {
                    Sheet sheet = wb.getSheetAt(i);
                    //循环报表数据放入到Excel中
                    putReportDataToExcel(reportDataList, i, sheet);
                }
            }
        } else {
            //reportIndex!=0的话就是下载单个sheet报表模板
            Sheet sheet = wb.getSheetAt(reportIndex-1);
            /*删除其他sheet*/
            //删除左半部分
            for (int i = 0; i < reportIndex-1; i++) {
                wb.removeSheetAt(0);
            }
            //删除右半部分
            for (int j = reportIndex; j < sheetNum; j++) {
                wb.removeSheetAt(1);
            }
            if (reportDataList != null) {
                //循环报表数据放入到Excel中
                putReportDataToExcel(reportDataList, reportIndex-1, sheet);
            }
        }

        try {
            out.flush();
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环报表数据放入到Excel中
     * @param reportDataList
     * @param reportNum
     * @param sheet
     */
    public void putReportDataToExcel(List<ComonReportPakage.Values> reportDataList, Integer reportNum, Sheet sheet){
        //报表子表数据
        ComonReportPakage.Values reportData = reportDataList.get(reportNum);

        if (reportData != null && reportData.getFields() != null) {
            //具体字段值
            List<ComonReportPakage.ValueField> fields = reportData.getFields();

            //处理报表数据
            List<List<EnterpriseReportDataStore>> reportSheetData = dealReportSheetData(fields);

            for (int j = 1;j<=sheet.getLastRowNum();j++) {
                //循环每行
                Row row = sheet.getRow(j);

                if (row != null && !"".equals(row.toString().trim())) {
                    //获取报表行内值
                    List<EnterpriseReportDataStore> reportSheet = reportSheetData.get(j-1);

                    if (reportSheet != null && reportSheet.size() > 0) {
                        //default 3列数据
                        Cell cell1 = row.getCell(1);
                        cell1.setCellValue(String.valueOf(reportSheet.get(0).getBeginBalance()));

                        Cell cell2 = row.getCell(2);
                        cell2.setCellValue(String.valueOf(reportSheet.get(0).getEndBalance()));

                        //6列数据
                        if (reportSheet.size() == 2) {
                            Cell cell4 = row.getCell(4);
                            cell4.setCellValue(String.valueOf(reportSheet.get(1).getBeginBalance()));

                            Cell cell5 = row.getCell(5);
                            cell5.setCellValue(String.valueOf(reportSheet.get(1).getEndBalance()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理报表子表中的数据
     * @param fields 报表子表中数据
     * @return
     */
    public List<List<EnterpriseReportDataStore>> dealReportSheetData(List<ComonReportPakage.ValueField> fields){
        //处理之后的数据
        List<List<EnterpriseReportDataStore>> sheetData = new ArrayList<>();

        //前3列
        List<EnterpriseReportDataStore> reportLeftSheet = new ArrayList<>();
        //后3列
        List<EnterpriseReportDataStore> reportRightSheet = new ArrayList<>();

        //循环每个value
        for (ComonReportPakage.ValueField valueField:fields) {
            //获取报表行内值
            ComonReportPakage.ModleField modleField = valueField.getModleField();

            if (modleField != null) {
                //列数
                Integer columnNo = modleField.getColunmNo();

                if (1 == columnNo) {
                    EnterpriseReportDataStore reportDataStore = getReportDataStore(valueField, modleField);

                    reportLeftSheet.add(reportDataStore);
                } else if (2 == columnNo) {
                    EnterpriseReportDataStore reportDataStore = getReportDataStore(valueField, modleField);

                    reportRightSheet.add(reportDataStore);
                }
            }
        }

        for (int i = 0;i < reportLeftSheet.size();i++) {
            //前3列
            List<EnterpriseReportDataStore> reportNewSheet = new ArrayList<>();

            if (i < reportRightSheet.size()) {
                EnterpriseReportDataStore reportDataStore = reportLeftSheet.get(i);
                EnterpriseReportDataStore reportDataStore2 = reportRightSheet.get(i);

                String columnNum = reportDataStore.getBeginExcelName().substring(1);
                String columnNum2 = reportDataStore2.getBeginExcelName().substring(1);

                if (!StringUtils.isEmpty(columnNum) && !StringUtils.isEmpty(columnNum2)) {
                    if (columnNum.equals(columnNum2)) {
                        reportNewSheet.add(reportDataStore);
                        reportNewSheet.add(reportDataStore2);
                        sheetData.add(reportNewSheet);
                    }
                }
            } else {
                EnterpriseReportDataStore reportDataStore = reportLeftSheet.get(i);
                reportNewSheet.add(reportDataStore);
                sheetData.add(reportNewSheet);
            }
        }

        return sheetData;
    }

    /**
     * 获取报表数据
     * @param valueField
     * @param modleField
     * @return
     */
    public EnterpriseReportDataStore getReportDataStore(ComonReportPakage.ValueField valueField, ComonReportPakage.ModleField modleField){
        //报表数据
        EnterpriseReportDataStore reportDataStore = new EnterpriseReportDataStore();

        reportDataStore.setReportModelId(valueField.getModleId());
        reportDataStore.setReportSonNo(modleField.getColunmNo());
        reportDataStore.setOrderNo(modleField.getOrder());
        reportDataStore.setName(modleField.getName());

        reportDataStore.setBeginBalance(new BigDecimal(valueField.getBeginBalance()));
        reportDataStore.setEndBalance(new BigDecimal(valueField.getEndBalance()));

        reportDataStore.setBeginExcelName(valueField.getBeginExcelName());
        reportDataStore.setBeginExcelName(valueField.getEndExcelName());

        return reportDataStore;
    }
}
