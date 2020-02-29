package com.lxjy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxjy.mall.entity.CsmdComment;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdCommentService extends IService<CsmdComment> {

    List<CsmdComment> queryGoodsByGid(Integer id, int offset, int limit);

}
