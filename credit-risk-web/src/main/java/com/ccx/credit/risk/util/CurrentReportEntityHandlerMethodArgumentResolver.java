//package com.ccx.credit.risk.util;
//
//import com.ccx.credit.risk.model.enterprise.*;
//import org.apache.poi.ss.formula.functions.T;
//import org.springframework.beans.BeanWrapper;
//import org.springframework.beans.BeanWrapperImpl;
//import org.springframework.core.MethodParameter;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * Created by xzd on 2017/9/8.
// */
//public class CurrentReportEntityHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver{
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        return parameter.hasParameterAnnotation(CurrentReportEntity.class);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        Map<String, String[]> parameterMap = webRequest.getParameterMap();
//
//        Object obj = null;
//
//        if (parameterMap.get("assetId") != null && !StringUtils.isEmpty(parameterMap.get("assetId")[0])) {
//            obj = (Object) new ReportAsset();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportAsset");
//            //反射处理后去掉逗号
//            ReportAsset asset = (ReportAsset) putMapVal(parameterMap, clazz, obj);
//            //ReportAsset asset = (ReportAsset) putVal(parameterMap, obj);
//            return asset;
//        } else if (parameterMap.get("ownerId") != null && !StringUtils.isEmpty(parameterMap.get("ownerId")[0])) {
//            obj = (Object) new ReportLiabilitiesEquites();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportLiabilitiesEquites");
//            //反射处理后去掉逗号
//            ReportLiabilitiesEquites owner = (ReportLiabilitiesEquites) putMapVal(parameterMap, clazz, obj);
//            return owner;
//        } else if (parameterMap.get("profitLossId") != null && !StringUtils.isEmpty(parameterMap.get("profitLossId")[0])) {
//            obj = (Object) new ReportProfitLoss();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportProfitLoss");
//            //反射处理后去掉逗号
//            ReportProfitLoss profitLoss = (ReportProfitLoss) putMapVal(parameterMap, clazz, obj);
//            return profitLoss;
//        } else if (parameterMap.get("cashId") != null && !StringUtils.isEmpty(parameterMap.get("cashId")[0])) {
//            obj = (Object) new ReportCashFlow();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportCashFlow");
//            //反射处理后去掉逗号
//            ReportCashFlow cash = (ReportCashFlow) putMapVal(parameterMap, clazz, obj);
//            return cash;
//        } else if (parameterMap.get("cashSupplyId") != null && !StringUtils.isEmpty(parameterMap.get("cashSupplyId")[0])) {
//            obj = (Object) new ReportAdditionalCashFlow();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportAdditionalCashFlow");
//            //反射处理后去掉逗号
//            ReportAdditionalCashFlow cashSupply = (ReportAdditionalCashFlow) putMapVal(parameterMap, clazz, obj);
//            return cashSupply;
//        } else if (parameterMap.get("financeId") != null && !StringUtils.isEmpty(parameterMap.get("financeId")[0])) {
//            obj = (Object) new ReportNotesFinancialStatement();
//            Class<?> clazz = Class.forName("com.ccx.credit.risk.model.enterprise.ReportNotesFinancialStatement");
//            //反射处理后去掉逗号
//            ReportNotesFinancialStatement finance = (ReportNotesFinancialStatement) putMapVal(parameterMap, clazz, obj);
//            return finance;
//        }
//        return obj;
//    }
//
//    /**
//     * 将map的值赋值给实体
//     * @param parameterMap
//     * @param clazz
//     * @param obj
//     * @return
//     * @throws Exception
//     */
//    public Object putMapVal(Map<String, String[]> parameterMap, Class<?> clazz, Object obj) throws Exception {
//        Iterator it = parameterMap.entrySet().iterator();
//        while(it.hasNext()){
//            Map.Entry<String, String[]> map = (Map.Entry<String, String[]>) it.next();
//
//            //去除非double类型字段
//            boolean flag = removeNotDouble(map.getKey());
//            //去除非double类型字段-reportId
//            boolean reportIdflag = removeReportId(map.getKey());
//
//            if (flag || reportIdflag) {
//                continue;
//            } else {
//                //页面传到的字段名称首字母转成大写
//                String key = toUpperCaseFirstOne((String) map.getKey());
//
//                Method method = clazz.getDeclaredMethod("set" + key, BigDecimal.class);
//
//                if (map.getValue().length > 0 && map.getValue()[0] != null && map.getValue()[0] != "") {
//                    String value = map.getValue()[0];
//                    if (value.contains(",")) {
//                        value = value.replaceAll(",", "");
//                        method.invoke(obj, new BigDecimal(value));
//                    } else {
//                        method.invoke(obj, new BigDecimal(value));
//                    }
//                } else {
//                    method.invoke(obj, new BigDecimal("0"));
//                }
//            }
//        }
//        return obj;
//    }
//
//    /**
//     *
//     * @param parameterMap
//     * @param obj
//     * @return
//     */
//    public Object putVal(Map<String, String[]> parameterMap, Object obj){
//        //对bean进行自动封装
//        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
//
//        Iterator it = parameterMap.entrySet().iterator();
//        while(it.hasNext()) {
//            Map.Entry<String, String[]> map = (Map.Entry<String, String[]>) it.next();
//
//            //去除非double类型字段
//            boolean flag = removeNotDouble(map.getKey());
//            //去除非double类型字段-reportId
//            boolean reportIdflag = removeReportId(map.getKey());
//
//            if (flag || reportIdflag) {
//                continue;
//            } else {
//                if (map.getValue().length > 0 && map.getValue()[0] != null && map.getValue()[0] != "") {
//                    String value = map.getValue()[0];
//                    if (value.contains(",")) {
//                        value = value.replaceAll(",", "");
//                        beanWrapper.setPropertyValue(map.getKey(), value);
//                    } else {
//                        beanWrapper.setPropertyValue(map.getKey(), value);
//                    }
//                } else {
//                    beanWrapper.setPropertyValue(map.getKey(), "0");
//                }
//            }
//        }
//
//        return obj;
//    }
//
//    /**
//     * 去除非double类型的字段
//     * @param str
//     * @return
//     */
//    public boolean removeNotDouble(String str) {
//        if ("id".equals(str) || "reportId".equals(str) || "reportUuid".equals(str) ||
//                "state".equals(str) || "createDate".equals(str) || "updateDate".equals(str) || "creatorName".equals(str)) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 去掉非double值
//     * @param str
//     * @return
//     */
//    public boolean removeReportId(String str) {
//        if ("assetId".equals(str)) {
//            return true;
//        } else if ("ownerId".equals(str)) {
//            return true;
//        } else if ("profitLossId".equals(str)) {
//            return true;
//        } else if ("cashId".equals(str)) {
//            return true;
//        } else if ("cashSupplyId".equals(str)) {
//            return true;
//        } else if ("financeId".equals(str)) {
//            return true;
//        }
//        return false;
//    }
//
//    //首字母转大写
//    public String toUpperCaseFirstOne(String str){
//        if(Character.isUpperCase(str.charAt(0)))
//            return str;
//        else
//            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
//    }
//
//}
