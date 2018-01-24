package com.ccx.credit.risk.manager.modelmessage;

import com.ccx.credit.risk.mapper.assetlayer.LayerMapper;
import com.ccx.credit.risk.mapper.assetlayer.LayerSetupMapper;
import com.ccx.credit.risk.mapper.gianmodel.GainModelMapper;
import com.ccx.credit.risk.model.assetlayer.Layer;
import com.ccx.credit.risk.model.assetlayer.LayerSetup;
import com.ccx.credit.risk.util.HttpClientUtil;
import com.ccx.credit.risk.util.JsonUtils;
import com.ccx.credit.risk.util.PropertiesUtil;
import com.ccx.credit.risk.utils.DateTool;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @author:lilong
 * @Date: 2017/10/24
 */
@Component
public class ModelDataMannager {
    @Autowired
    GainModelMapper gainModelMapper;

    @Autowired
    LayerMapper layerMapper;

    @Autowired
    LayerSetupMapper layerSetupMapper;

     /**
     * 类型模板存储路径
     */
    private static final String MODEL_EXCEL_SAVE_DIR= PropertiesUtil.getProperty("model_excel_save_dir"); //"E://data//model.xlsx";


     enum ModelEnums {


         //现金流数据获取
         CASHFLOW("还款计划", 2, 0),
         //获取违约率
         DEFAULRATES("违约率", 3, 0),
         //获取回收率
         RECOVERYRATES("回收率", 1, 0),

         //获取相关性
         CORRELATION("相关系数", 1, 1),

         //获取分层数据
         SETUP("分层设置数据", 15, 0);

         //sheet名称
         private String name;
         //行
         private Integer row;
         //列
         private Integer lie;

         ModelEnums(String name, Integer row, Integer lie) {
             this.name = name;
             this.row = row;
             this.lie = lie;
         }
        public static Object[] getMsg(ModelEnums e){
           for(ModelEnums str: ModelEnums.values()){
               if(e==str){
                   return new Object[]{str.name,str.row,str.lie};
               }
           }
           return  null;
        }
      }

    /**
     * 提交分层数据到模型组
     * @param layerId
     * @return
     */
    public Map<String,Object> postLayerData(Integer layerId){
        Map<String,Object> map = new HashMap<>();
         try{
             Workbook workbook=createExel(layerId);
             ByteArrayOutputStream os = new ByteArrayOutputStream();
             workbook.write(os);
             byte[] b = os.toByteArray();
             String res=  HttpClientUtil.post("http://localhost:8999/rtype/exelupload",
                     b,"application/octet-stream",layerId+".xlsx");
             os.close();
             System.out.println("res::::::::::::"+res);
             //放入本地服务器
//                      FileOutputStream outputStream=new FileOutputStream("E://data//ceshi//"+System.currentTimeMillis()+".xlsx");
//                      workbook.write(outputStream);
//                      outputStream.close();
             map.put("code","0000");
             map.put("msg","提交分层成功");
         }catch (Exception e){
             map.put("code","9999");
             map.put("msg","系统异常");
             e.printStackTrace();
         }
        return  map;
    }

    /**
     * 创建exek
     * @param layerId
     */
      private Workbook  createExel(Integer layerId){
          Workbook workbook=null;
          Map<String,Object> data=get(layerId);
          if(data==null||data.size()!=5) throw new RuntimeException("读取数据失败");
          File file = new File(MODEL_EXCEL_SAVE_DIR);
              if(file.exists()){    //文件存在
                  //创建workbook
                  try {
                      workbook = new XSSFWorkbook(new FileInputStream(file));
                      for(ModelEnums str: ModelEnums.values()){
                          Sheet sheet = workbook.getSheet(str.name);
                          Object object = data.get(str.toString());
                          if(object instanceof Map){
                              Map<String ,Object> map=( Map<String ,Object>)object;
                              write(sheet,(Map<String,String>)map.get("1"));
                              write(str.row,str.lie,sheet,(List<List<String>>)map.get("2"));
                          }else {
                              write(str.row,str.lie,sheet,(List<List<String>>)object);
                          }
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
                       return workbook;
                }
                throw new RuntimeException("模板不存在");
      }

    /**
     * 写入list到exel中
     * @param sheet
     * @param list
     */
      private  void write(int r,int l,Sheet sheet,List<List<String>> list){
          for(List<String > str:list){
              int lie=l;
              Row row = sheet.createRow(r++);
              for(String st:str){
                  Cell cell = row.createCell(lie++);
                  cell.setCellValue(st);
              }
          }
      }
    /**
     * 写入map到exel中
     * @param sheet
     * @param map
     */
    private  void write(Sheet sheet,Map<String,String> map) {
          for(Map.Entry<String,String> str:map.entrySet()){
              String key=str.getKey();
              String[] val=key.split(",");
              Row row=sheet.getRow(Integer.valueOf(val[0]));
              Cell cell=row.createCell(Integer.valueOf(val[1]));
              cell.setCellValue(str.getValue());
          }
    }

    /**
     * 获取数据
     * @param layerId
     */
    private   Map<String,Object>  get(Integer layerId){
        Map<String,Object> data=new HashMap<>();
        Layer layer= layerMapper.selectByPrimaryKey(layerId);
        LayerSetup setup=layerSetupMapper.selectByLayerId(layerId);
        //现金流数据获取
        getCashFlow(layer.getAssetPakegeId(),data,ModelEnums.CASHFLOW.toString(),setup);
        //获取违约率
        getDefaulrates(data,ModelEnums.DEFAULRATES.toString());
        //获取回收率
        getRecoveryrates(data,ModelEnums.RECOVERYRATES.toString());

        //获取相关性
        getCorrelationIndex(layerId,layer.getAssetPakegeId(),data,ModelEnums.CORRELATION.toString());

        //获取分层数据
        getLayerSetup(layerId,data,ModelEnums.SETUP.toString(),setup);
        System.out.println("josn:::::::::::....."+JsonUtils.toJson(data));
        return data;
    }

    /**
     * 获取现金流数据
     * @param packageId
     * @param data
     * @param key
     */
    private void getCashFlow(final Integer packageId, final Map<String,Object> data, final String key,final LayerSetup setup){
        final  String stime= DateTool.datetime2Str(setup.getFoundTime(),"yyyy-MM-dd");
        final  String etime= DateTool.datetime2Str(setup.getPredictExpireTime(),"yyyy-MM-dd");
      new BaseGainMsg<Map<String,Object>,GainModelMapper>(gainModelMapper,data,key) {
          @Override
          public Map<String,Object> initMsg(GainModelMapper dao) {
              Map<String,Object> data=new HashMap<>();
              Map<Integer,List<CommonModlePakage.Repayment>> map = new LinkedHashMap<>();
              List<Map<String ,Object>> list = dao.getCashFlowList(packageId,stime,etime);
              for(Map<String,Object> str:list){
                  Integer assetsId=Integer.valueOf(str.get("assets_id").toString());
                  List<CommonModlePakage.Repayment> list1=map.get(assetsId);
                  if(list1==null){
                      list1=new ArrayList<>();
                      map.put(assetsId,list1);
                  }
                  list1.add(new CommonModlePakage.Repayment((String)str.get("repayment_date"),Double.valueOf(str.get("repayment_amount").toString())));
              }

              List<Map<String,Object>> asstesNames=gainModelMapper.getAssetsList(packageId);
             //资产包资产名称
              Map<Integer,String> asstes=new HashMap<>();
              for(Map<String,Object> str:asstesNames){
                  asstes.put(Integer.valueOf(str.get("assets_id").toString()),str.get("name").toString());
              }
              List<List<String>> list1 =   dealWithResult(map,asstes,DateTool.datetime2Str(setup.getFoundTime(),"yyyy年M月"),DateTool.datetime2Str(setup.getPredictExpireTime(),"yyyy年M月"));
              Map<String,String> d1=new HashMap<>();
              d1.put("1,1",String .valueOf(list1.size()-1));
              data.put("1",d1);
              data.put("2",list1);
            return  data;
          }

          /**
           * 处理结果集
           * @param map
           * @param asstes
           */
          private List<List<String>> dealWithResult(Map<Integer,List<CommonModlePakage.Repayment>> map,Map<Integer,String> asstes,String stime,String etime){
             String s=stime;
              List<List<String>> res=new ArrayList<>();
              //创建第一行
              List<String> first=new ArrayList<>();
              first.add("贷款时长(月)");
              first.add("资产名称");
              first.add(stime);
              while (!s.equals(etime)){
                  s=getNextMonth(s);
                  first.add(s);
              }
              res.add(first);
            for(Map.Entry<Integer,List<CommonModlePakage.Repayment>> str:map.entrySet()){
                String s1=stime;
                List<CommonModlePakage.Repayment> d=str.getValue();
                List<String> row=new ArrayList<>();
                //插入贷款时长
                row.add(String.valueOf(getMonthCount(d.get(0).getDate(),d.get(d.size()-1).getDate())));
                //插入主体名称
                row.add(asstes.get(str.getKey()));
                 //处理之前的日期的还款置为0
                String f=d.get(0).getDate();
                while (!f.equals(s1)){
                    row.add(String.valueOf(0));
                    s1=getNextMonth(s1);
                }
                for(CommonModlePakage.Repayment st:d){
                    while (!s1.equals(st.getDate())){
                        row.add(String.valueOf(0));
                        s1=getNextMonth(s1);
                    }
                    s1=getNextMonth(s1);
                    row.add(String.valueOf(st.getAmount()));
                }
                if(s1.equals(etime)) continue;
                //将后面的日期置为0
                while (!etime.equals(s1)){
                    s1=getNextMonth(s1);
                    row.add(String.valueOf(0));
                }
                row.add(String.valueOf(0));
                res.add(row);
            }
            return res;
          }


      };
    }

    /**
     * 获取违约率
     * @param data
     * @param key
     */
    private void getDefaulrates(final Map<String,Object> data, final String key){
        new BaseGainMsg<List<List<String>>,GainModelMapper>(gainModelMapper,data,key) {
            @Override
            public List<List<String>> initMsg(GainModelMapper dao) {
                List<Map<String,Object>> list = dao.getRateLevel();
                List<List<String>> data=new ArrayList<>();
                int i=1;
                String rate=null;
                for(Map<String,Object> str:list){
                   String dj= str.get("rating_level").toString();
                   List<String> d=null;
                   if(rate==null||!rate.equals(dj)){
                       d=new ArrayList<>();
                       d.add(String.valueOf(i++));
                       d.add(dj);
                       data.add(d);
                       rate=dj;
                   }
                   d=data.get(data.size()-1);
                   d.add(str.get("break_rate").toString());
                }

                return  data;
            }
        };
    }
    /**
     * 获取回收率
     * @param data
     * @param key
     */
    private void getRecoveryrates(final Map<String,Object> data, final String key){
        new BaseGainMsg<List<List<String>>,GainModelMapper>(gainModelMapper,data,key) {
            @Override
            public List<List<String>> initMsg(GainModelMapper dao) {
                List<Map<String,Object>> list = dao.getRecoveryrates();
                List<List<String>> data=new ArrayList<>();
                int i=1;
                for(Map<String,Object> str:list){
                    List<String> list1 = new ArrayList<>();
                    list1.add(String.valueOf(i++));
                    list1.add( str.get("name").toString());
                    list1.add( str.get("recycle_rate").toString());
                    data.add(list1);
                }
                return  data;
            }
        };
    }

    /**
     * 获取相关性系数
     * @param layerId
     * @param packageId
     * @param data
     * @param key
     */
   public void getCorrelationIndex(final Integer layerId, final Integer packageId, final Map<String,Object> data, final String key){
       new BaseGainMsg<List<List<String>>,GainModelMapper>(gainModelMapper,data,key) {
           @Override
           public List<List<String>> initMsg(GainModelMapper dao) {
               List<Map<String,Object>> list = dao.getRecoveryrates();
               List<List<String>> data=new ArrayList<>();
               //相关性常量系数α，β，γ
               List<Map<String,Object>> setup=dao.getCorrelationIndexParam();
                Double a=Double.valueOf(setup.get(0).get("param_value").toString());
                Double b=Double.valueOf(setup.get(1).get("param_value").toString());
                Double r=Double.valueOf(setup.get(2).get("param_value").toString());

                //资产信息
                List<AsstesMsg> asstesMsgs = null;
                Map<Integer,AsstesMsg> asstesMsgMap=new HashMap<>();
               //第一行
               List<String> first=new ArrayList<>();
               first.add("");
               data.add(first);
               asstesMsgs=getAssets(dao,packageId,asstesMsgMap,first);

                //主体相关性
                Map<Integer,Integer> ei =  getEI(dao,layerId);

                //后台行业相关性
               Map<Integer,Map<Integer,Double>> ii=getII(dao);

               for(AsstesMsg str:asstesMsgs){
                   List<String> list1 = new ArrayList<>();
                   list1.add(str.getName());
                   for(AsstesMsg st:asstesMsgs){
                        //行业相关值
                        double hi;
                        Double h= ii.get(str.getBgid()).get(st.getBgid());
                        hi=h==null?0:h;
                        //区域相关值
                        double qi=str.getProvinceid().equals(st.getProvinceid())?1:0;
                        //主体相关值
                        double zi=ei.get(str.getAssetsid())!=null&&ei.get(str.getAssetsid())!=0&&ei.get(st.getAssetsid())!=null&&ei.get(str.getAssetsid())==ei.get(st.getAssetsid())?1:0;
                        list1.add(String.valueOf(getScore(a,b,r,hi,qi,zi)));
                   }
                   data.add(list1);
               }


               return  data;
           }

           //计算相关性
           public Double getScore(double a,double b,double r,Double hi,Double qi,Double zi){
            return a*hi+b*qi+r*zi;
           }

           //获取主体相关性数据
           public Map<Integer,Integer> getEI(GainModelMapper dao,Integer layerId){
               Map<Integer,Integer> map = new HashMap<>();
               List<Map<String ,Object>> list = dao.getEnterpriseCorrelationIndex(layerId);
               for(Map<String,Object> str:list){
                   map.put(Integer.valueOf(str.get("asset_id").toString()),Integer.valueOf(str.get("relevant_value").toString()));
               }
            return map;
           }
           //获取资产相关数据
           public List<AsstesMsg> getAssets(GainModelMapper dao,Integer packageId,Map<Integer,AsstesMsg> asstesMsgMap,List<String> first){
               List<AsstesMsg> list = dao.getAsstesMsg(packageId);
               for(AsstesMsg str:list){
                    asstesMsgMap.put(str.getAssetsid(),str);
                    first.add(str.getName());
               }
               return list;
           }
           //获取主体相关性数据
           public  Map<Integer,Map<Integer,Double>> getII(GainModelMapper dao){
               Map<Integer,Map<Integer,Double>> map = new HashMap<>();
               List<Map<String ,Object>> list = dao.getInsdustryCorrelationIndex();
               for(Map<String,Object> str:list){
                   Integer f=Integer.valueOf(str.get("insdustry_first").toString());
                   Integer s=Integer.valueOf(str.get("insdustry_second").toString());
                   Double v=Double.valueOf(str.get("index_value").toString());
                   Map<Integer,Double> map1=null;
                   Map<Integer,Double> map2=null;
                   if(map.get(f)==null){
                       map1=new HashMap<>();
                       map.put(f,map1);
                   }
                   map1=map.get(f);
                   map1.put(s,v);
                   if(map.get(s)==null){
                       map2=new HashMap<>();
                       map.put(s,map2);
                   }
                   map2=map.get(s);
                    map2.put(f,v);
               }
               return map;
           }
       };
   }

    /**
     * 获取分层设置
     * @param data
     * @param key
     */
    private void getLayerSetup(final Integer layerId, final Map<String,Object> data, final String key, final LayerSetup setup) {
        new BaseGainMsg<Map<String, Object>, GainModelMapper>(gainModelMapper, data, key) {
            @Override
            public Map<String, Object> initMsg(GainModelMapper dao) {
                Map<String,Object> map = new HashMap<>();
                map.put("1",getLayerSetUp(setup));
                List<List<String>> data=new ArrayList<>();
                List<Map<String,Object>> levels=dao.getLayerLevel(layerId);
                Integer n=Integer.valueOf(DateTool.date2TimeStr(setup.getPredictExpireTime(),"yyyy"))
                        - Integer.valueOf(DateTool.date2TimeStr(setup.getFoundTime(),"yyyy"))+1;
                List<String> first=new ArrayList<>();
                first.add(String.valueOf(15));
                first.add("层级名称");
                first.add("层级比例");
                for (int i = 1; i <=n ; i++) {
                    first.add("第"+i+"年的预期收益率");
                }
                data.add(first);
                for(Map<String,Object> str:levels){
                    List<String> list = new ArrayList<>();
                    list.add("");
                    list.add(str.get("layer_name").toString());
                    list.add(str.get("capital_rate").toString());
                    Double lv=Double.valueOf(str.get("expect_earnings_rate").toString());
                    String isf=str.get("is_float").toString();
                    Double float_value=Double.valueOf(str.get("float_value").toString());
                    Double float_up=Double.valueOf(str.get("float_up").toString());
                    Double float_down=Double.valueOf(str.get("float_down").toString());
                    Double add=0d;
                    for (int i = 1; i <=n ; i++) {
                        list.add(String.valueOf(lv));
                        if("0".equals(isf)){
                           if((float_value>0&add+float_value<float_up)||(float_value<0&add+float_value>float_down)){
                               lv+=float_value;
                               add+=float_value;
                           }
                        }
                    }
                    data.add(list);
                }
                map.put("2",data);
                return map;
            }
        };
    }
    /**
     * 通过当前月份下一个的时间
     * 格式 yyyy-MM
     * @param thisMonth
     * @return
     */
    private static String getNextMonth(String thisMonth){
        System.out.println("thisMoth:::"+thisMonth);
        Integer year=Integer.valueOf(thisMonth.substring(0,4));
        Integer month=Integer.valueOf(thisMonth.substring(5,thisMonth.length()-1));
        System.out.println("year:::"+year);
        System.out.println("month:::"+month);
        return (month+1)>12?((year+1)+"年1月"):(year+"年"+(month+1)+"月");
    }

    /**
     * 计算贷款时长
     * @param stime
     * @param etime
     * @return
     */
    private static Integer getMonthCount(String stime,String etime){
        Integer syear=Integer.valueOf(stime.substring(0,4));
        Integer smonth=Integer.valueOf(stime.substring(5,stime.length()-1));
        Integer eyear=Integer.valueOf(etime.substring(0,4));
        Integer emonth=Integer.valueOf(etime.substring(5,etime.length()-1));
        return (eyear-syear)*12+emonth+1-smonth;
    }

    /**
     * 获取分层设置信息
     */

    private static Map<String,String> getLayerSetUp(LayerSetup setup){
        Map<String,String> map = new LinkedHashMap<>();
        map.put("1,2",object2String(setup.getSimulationNum()));
        map.put("2,2",object2String(setup.getPublishCapital()));
        map.put("3,2",DateTool.date2TimeStr(setup.getClosePackageTime(),"yyyy-MM-dd"));
        map.put("4,2",DateTool.date2TimeStr(setup.getFoundTime(),"yyyy-MM-dd"));
        map.put("5,2",DateTool.date2TimeStr(setup.getPredictExpireTime(),"yyyy-MM-dd"));
        map.put("6,2",object2String(setup.getTrusteeshipRate()));
        map.put("7,2",object2String(setup.getManageRate()));
        map.put("8,2",object2String(setup.getGradeRate()));
        map.put("9,2",object2String(setup.getTaxRate()));
        map.put("10,2",object2String(setup.getRepaymentType()));
        map.put("11,2",object2String(setup.getRepaymentIntervalTime()));
        map.put("12,2",DateTool.date2TimeStr(setup.getFirstRepaymentTime(),"yyyy-MM-dd"));
        map.put("13,2",object2String(setup.getRepaymentTime()));
        map.put("14,2",object2String(setup.getExpediteSettlementDefaultRate()));
        return map;
    }
    public static String object2String(Object obj){
        return obj==null?"":String .valueOf(obj.toString());
    }
    public static void main(String[] args) {
        /*System.out.println(DateTool.long2TimeStr(System.currentTimeMillis(),"yyyy-M"));
        //  System.out.println(getMonthCount("2017年03月","2017年05月"));
        String thisMonth="2017年5月";
        System.out.println(thisMonth.substring(5,thisMonth.length()-1));*/
        System.out.println(ModelEnums.getMsg(ModelEnums.CASHFLOW)[0]);
        System.out.println(ModelEnums.CASHFLOW.toString());
        AtomicInteger integer;
    }
}
