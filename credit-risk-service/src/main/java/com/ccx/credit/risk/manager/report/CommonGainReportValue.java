package com.ccx.credit.risk.manager.report;

import com.ccx.credit.risk.manager.TimerConcurrentHashMap;
import com.ccx.credit.risk.mapper.gainreport.GainReportMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/26
 */
@Component
public class CommonGainReportValue {
    @Autowired
    private GainReportMapper gainReportMapper;

    private volatile Map<Integer,GainReportValue> dataValues=new TimerConcurrentHashMap<>(1000*60*10l,1000);

    private Logger logger = LogManager.getLogger(CommonGainReportValue.class);
    /**
     * 获取评级报表基础数据包
     * @param reportId
     * @return
     */
    public ComonReportPakage.BaseValues getBaseValue(Integer reportId){
        return  getV(reportId).getBaseValues();
    }


    /**
     * 获取子表数据，通过子表id、子表名称、子表在模板中的顺序中的任意一个
     * @param reportId 报表id
     * @param sheetType 根据子表的哪种属性查询子表
     * @param sheetKey 子表属性值
     * @param type 返回数据包 map的key的类型
     * @return 返回数据为Map，其中key为 字段Id,中文字段名,excel字段名，排序编号 中的任意一个
     */
    public Map<String,ComonReportPakage.ValueField> getSheetValue( Integer reportId,SheetKeyEnum sheetType, String sheetKey, MapKeyEnum type){
        Map<String,ComonReportPakage.ValueField> map = new HashMap<>();
        ComonReportPakage.Values vs = chooiseVs(sheetType,reportId,sheetKey);
        if(vs==null) return null;
        List<ComonReportPakage.ValueField> valueFields=vs.getFields();
        for(ComonReportPakage.ValueField str:valueFields){
           map.put(MapKeyEnum.getValue(str,type),str);
        }
        return map;
    }

    /**
     * 获取子表数据，通过子表id、子表名称、子表在模板中的顺序中的任意一个
     * @param reportId 报表id
     * @param sheetType 根据子表的哪种属性查询子表
     * @param sheetKey 子表属性值
     * @param type 返回数据包 map的key的类型
     * @return 返回数据为Map，其中key为 字段属性+$begin/$end 或者返回excle中的具体位置
     */
    public Map<String,String> getSheetValueOther(Integer reportId,SheetKeyEnum sheetType,  String sheetKey, SpecialKeyEnum type){
        Map<String,String> map = new HashMap<>();
        ComonReportPakage.Values vs = chooiseVs(sheetType,reportId,sheetKey);
        if(vs==null) return null;
        List<ComonReportPakage.ValueField> valueFields=vs.getFields();
        for(ComonReportPakage.ValueField str:valueFields){
            SpecialKeyEnum.getValue(str,type,map);
        }
        return map;
    }

    /**
     * 根据sheet属性获取sheet数据
     * @param sheetType
     * @param reportId
     * @param sheetKey
     * @return
     */
    private  ComonReportPakage.Values chooiseVs(SheetKeyEnum sheetType, Integer reportId, String sheetKey){
        List<ComonReportPakage.Values> valus= getV(reportId).getBaseValues().getValues();
        ComonReportPakage.Values vs = null;
        for(ComonReportPakage.Values str:valus){
            if(SheetKeyEnum.getValue(str,sheetType).equals(sheetKey)){
                vs=str;
                break;
            }
        }
        return vs;
    }
    private  GainReportValue getV(Integer reportId) {
        GainReportValue value =null;
        if(dataValues.get(reportId)==null){
            synchronized ("getV_" + reportId) {
                if(dataValues.get(reportId)==null){
                    logger.info("获取数据包数据,评级Id："+reportId);
                     value =  new GainReportValue(gainReportMapper, reportId) {
                        @Override
                        public void initK() {
                        }

                        @Override
                        public void initV() {
                        }
                    };
                     dataValues.put(reportId,value);
                    return value;
                }
            }
        }
        logger.info("获取数据包缓存数据,评级Id："+reportId);
       return dataValues.get(reportId);
    }

    /**
     * 报表数据集封装
     */
    public class ReportMap<K> {
        /**
         * 第一层sheet数据map的key格式为（$1_sheetid$2_sheetName$3_sheetOrder$）
         * 第二层filed数据map的key格式为$1_filed_id$1_fieldName$3_filedExcleName$4_fieldorder$
         */

        private Map<String,List<ComonReportPakage.ValueField>> data;
        public ReportMap(Map<String,List<ComonReportPakage.ValueField>> data) {
            this.data = data;
        }
        /**
         * 获取表数据（以一行为单位)
         * @return
         */
        public Map<String, ComonReportPakage.ValueField> getDataH(){
            return null;
        }
    }

    /**
     * 子表的key的传入形式
     */
    public  enum SheetKeyEnum{
        SHEET_ID,//子表Id
        SHEET_NAME,//子表名称
        SHEET_ORRDER;//子表排序编号，默认从1开始

        public static String getValue(ComonReportPakage.Values str,  SheetKeyEnum type){
            String re="";
            switch (type){
                case SHEET_ID:
                    re= String.valueOf(str.getSheetId());
                    break;
                case SHEET_NAME:
                    re= str.getSheetName();
                    break;
                case SHEET_ORRDER:
                    re= String.valueOf(str.getSheetOrder());
                    break;

                default:
            }
            return re;
        }
    }

    /**
     * Map返回中Key的格式
     */
    public  enum MapKeyEnum{
        FIELD_ID,//字段Id
        FIELD_NAME,//字段名称
        FIELD_EXCLE_NAME,//excel名称
        FIELD_ORDER;//排序编号
        public static String getValue(ComonReportPakage.ValueField str, MapKeyEnum key){
            String re;
            switch (key){
                case FIELD_ID:
                    re= String.valueOf(str.getModleId());
                    break;
                case FIELD_NAME:
                    re=str.getModleField().getName();
                    break;
                case FIELD_EXCLE_NAME:
                    re=str.getModleField().getExcelName();
                    break;
                case FIELD_ORDER:
                    re=String.valueOf(str.getModleField().getOrder());
                    break;
                default:
                    re=str.getModleField().getName();
            }
            return re;
        }
    }
    /**
     * Map返回中Key的格式
     */
    public  enum SpecialKeyEnum{
        FIELD_ID,//字段Id$begin、//字段Id$end
        FIELD_NAME,//字段名称$begin 、字段名称$end
        FIELD_EXCLE_NAME,//excel名称 例如B2、C3等
        FIELD_ORDER;//排序编号$begin、排序编号$end
        public static void  getValue(ComonReportPakage.ValueField str, SpecialKeyEnum key,Map<String,String> map){
            String re;
            switch (key){
                case FIELD_ID:
                    map.put(str.getModleId()+"$begin",str.getBeginBalance());
                    map.put(str.getModleId()+"$end",str.getEndBalance());
                    break;
                case FIELD_NAME:
                    map.put(str.getModleField().getName()+"$begin",str.getBeginBalance());
                    map.put(str.getModleField().getName()+"$end",str.getEndBalance());
                    break;
                case FIELD_EXCLE_NAME:
                    map.put(str.getBeginExcelName(),str.getBeginBalance());
                    map.put(str.getEndExcelName(),str.getEndBalance());
                    break;
                case FIELD_ORDER:
                    map.put(str.getModleField().getOrder()+"$begin",str.getBeginBalance());
                    map.put(str.getModleField().getOrder()+"$end",str.getEndBalance());
                    break;
                default:
            }
        }
    }

}
