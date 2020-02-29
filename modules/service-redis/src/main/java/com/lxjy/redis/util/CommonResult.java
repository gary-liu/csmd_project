package com.lxjy.redis.util;

import java.util.HashMap;

/**
 * create by gary 2020/2/21
 * 技术交流请加QQ:498982703
 */
public class CommonResult extends HashMap {

    public static  String SUCCESS_CODE="200";
    public static String ERROR_CODE="500";
    public static String DATA_KEY = "data";
    public static String MSG_KEY = "msg";

    private CommonResult(){

    }

    public CommonResult set(String key, Object object){
        super.put(key,object);
        return  this;
    }

    private  static CommonResult ok(){
        return new CommonResult();
    }

    public static CommonResult success(){

        return CommonResult.ok().set("code", CommonResult.SUCCESS_CODE).set(CommonResult.MSG_KEY,"操作成功");
    }

    public static CommonResult success(String msg){

        return CommonResult.ok().set("code", CommonResult.SUCCESS_CODE).set(CommonResult.MSG_KEY,msg);
    }

    public static CommonResult success(String msg, Object object){

        return CommonResult.ok().set("code", CommonResult.SUCCESS_CODE).set(CommonResult.MSG_KEY,msg).set(CommonResult.DATA_KEY,object);
    }

    public CommonResult data(Object obj){
        return this.set("data",obj);
    }

    public static CommonResult error(){
        return CommonResult.ok().set(CommonResult.MSG_KEY,"操作失败").set("code", CommonResult.ERROR_CODE);
    }

    public static CommonResult error(String msg){
        return CommonResult.ok().set(CommonResult.MSG_KEY,msg).set("code", CommonResult.ERROR_CODE);
    }

    public static CommonResult error(String msg, Object object){
        return CommonResult.ok().set(CommonResult.MSG_KEY,msg).set(CommonResult.DATA_KEY,object).set("code", CommonResult.ERROR_CODE);
    }
}
