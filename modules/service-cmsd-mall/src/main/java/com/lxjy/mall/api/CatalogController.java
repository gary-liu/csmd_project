package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.lxjy.mall.entity.CsmdCategory;
import com.lxjy.mall.service.CsmdCategoryService;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.HomeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 类目服务
 */
@RestController
@RequestMapping("/app/catalog")
@Validated
@Slf4j
public class CatalogController {


    @Autowired
    private CsmdCategoryService categoryService;

    /**
     * 分类详情
     *
     * @param id
     *            分类类目ID。 如果分类类目ID是空，则选择第一个分类类目。 需要注意，这里分类类目是一级类目
     * @return 分类详情
     */
    @GetMapping("index")
    public CommonResult index(Integer id) {
        log.info("【请求开始】分类详情,请求参数,id:{}", id);
        // 所有一级分类目录
        List<CsmdCategory> l1CatList = categoryService.queryL1();

        // 当前一级分类目录
        CsmdCategory currentCategory = null;
        if (id != null) {
            currentCategory = categoryService.getById(id);
        } else {
            currentCategory = l1CatList.get(0);
        }

        // 当前一级分类目录对应的二级分类目录
        List<CsmdCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);

        log.info("【请求结束】分类详情,响应结果：{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 所有分类数据
     *
     * @return 所有分类数据
     */
    @GetMapping("all")
    public CommonResult queryAll() {
        log.info("【请求开始】所有分类查询...");
        // 优先从缓存中读取
        if (HomeCacheManager.hasData(HomeCacheManager.CATALOG)) {
            return CommonResult.success(HomeCacheManager.getCacheData(HomeCacheManager.CATALOG));
        }

        // 所有一级分类目录
        List<CsmdCategory> l1CatList = categoryService.queryL1();

        // 所有子分类列表
        Map<Integer, List<CsmdCategory>> allList = new HashMap<>();
        List<CsmdCategory> sub;
        for (CsmdCategory category : l1CatList) {
            sub = categoryService.queryByPid(category.getId());
            allList.put(category.getId(), sub);
        }

        // 当前一级分类目录
        CsmdCategory currentCategory = l1CatList.get(0);

        // 当前一级分类目录对应的二级分类目录
        List<CsmdCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("allList", allList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);

        // 缓存数据
        try {
            HomeCacheManager.loadData(HomeCacheManager.CATALOG, data);
        } catch (Exception e) {
            log.error("所有分类查询出错：缓存分类数据错误：{}", e.getMessage());
            e.printStackTrace();
        }

        log.info("【请求结束】所有分类查询,响应结果：{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 当前分类栏目
     *
     * @param id
     *            分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("current")
    public CommonResult current(@NotNull Integer id) {
        log.info("【请求开始】当前分类栏目查询,id:{}", id);

        // 当前分类
        CsmdCategory currentCategory = categoryService.getById(id);
        List<CsmdCategory> currentSubCategory = categoryService.queryByPid(currentCategory.getId());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);

        log.info("【请求结束】当前分类栏目查询,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }





}
