package com.ccx.credit.risk.manager.report;


import com.ccx.credit.risk.mapper.gainreport.GainReportMapper;
import com.ccx.credit.risk.model.enterprise.Report;
import com.ccx.credit.risk.utils.LockUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/25
 */
public abstract class GainReportValue<K,V> {
    //@Autowired
    private GainReportMapper gainReportMapper;

    //reportId
    private Integer reportId;
    //typeId
    private Integer typeId;
    //报表基础数据包
    private ComonReportPakage.BaseValues baseValues=new ComonReportPakage.BaseValues();

    //K模型存储
    private K k;
    //V模型存储
    private V v;

    //sheet数据包
    // private TreeMap<Integer,TreeMap<Integer,ComonReportPakage.ValueField>> sheetValue=new TreeMap<>();
    private Map<Integer,List<ComonReportPakage.ValueField>> sheetValue = new HashMap<>();

    public GainReportValue(GainReportMapper gainReportMapper,Integer reportId) {
        this.gainReportMapper = gainReportMapper;
        this.reportId=reportId;
        init();
    }

    private void init(){
        //获取概况信息
       Report report= gainReportMapper.findById(reportId);
       baseValues.setReport(report);
       this.typeId=report.getType();
       List<ComonReportPakage.ValueField> list = new ArrayList<>();
       //获取报表状态信息
       List<Map<String,Object>> slist= gainReportMapper.findReportSheetStatusByid(reportId);

       //定义map存储Vlues
       List<ComonReportPakage.Values> valuesList = new ArrayList<>();
        //获取报表所有的存储值
        List<Map<String,Object>> valueList= gainReportMapper.findValuesByreportid(reportId);
        //将valueList放入Map容器中
        dealWithValus(valueList);

       for(Map<String,Object> str:slist){
           ComonReportPakage.Values values = new ComonReportPakage.Values();
           values.setId(Integer.valueOf(str.get("id").toString()));
           values.setStatus(Integer.valueOf(str.get("state").toString()));
           values.setSheetId(Integer.valueOf(str.get("report_son_type").toString()));
           values.setSheetName(str.get("name").toString());
           values.setFields(sheetValue.get(values.getSheetId()));
            values.setSheetOrder(Integer.valueOf(str.get("sheet_order").toString()));
           valuesList.add(values);
       }
        baseValues.setValues(valuesList);

    }

    /**
     * 处理存储值
     * @param valueList
     * @return
     */
    private void   dealWithValus(List<Map<String,Object>> valueList){

        for (Map<String,Object> str:valueList){
            List<ComonReportPakage.ValueField> list = null;
           Integer sheetId=Integer.valueOf(str.get("report_son_type").toString());
           if(sheetValue.get(sheetId)==null){
               list=new ArrayList<>();
               sheetValue.put(sheetId,list);
           }
           list=sheetValue.get(sheetId);
            ComonReportPakage.ValueField field=new ComonReportPakage.ValueField();
           field.setBeginBalance(str.get("begin_balance")==null?null:str.get("begin_balance").toString());
           field.setBeginExcelName((String)str.get("begin_excel_name"));
           field.setEndBalance(str.get("end_balance")==null?null:str.get("end_balance").toString());
           field.setEndExcelName((String)str.get("end_excel_name"));
           field.setModleId(Integer.valueOf(str.get("report_model_id").toString()));
            ComonReportPakage.ModleField mfield = new ComonReportPakage.ModleField();
            mfield.setName((String)str.get("financial_subject"));
            mfield.setColunmNo(Integer.valueOf(str.get("report_son_no").toString()));
            mfield.setExcelName((String)str.get("column_excel"));
            mfield.setOrder(Integer.valueOf(str.get("order_no").toString()));
            mfield.setRequired(Integer.valueOf(str.get("required").toString()));
            field.setModleField(mfield);
           list.add(field);
        }
    }

    public K getK() {
        if(k==null) {
            Lock lock= LockUtils.getLock("getVK_"+typeId);
            lock.lock();
            try {
                if(k==null){
                    initK();
                }
            }finally {
                lock.unlock();
            }
        }
        return k;
    }

    public V getV() {
        if(v==null) {
            Lock lock= LockUtils.getLock("getVV_"+typeId);
            lock.lock();
            try {
                if(k==null){
                    initV();
                }
            }finally {
                lock.unlock();
            }
        }
        return v;
    }

    public abstract  void initK();

    public abstract  void initV();

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public ComonReportPakage.BaseValues getBaseValues() {
        return baseValues;
    }

    public void setBaseValues(ComonReportPakage.BaseValues baseValues) {
        this.baseValues = baseValues;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
    public void setK(K k) {
        this.k = k;
    }

    public void setV(V v) {
        this.v = v;
    }
}
