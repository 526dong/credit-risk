package com.ccx.credit.risk.manager.report;


import com.ccx.credit.risk.mapper.gainreport.GainReportMapper;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.utils.LockUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/25
 */
public abstract class GainReportModel<K,V> {
    //mapper数据库查询
    private GainReportMapper gainReportMapper;

    //typeId
    private Integer typeId;

    //模型字段
     List<ComonReportPakage.Modle> modles=new ArrayList<>();

     //K模型存储
    private K k;
    //V模型存储
    private V v;

    public GainReportModel(GainReportMapper gainReportMapper,Integer typeId) {
        this.typeId=typeId;
        this.gainReportMapper=gainReportMapper;
         initModles(typeId);
    }

    /**
     * 初始化模板
     * @param typeId
     */
    private void initModles(Integer typeId) {
        this.modles=getModlesValue(typeId);
    }

    /**
     * 从数据库获取模板值
     * @param typeId
     * @return
     */
    private List<ComonReportPakage.Modle> getModlesValue(Integer typeId) {
        List<ComonReportPakage.Modle> modles=new ArrayList<>();
        //获取sheet属性
        List<Map<String,Object>> list=gainReportMapper.getModleSheepList(typeId);
        //组装字段到sheet中
        Map<Integer,List<ComonReportPakage.ModleField>> sf=dealFields(gainReportMapper.getModleFiledpList(typeId));

        //获取公式
        Map<Integer,List<ComonReportPakage.Formula>> formula=dealFormulas(gainReportMapper.findFormulasBytypeId(typeId));

        for(Map<String ,Object> str:list){
            ComonReportPakage.Modle mode=new ComonReportPakage.Modle();
            mode.setSheetId(Integer.valueOf(str.get("id").toString()));
            mode.setSheetName(str.get("name").toString());
            mode.setFristClumn( Arrays.asList( str.get("columns_first_name").toString().split(";;")));
            mode.setFields(sf.get(Integer.valueOf(str.get("id").toString())));
            mode.setFormulas(formula.get(mode.getSheetId()));
            modles.add(mode);
        }
        System.out.println("size:::"+list.size());
        return modles;
    }

    /**
     * 组装字段
     * @param fileds
     * @return
     */
    private Map<Integer,List<ComonReportPakage.ModleField>> dealFields(List<Map<String,Object>> fileds){
        Map<Integer,List<ComonReportPakage.ModleField>> map=new HashMap<>();
        if(fileds==null||fileds.size()==0) {
            return map;
        }
        List<ComonReportPakage.ModleField> list=null;
       for(Map<String,Object> str:fileds){
           Integer sheetId=Integer.valueOf(str.get("report_son_type").toString());
           list=map.get(sheetId);
           if(list==null) {
               list=new ArrayList<>();
               map.put(sheetId,list);
           }
           ComonReportPakage.ModleField field=new ComonReportPakage.ModleField();
           field.setModleId(Integer.valueOf(str.get("id").toString()));
           field.setName((String)str.get("financial_subject"));
           field.setColunmNo(Integer.valueOf(str.get("report_son_no").toString()));
           field.setExcelName((String)str.get("column_excel"));
           field.setOrder(Integer.valueOf(str.get("order_no").toString()));
           field.setRequired(Integer.valueOf(str.get("required").toString()));
           list.add(field);
       }
        return map;
    }

    /**
     * 组装sheet公式
     * @return
     */
    private Map<Integer,List<ComonReportPakage.Formula>> dealFormulas( List<Map<String,Object>> formulas){
        Map<Integer,List<ComonReportPakage.Formula>> map = new HashMap<>();
        if(formulas==null||formulas.size()==0) return map;
        for(Map<String,Object> str:formulas){
            Integer sheetId=Integer.valueOf(str.get("report_son_type").toString());
            ComonReportPakage.Formula formula=new ComonReportPakage.Formula();
            formula.setModleId(Integer.valueOf(str.get("model_id").toString()));
            formula.setBeginFormula((String)str.get("begin_formula"));
            formula.setEndFormula((String)str.get("end_formula"));
            List<ComonReportPakage.Formula> list = map.get(sheetId);
            if(list==null){
                list=new ArrayList<>();
                map.put(sheetId,list);
            }
            list.add(formula);
        }
        return  map;
    }
    public K getK() {
        if(k==null) {
            Lock lock= LockUtils.getLock("getK_"+typeId);
            lock.lock();
            try {
                if (k == null) {
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
            Lock lock= LockUtils.getLock("getV_"+typeId);
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

    public List<ComonReportPakage.Modle> getModles() {
        return modles;
    }

    public void setK(K k) {
        this.k = k;
    }

    public void setV(V v) {
        this.v = v;
    }

    public void setModles(List<ComonReportPakage.Modle> modles) {
        this.modles = modles;
    }
}
