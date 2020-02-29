package com.lxjy.redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 获取key
     * @param key
     * @return
     */
    public  Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 存取key
     * @param key
     * @param value
     */
    public  void set(String key,Object value){
         redisTemplate.opsForValue().set(key,value);
    }

    public boolean hasKey(Object  key){
        return redisTemplate.hasKey(key);
    }

    public boolean delete(Object key){
        return redisTemplate.delete(key);

    }

    /**
     *
     * @param key  key值
     * @param timout 过期时间
     * @param unit  过期时间单位
     * @return
     */
    public boolean expire(Object key, long timout, TimeUnit unit) {
        return redisTemplate.expire(key, timout, unit);
    }

    /**
     * 提定这个key在什么时候过期
     * @param key
     * @param data
     * @return
     */
    public boolean expireAt(Object key, Date data) {
        return redisTemplate.expireAt(key, data);
    }

}
