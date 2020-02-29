package com.lxjy.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxjy.mall.entity.CsmdTopic;

/**
 * <p>
 * 专题表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdTopicService extends IService<CsmdTopic> {

    IPage<CsmdTopic> getPage(Integer page, Integer size);

}
