package com.lxjy.mall.service;

import com.lxjy.mall.entity.CsmdGoodsSpecification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品规格表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdGoodsSpecificationService extends IService<CsmdGoodsSpecification> {

    Object getSpecificationVoList(Integer goodsId);

    List<CsmdGoodsSpecification> queryByGid(Integer goodsId);

}
