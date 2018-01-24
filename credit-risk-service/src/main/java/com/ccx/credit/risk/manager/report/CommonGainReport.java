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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/9/26
 */
@Component
public class CommonGainReport  {
    @Autowired
    private GainReportMapper gainReportMapper;

    private volatile Map<Integer,GainReportModel> dataModles=new TimerConcurrentHashMap<>(1000*60*60l,1000);
    private Logger logger = LogManager.getLogger(CommonGainReport.class);
    /**
     * 获取模型sheet包 结构， [sheet数据1，sheet数据2] ,其中sheet数据为 { 子表id，字表名称，[第一行列名1,第二行列2...],[字段属性1，字段属性2，...]}
     * @param typeId
     * @return
     */
    public  List<ComonReportPakage.Modle> getModles(Integer typeId){
        return   getMd(typeId).getModles();
    }

    /**
     * 根据sheetId，即子表id，获取子表属性map，以字段Id,中文字段名,excel字段名，排序编号 作为key
     * @param sheetId
     * @param type
     * @return
     */
    public Map<String,ComonReportPakage.ModleField> getModleFieldBySheetId(Integer sheetId,MapKeyEnum type){
        if(sheetId==null) return null;
        Map map=new HashMap();
        map.put("id",sheetId);
        Map<String ,Object> val= gainReportMapper.getSheetMsgBysheetId(map);
        if(val==null||val.size()==0||val.get("report_type")==null) return null;
        return getModleFieldBySheetId(Integer.valueOf( val.get("report_type").toString()),sheetId,type);
    }
    /**
     * 根据sheetName 即子表名称+typeId，获取子表属性map，以字段Id,中文字段名,excel字段名，排序编号 作为key
     * @param
     * @param type
     * @return
     */
    public Map<String,ComonReportPakage.ModleField> getModleFieldBySheetId(Integer typeId,String sheetName,MapKeyEnum type){
        if(typeId==null&&sheetName!=null) return null;
        Map map=new HashMap();
        map.put("name",sheetName);
        map.put("typeId",typeId);
        Map<String ,Object> val= gainReportMapper.getSheetMsgBysheetId(map);
        if(val==null||val.size()==0||val.get("id")==null) return null;
        return getModleFieldBySheetId(Integer.valueOf( val.get("report_type").toString()),Integer.valueOf( val.get("id").toString()),type);
    }

    /**
     * 根据sheetId，即子表id，获取子表属性map，以字段Id,中文字段名,excel字段名，排序编号 作为key
     * @param sheetId
     * @return
     */
    private Map<String,ComonReportPakage.ModleField> getModleFieldBySheetId(Integer typeId,Integer sheetId,MapKeyEnum type){
        List<ComonReportPakage.ModleField> list = ((Map<Integer, List<ComonReportPakage.ModleField>>)  getMd(typeId).getK()).get(sheetId);
        Map<String,ComonReportPakage.ModleField> map=new HashMap<>();
        for(ComonReportPakage.ModleField str:list){
            map.put(MapKeyEnum.getValue(str,type),str);
        }
        return map;
    }

    /**
     * 获取sheet表属性方法 getModleFieldBySheetId 中的type
     */
    public  enum MapKeyEnum{
        FIELD_ID,//字段Id
        FIELD_NAME,//字段名称
        FIELD_EXCLE_NAME,//excel名称
        FIELD_ORDER;//排序编号
        public static String getValue(ComonReportPakage.ModleField str,MapKeyEnum key){
            String re;
           switch (key){
               case FIELD_ID:
                   re= String.valueOf(str.getModleId());
                   break;
               case FIELD_NAME:
                   re=str.getName();
                   break;
               case FIELD_EXCLE_NAME:
                   re=str.getExcelName();
                   break;
               case FIELD_ORDER:
                   re=String.valueOf(str.getOrder());
                   break;
               default:
                   re=str.getName();
           }
           return re;
        }
    }
    private  GainReportModel getMd(Integer typeId){
        if(dataModles.get(typeId)!=null){
            return  dataModles.get(typeId);
        }
        return   getM(typeId);
    }
    private  GainReportModel getM(Integer typeId){
        GainReportModel model=null;
        if(dataModles.get(typeId)==null){
            synchronized ("getM_"+typeId) {
                if(dataModles.get(typeId)==null){
                    logger.info("获取数据包模型数据,分类Id："+typeId);
                    model=new GainReportModel<Map<Integer,List<ComonReportPakage.ModleField>>,Map<Integer,ComonReportPakage.ModleField>>(gainReportMapper, typeId) {
                        @Override
                        public void initK() {
                            Map<Integer,List<ComonReportPakage.ModleField>> map = new HashMap<>();
                            for(ComonReportPakage.Modle str:modles){
                                map.put(str.getSheetId(),str.getFields());
                            }
                            setK(map);
                        }

                        @Override
                        public void initV() {
                            Map<Integer,ComonReportPakage.ModleField> map = new HashMap<>();
                            for(ComonReportPakage.Modle str:modles){
                                for(ComonReportPakage.ModleField st:str.getFields()){
                                    map.put(st.getModleId(),st);
                                }
                            }
                            setV(map);
                        }
                    };
                    dataModles.put(typeId,model);
                    return  model;
                }
            }
        }
        logger.info("获取数据包模型缓存数据,分类Id："+typeId);
        return  dataModles.get(typeId);
    }
   /* @Override
    public Object modles(Integer typeId) {
        List<ComonReportPakage.Modle> lits= getModles(1);
        for (ComonReportPakage.Modle str:lits){
            System.out.println("fist:::"+str.getFristClumn());
        }
        return null;
    }

    @Override
    public Object vals() {
        return null;
    }*/

}
