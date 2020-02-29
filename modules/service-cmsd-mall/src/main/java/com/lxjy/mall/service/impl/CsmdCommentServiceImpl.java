package com.lxjy.mall.service.impl;

import com.lxjy.mall.entity.CsmdComment;
import com.lxjy.mall.mapper.CsmdCommentMapper;
import com.lxjy.mall.service.CsmdCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdCommentServiceImpl extends ServiceImpl<CsmdCommentMapper, CsmdComment> implements CsmdCommentService {

    @Resource
    private CsmdCommentMapper csmdCommentMapper;

    @Override
    public List<CsmdComment> queryGoodsByGid(Integer id, int offset, int limit) {
        return null;
    }
}
