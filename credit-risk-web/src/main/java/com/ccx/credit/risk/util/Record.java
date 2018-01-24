package com.ccx.credit.risk.util;

/**
 * 自定义注解 for AbsOperationRecord
 *
 * by : ymj
 */


import java.lang.annotation.*;
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Record {

    /** 要执行的操作类型比如：add操作 **/
    public String operationType() default "";

    /** 执行方法的模块：大中型企业内部评级 **/
    public String operationBasicModule() default "";

    /** 执行方法的模块：大中型企业内部评级 **/
    public String operationConcreteModule() default "";
}
