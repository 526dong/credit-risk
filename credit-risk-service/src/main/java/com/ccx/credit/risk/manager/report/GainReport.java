/*
package com.ccx.credit.risk.manager;


import com.ccx.credit.risk.mapper.gainreport.GainReportMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*/
/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/25
 *//*

public abstract class GainReport<K,V> {
    @Autowired
    private GainReportMapper gainReportMapper;
    //模型字段
     List<ComonReportPakage.Modle> modles=new ArrayList<>();

    //报表值
      List<ComonReportPakage.Values> vals=new ArrayList<>();

    public List<ComonReportPakage.Modle> getModles(Integer typeId) {
        List<ComonReportPakage.Modle> modles=null;
        modles=getModlesValue(typeId);
        return modles;
    }
    public List<ComonReportPakage.Modle> getModlesValue(Integer typeId) {
        List<ComonReportPakage.Modle> modles=new ArrayList<>();
        List<Map<String,Object>> list=gainReportMapper.getModleSheepList();
        for(Map<String ,Object> str:list){
            ComonReportPakage.Modle mode=new ComonReportPakage.Modle();
            mode.setSheetId(Integer.valueOf(str.get("id").toString()));
            mode.setSheetName(str.get("name").toString());
            modles.add(mode);
        }
        System.out.println("size:::"+list.size());
        return modles;
    }
    public List<ComonReportPakage.Values> getVals(Integer reportId) {
        List<ComonReportPakage.Values> values=getValsV(reportId);
        return values;
    }
    public List<ComonReportPakage.Values> getValsV(Integer reportId) {
        return null;
    }
    public abstract  K modles(Integer typeId);
    public abstract V vals();

    public List<ComonReportPakage.Modle> getModles() {
        return modles;
    }

    public void setModles(List<ComonReportPakage.Modle> modles) {
        this.modles = modles;
    }

    public List<ComonReportPakage.Values> getVals() {
        return vals;
    }

    public void setVals(List<ComonReportPakage.Values> vals) {
        this.vals = vals;
    }
}
*/
