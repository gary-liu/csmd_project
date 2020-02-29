package com.lxjy.redis.controller;

import com.lxjy.redis.util.CommonResult;
import com.lxjy.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by gary 2020/2/21
 * 技术交流请加QQ:498982703
 */
@RestController
@RequestMapping("/redis")
public class RedisServerController {
    @Autowired
    private  RedisUtil redisUtil;


    @PostMapping("/setObject")
    public CommonResult setObject(@RequestParam("paramKey") String paramKey, @RequestParam("paramValue") Object value) {
        redisUtil.set(paramKey, value);
        return CommonResult.success();

    }

    @GetMapping("/getObject")
    public CommonResult getObject(@RequestParam("paramKey") String paramKey) {
        Object result = redisUtil.get(paramKey);
        return CommonResult.success("success", result);
    }




}
