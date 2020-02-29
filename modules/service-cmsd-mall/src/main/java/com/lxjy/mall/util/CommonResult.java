package com.lxjy.mall.util;

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
    public static String SUCCESS_MSG = "success";
    public static String ERROR_MSG = "error";

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
    public static CommonResult success(Object data){

        return CommonResult.ok().set("code", CommonResult.SUCCESS_CODE).set(CommonResult.MSG_KEY, CommonResult.SUCCESS_MSG)
                .set(CommonResult.DATA_KEY, data);
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

    public static CommonResult badArgument() {
        return CommonResult.ok().set("code", "401").set(CommonResult.MSG_KEY, "参数不对");

    }
    public static CommonResult badArgumentValue() {
        return CommonResult.ok().set("code", "402").set(CommonResult.MSG_KEY, "参数值不对");

    }

    public static CommonResult unlogin() {
        return CommonResult.ok().set("code", "501").set(CommonResult.MSG_KEY, "请登录");
    }

    public static CommonResult serious() {
        return CommonResult.ok().set("code", "502").set(CommonResult.MSG_KEY, "系统内部错误");
    }

    public static CommonResult unsupport() {
        return CommonResult.ok().set("code", "503").set(CommonResult.MSG_KEY, "业务不支持");
    }

    public static CommonResult updatedDateExpired() {
        return CommonResult.ok().set("code", "504").set(CommonResult.MSG_KEY, "更新数据已经失效");
    }

    public static CommonResult updatedDataFailed() {
        return CommonResult.ok().set("code", "505").set(CommonResult.MSG_KEY, "更新数据失败");
    }

    public static CommonResult unauthz() {

        return CommonResult.ok().set("code", "506").set(CommonResult.MSG_KEY, "无操作权限");
    }



}
