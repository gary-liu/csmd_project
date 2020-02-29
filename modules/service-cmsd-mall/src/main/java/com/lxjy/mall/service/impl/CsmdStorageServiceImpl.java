package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxjy.mall.entity.CsmdStorage;
import com.lxjy.mall.mapper.CsmdStorageMapper;
import com.lxjy.mall.service.CsmdStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 文件存储表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdStorageServiceImpl extends ServiceImpl<CsmdStorageMapper, CsmdStorage> implements CsmdStorageService {

    @Resource
    private CsmdStorageMapper storageMapper;

    @Override
    public CsmdStorage getBykey(String key) {

        QueryWrapper<CsmdStorage> storageQueryWrapper = new QueryWrapper<>();
        storageQueryWrapper.eq("key", key).eq("deleted", false);
        return storageMapper.selectOne(storageQueryWrapper);
    }
}
