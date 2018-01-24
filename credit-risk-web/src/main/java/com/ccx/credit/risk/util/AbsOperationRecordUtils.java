package com.ccx.credit.risk.util;

import com.ccx.credit.risk.model.AbsOperationRecord;
import com.ccx.credit.risk.model.User;
import com.ccx.credit.risk.model.asset.AbsAsset;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 操作日志记录 工具类
 * by:ymj
 */
public class AbsOperationRecordUtils {

    private static AbsOperationRecord absOperationRecord=null;

    /**
     * 获取日志对象
     * @return
     */
    public static synchronized AbsOperationRecord getRecord(JoinPoint joinPoint) {

        if (absOperationRecord == null) {
            absOperationRecord = new AbsOperationRecord();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("risk_crm_user");

        if (null!=user){
            try {

                String targetName = joinPoint.getTarget().getClass().getName();
                String methodName = joinPoint.getSignature().getName();
                Object[] arguments = joinPoint.getArgs();

                Class targetClass = Class.forName(targetName);
                Method[] methods = targetClass.getMethods();
                String operationType = "";
                String operationBasicModule = "";
                String operationConcreteModule = "";

                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        Class[] clazzs = method.getParameterTypes();

                        if (clazzs.length == arguments.length) {
                            operationType = method.getAnnotation(Record.class).operationType();
                            operationConcreteModule = method.getAnnotation(Record.class).operationConcreteModule();
                            operationBasicModule = method.getAnnotation(Record.class).operationBasicModule();
                            break;
                        }
                    }
                }
                if (null != operationBasicModule &&operationBasicModule.trim()!=""
                        && null != operationType &&operationType.trim()!=""
                        && null != operationConcreteModule&&operationConcreteModule.trim()!="") {

                    //设置操作时间
                    Date date = new Date();
                    String dateStr = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(date);
                    absOperationRecord.setOperatTime(dateStr);

                    //设置操作人
                    absOperationRecord.setOperator(user.getLoginName());
                    absOperationRecord.setCompanyId(user.getInstitutionId());

                    //给操作日志对象赋值(操作的基础模块，具体模块和操作方法)
                    absOperationRecord.setBasicModule(operationBasicModule);
                    absOperationRecord.setConcreteModule(operationConcreteModule);
                    absOperationRecord.setOperationMethod(operationType);
                    absOperationRecord.setUrlPath(request.getRequestURI());
                    absOperationRecord.setUrlParam(JsonUtils.toJson(request.getParameterMap()));

                    return absOperationRecord;
                }
            } catch (Exception e) {
//               e.printStackTrace();
            }
        }
        return null;
    }
}
