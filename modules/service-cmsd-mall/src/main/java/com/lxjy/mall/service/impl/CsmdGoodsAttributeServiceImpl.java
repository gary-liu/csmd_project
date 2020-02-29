package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxjy.mall.entity.CsmdGoodsAttribute;
import com.lxjy.mall.mapper.CsmdGoodsAttributeMapper;
import com.lxjy.mall.service.CsmdGoodsAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品参数表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdGoodsAttributeServiceImpl extends ServiceImpl<CsmdGoodsAttributeMapper, CsmdGoodsAttribute> implements CsmdGoodsAttributeService {

    @Resource
    private CsmdGoodsAttributeMapper goodsAttributeMapper;

    @Override
    public List<CsmdGoodsAttribute> getByGoodsId(Integer goodsId) {

        QueryWrapper<CsmdGoodsAttribute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId).eq("deleted", false);
        return goodsAttributeMapper.selectList(queryWrapper);
    }
}
