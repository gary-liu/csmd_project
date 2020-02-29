package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxjy.mall.entity.CsmdGoodsSpecification;
import com.lxjy.mall.mapper.CsmdGoodsSpecificationMapper;
import com.lxjy.mall.service.CsmdGoodsSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品规格表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdGoodsSpecificationServiceImpl extends ServiceImpl<CsmdGoodsSpecificationMapper, CsmdGoodsSpecification> implements CsmdGoodsSpecificationService {

    @Resource
    private CsmdGoodsSpecificationMapper goodsSpecificationMapper;
    


    @Override
    public Object getSpecificationVoList(Integer goodsId) {

        List<CsmdGoodsSpecification> goodsSpecificationList = queryByGid(goodsId);

        Map<String, VO> map = new HashMap<>();
        List<VO> specificationVoList = new ArrayList<>();

        for (CsmdGoodsSpecification goodsSpecification : goodsSpecificationList) {
            String specification = goodsSpecification.getSpecification();
            VO goodsSpecificationVo = map.get(specification);
            if (goodsSpecificationVo == null) {
                goodsSpecificationVo = new VO();
                goodsSpecificationVo.setName(specification);
                List<CsmdGoodsSpecification> valueList = new ArrayList<>();
                valueList.add(goodsSpecification);
                goodsSpecificationVo.setValueList(valueList);
                map.put(specification, goodsSpecificationVo);
                specificationVoList.add(goodsSpecificationVo);
            } else {
                List<CsmdGoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                valueList.add(goodsSpecification);
            }
        }

        return specificationVoList;
    }

    @Override
    public List<CsmdGoodsSpecification> queryByGid(Integer goodsId) {
        return goodsSpecificationMapper.selectList(new QueryWrapper<CsmdGoodsSpecification>().
                eq("goods_id", goodsId).eq("deleted", false));
    }

    private class VO {
        private String name;
        private List<CsmdGoodsSpecification> valueList;

        @SuppressWarnings("unused")
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CsmdGoodsSpecification> getValueList() {
            return valueList;
        }

        public void setValueList(List<CsmdGoodsSpecification> valueList) {
            this.valueList = valueList;
        }
    }
}
