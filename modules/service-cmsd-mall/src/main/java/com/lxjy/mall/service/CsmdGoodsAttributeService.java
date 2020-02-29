package com.lxjy.mall.service;

import com.lxjy.mall.entity.CsmdGoodsAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品参数表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdGoodsAttributeService extends IService<CsmdGoodsAttribute> {

    List<CsmdGoodsAttribute> getByGoodsId(Integer goodsId);


}
