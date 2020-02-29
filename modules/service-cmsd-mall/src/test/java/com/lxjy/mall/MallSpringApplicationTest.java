package com.lxjy.mall;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.entity.CsmdAddress;
import com.lxjy.mall.entity.CsmdBrand;
import com.lxjy.mall.mapper.CsmdBrandMapper;
import com.lxjy.mall.service.CsmdAddressService;
import com.lxjy.mall.service.CsmdBrandService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * create by gary 2020/2/24
 * 技术交流请加QQ:498982703
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSpringApplicationTest {

    @Resource
    CsmdBrandMapper brandMapper;

    @Autowired
    CsmdBrandService csmdBrandService;

    @Autowired
    private CsmdAddressService addressService;

    @Test
    public void testAddress() {

        IPage<CsmdAddress> page = addressService.page(new Page<>(0, 1));
        System.out.println("+++++++++++++++++++++++++++++++++++");
        System.out.println(JSON.toJSONString(page));


    }

    @Test
    public void testBrand() {
        Page<CsmdBrand> page = csmdBrandService.page(new Page<>(0, 10));

        System.out.println("+++++++++++++++++++++++++++++++++++");
        System.out.println(JSON.toJSONString(page));


    }











}
