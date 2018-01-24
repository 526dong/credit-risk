package com.ccx.credit.risk.manager.modelmessage;

import java.util.Map;

/**
 * @Description: 定义基础数据获取抽象类
 * @author:lilong
 * @Date: 2017/10/23
 */
public  abstract class BaseGainMsg<T,D> {

   // private T t;

    public  BaseGainMsg(){}

    /**
     * 构造方法，数据获取
     * @param dao 数据库操纵类dao传入
     * @param data 数据存储容器
     * @param key 数据存储容器中的key
     */
    public  BaseGainMsg(D dao, Map<String,Object> data,String key){
        data.put(key,initMsg(dao));
    }

    /**
     * 数据处理
     * @param dao
     */
    public abstract T  initMsg(D dao);

  /*  public T getT() {
        return t;
    }*/

   /* public void setT(T t) {
        this.t = t;
    }*/
}
