package com.ccx.credit.risk.util;


import com.alibaba.fastjson.JSONArray;
import com.ccx.credit.risk.api.AbsOperationRecordApi;
import com.ccx.credit.risk.model.AbsOperationRecord;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于记录所有系统操作的切面类
 * <p>
 * by:ymj
 */

@Aspect
@Component
public class AbsOperationRecordAspect {

    //注入service保存日志
    @Autowired
    private AbsOperationRecordApi absOperationRecordApi;

    //controller切点层   risk所有子包里的Controller类的任意方法的执行
    @Pointcut("execution(* com.ccx.credit.risk.*.*Controller.*(..))")
    public void controllerAspect() {

    }

    /*** 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before(value = "com.ccx.credit.risk.util.AbsOperationRecordAspect.controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
//        System.out.println("before==========" + joinPoint);
    }


    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint) {
//        System.out.println("========后置通知开始=============");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request.getRequestURI().indexOf("logout") == -1){
            AbsOperationRecord absOperationRecord = AbsOperationRecordUtils.getRecord(joinPoint);

            if (null != absOperationRecord) {

                //获取请求路径
                String urlPath = absOperationRecord.getUrlPath();
                //获取请求参数
                Map<String, String[]> paramMap = request.getParameterMap();
                //企业名称
                String operationName = "";
                try {
                    //当参数map不为空，获取参数在与id和name相关的值
                    if (!paramMap.isEmpty()) {
                        //基础模块：企业、角色...
                        //服务器和本地测试路径不一致，
                        String paramStr = urlPath.replace("//","/");
                        String objectAttr1 = urlPath.split("/")[1];
                        String objectAttr2 = urlPath.split("/")[2];
                        //如果id且不为空，查询id获取相应的名称
                        if (paramMap.containsKey("id") && paramMap.get("id")[0] != null && paramMap.get("id")[0].trim() != "") {
                            String id1 = paramMap.get("id")[0];
                            //获取参数中的id，根据url的路径判断从哪个表获取name
                            int id = Integer.parseInt(id1);

                            if (objectAttr1.equals("enterprise") || objectAttr1.equals("ratingApply") || objectAttr1.equals("riskCheck")) {
                                operationName = absOperationRecordApi.selectEnterPriseNameById(id);
                            }else if (objectAttr2.equals("enterprise") || objectAttr2.equals("ratingApply") || objectAttr2.equals("riskCheck")){
                                operationName = absOperationRecordApi.selectEnterPriseNameById(id);
                            }
                            else if (objectAttr1.equals("asset")||objectAttr2.equals("asset")) {
                                operationName = absOperationRecordApi.selectAssetNameById(id);
                            } else if (objectAttr1.equals("assetsPackage")||objectAttr2.equals("assetsPackage")) {
                                operationName = absOperationRecordApi.selectAssetsPackageNameById(id);
                            } else if (objectAttr1.equals("user")||objectAttr2.equals("user")) {
                                operationName = absOperationRecordApi.selectUserNameById(id);
                            } else if (objectAttr1.equals("role")||objectAttr2.equals("role")) {
                                operationName = absOperationRecordApi.selectRoleNameById(id);
                            }
                        }
                        //如果不存在id，则查找id相关的字段（此处往下为特殊情况，分辨列出判断）
                        else if (paramMap.containsKey("enterpriseId") && paramMap.get("enterpriseId")[0] != null && paramMap.get("enterpriseId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("enterpriseId")[0]);
                            operationName = absOperationRecordApi.selectEnterPriseNameById(id);
                        }
                        else if (paramMap.containsKey("comId") && paramMap.get("comId")[0] != null && paramMap.get("comId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("comId")[0]);
                            operationName = absOperationRecordApi.selectEnterPriseNameById(id);
                        }
                        else if (paramMap.containsKey("financialID") && paramMap.get("financialID")[0] != null && paramMap.get("financialID")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("financialID")[0]);
                            operationName = absOperationRecordApi.selectEnterPriseNameById(id);
                        }
                        else if (paramMap.containsKey("assetsPackageId") && paramMap.get("assetsPackageId")[0] != null && paramMap.get("assetsPackageId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("assetsPackageId")[0]);
                            operationName = absOperationRecordApi.selectAssetsPackageNameById(id);
                        }
                        else if (paramMap.containsKey("assetPackageId") && paramMap.get("assetPackageId")[0] != null && paramMap.get("assetPackageId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("assetPackageId")[0]);
                            operationName = absOperationRecordApi.selectAssetsPackageNameById(id);
                        }
                        else if (paramMap.containsKey("packageId") && paramMap.get("packageId")[0] != null && paramMap.get("packageId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("packageId")[0]);
                            operationName = absOperationRecordApi.selectAssetsPackageNameById(id);
                        }
                        else if (paramMap.containsKey("userId") && paramMap.get("userId")[0] != null && paramMap.get("userId")[0].trim() != "") {
                            int id = Integer.parseInt(paramMap.get("userId")[0]);
                            operationName = absOperationRecordApi.selectUserNameById(id);
                        }
                        //如果id相关字段不存在，查找与name相关字段存在直接赋值
                        else if (paramMap.containsKey("name")) {
                            operationName = paramMap.get("name")[0];
                        }
                        else if (paramMap.containsKey("assetPackageName")) {
                            operationName = paramMap.get("name")[0];
                        }
                        else if (paramMap.containsKey("keyWord")) {
                            operationName = paramMap.get("keyWord")[0];
                        }
                        else if (paramMap.containsKey("roleName")) {
                            operationName = paramMap.get("roleName")[0];
                        }
                        //资产类型判断
                        else if (paramMap.containsKey("assetType")) {
                            if ("0".equals(paramMap.get("assetType")[0])){
                                operationName = "  租赁债权";
                            }else if("1".equals(paramMap.get("assetType")[0])){
                                operationName = "  保理债权";
                            }else if ("2".equals(paramMap.get("assetType")[0])){
                                operationName = "  贷款债权";
                            }
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                try {
                    //设置对象的属性，并添加日志
                    absOperationRecord.setOperationName(operationName);
                    absOperationRecordApi.add(absOperationRecord);
                }catch (Exception e){
//                System.out.println("=========================================================添加日志失败");
//                e.printStackTrace();
                }
            }
//        System.out.println("============后置通知结束=============");
        }
    }
}
