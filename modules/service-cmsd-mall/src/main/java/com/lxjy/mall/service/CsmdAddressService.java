package com.lxjy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxjy.mall.entity.CsmdAddress;

import java.util.List;

/**
 * <p>
 * 收货地址表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdAddressService extends IService<CsmdAddress> {


    /**
     * 取消用户的默认地址配置
     *
     * @param userId
     */
    void resetDefault(Integer userId);

    List<CsmdAddress> queryByUserId(Integer id);




}
