package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxjy.mall.entity.CsmdTopic;
import com.lxjy.mall.mapper.CsmdTopicMapper;
import com.lxjy.mall.service.CsmdTopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdTopicServiceImpl extends ServiceImpl<CsmdTopicMapper, CsmdTopic> implements CsmdTopicService {

    @Resource
    private CsmdTopicMapper topicMapper;

    @Override
    public IPage<CsmdTopic> getPage(Integer page, Integer size) {

        QueryWrapper<CsmdTopic> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("add_time");

        return topicMapper.selectPage(new Page<CsmdTopic>(page, size), queryWrapper);
    }
}
