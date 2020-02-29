package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.entity.CsmdBrand;
import com.lxjy.mall.service.CsmdBrandService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 品牌供应商
 */
@RestController
@Validated
@RequestMapping("/app/brand")
@Slf4j
public class BrandController {


    @Autowired
    private CsmdBrandService brandService;


    /**
     * 品牌列表
     *
     * @param page
     *            分页页数
     * @param size
     *            分页大小
     * @return 品牌列表
     */
    @GetMapping("list")
    public CommonResult list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】品牌列表,请求参数,page:{},size:{}", page, size);
        QueryWrapper<CsmdBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false)
                .orderByDesc("add_time");
        IPage<CsmdBrand> brandIPage = brandService.page(new Page<CsmdBrand>(page, size), queryWrapper);

        log.info("【请求结束】品牌列表,响应结果：{}", JSONObject.toJSONString(brandIPage));
        return CommonResult.success(brandIPage);
    }

    /**
     * 品牌详情
     *
     * @param id
     *            品牌ID
     * @return 品牌详情
     */
    @GetMapping("detail")
    public CommonResult detail(@NotNull Integer id) {
        log.info("【请求开始】品牌详情,请求参数,id:{}", id);
        CsmdBrand entity = brandService.getById(id);
        if (entity == null) {
            log.error("品牌商获取失败,id:{}", id);
            return CommonResult.badArgumentValue();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brand", entity);

        log.info("【请求结束】品牌详情,响应结果：{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

}
