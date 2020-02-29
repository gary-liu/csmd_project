package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lxjy.mall.entity.CsmdAddress;
import com.lxjy.mall.mapper.CsmdAddressMapper;
import com.lxjy.mall.service.CsmdAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdAddressServiceImpl extends ServiceImpl<CsmdAddressMapper, CsmdAddress> implements CsmdAddressService {

    @Resource
    private CsmdAddressMapper addressMapper;

    @Override
    public void resetDefault(Integer userId) {
        CsmdAddress csmdAddress = new CsmdAddress();
        csmdAddress.setUserId(userId);
        csmdAddress.setIsDefault(false);
        csmdAddress.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<CsmdAddress> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id", userId).eq("deleted", false).
                eq("is_default", true);
        addressMapper.update(csmdAddress, updateWrapper);

    }

    @Override
    public List<CsmdAddress> queryByUserId(Integer userId) {
        return addressMapper.selectList(new QueryWrapper<CsmdAddress>().
                eq("user_id", userId).eq("deleted", false));
    }
}
