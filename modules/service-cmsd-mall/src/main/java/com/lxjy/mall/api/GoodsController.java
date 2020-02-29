package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.*;
import com.lxjy.mall.service.*;
import com.lxjy.mall.util.CommonResult;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 商品服务
 */
@RestController
@Validated
@RequestMapping("/app/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private CsmdGoodsService goodsService;

    @Autowired
    private CsmdGoodsProductService productService;

    @Autowired
    private CsmdIssueService goodsIssueService;

    @Autowired
    private CsmdGoodsAttributeService goodsAttributeService;

    @Autowired
    private CsmdBrandService brandService;

    @Autowired
    private CsmdCommentService commentService;

    @Autowired
    private CsmdUserService userService;

    @Autowired
    private CsmdCollectService collectService;

    @Autowired
    private CsmdFootprintService footprintService;

    @Autowired
    private CsmdCategoryService categoryService;

    @Autowired
    private CsmdSearchHistoryService searchHistoryService;

    @Autowired
    private CsmdGoodsSpecificationService goodsSpecificationService;

    @Autowired
    private CsmdGrouponRulesService rulesService;

    private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);

    private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(16, 16, 1000, TimeUnit.MILLISECONDS,
            WORK_QUEUE, HANDLER);

    /**
     * 商品详情
     * <p>
     * 用户可以不登录。 如果用户登录，则记录用户足迹以及返回用户收藏信息。
     *
     * @param userId
     *            用户ID
     * @param id
     *            商品ID
     * @return 商品详情
     */
    @SuppressWarnings("rawtypes")
    @GetMapping("detail")
    public CommonResult detail(@LoginUser Integer userId, @NotNull Integer id) {
        log.info("【请求开始】商品详情,请求参数,userId:{},id:{}", userId, id);

        // 商品信息
        CsmdGoods info = goodsService.getById(id);

        // 商品属性
        Callable<List> goodsAttributeListCallable = () -> goodsAttributeService.getByGoodsId(id);

        // 商品规格 返回的是定制的GoodsSpecificationVo
        Callable<Object> objectCallable = () -> goodsSpecificationService.getSpecificationVoList(id);

        // 商品规格对应的数量和价格
        Callable<List> productListCallable = () -> productService.list(new QueryWrapper<CsmdGoodsProduct>()
                .eq("goods_id", id).eq("deleted", false));

        // 商品问题，这里是一些通用问题
        Callable<List> issueCallable = () -> goodsIssueService.list();

        // 商品品牌商
        Callable<CsmdBrand> brandCallable = () -> {
            Integer brandId = info.getBrandId();
            CsmdBrand brand;
            if (brandId == 0) {
                brand = new CsmdBrand();
            } else {
                brand = brandService.getById(info.getBrandId());
            }
            return brand;
        };

        // 评论
       /* Callable<Map> commentsCallable = () -> {
            List<CsmdComment> comments = commentService.queryGoodsByGid(id, 0, 2);
            List<Map<String, Object>> commentsVo = new ArrayList<>(comments.size());
            long commentCount = PageInfo.of(comments).getTotal();
            for (CsmdComment comment : comments) {
                Map<String, Object> c = new HashMap<>();
                c.put("id", comment.getId());
                c.put("addTime", comment.getAddTime());
                c.put("content", comment.getContent());
                CsmdUser user = userService.getById(comment.getUserId());
                c.put("nickname", user.getNickname());
                c.put("avatar", user.getAvatar());
                c.put("picList", comment.getPicUrls());
                commentsVo.add(c);
            }
            Map<String, Object> commentList = new HashMap<>();
            commentList.put("count", commentCount);
            commentList.put("data", commentsVo);
            return commentList;
        };
*/
        // 团购信息
     /*   Callable<List> grouponRulesCallable = () -> rulesService.queryByGoodsId(id);

        // 用户收藏
        int userHasCollect = 0;
        if (userId != null) {
            userHasCollect = collectService.count(userId, id);
        }

        // 记录用户的足迹 异步处理
        if (userId != null) {
            executorService.execute(() -> {
                CsmdFootprint footprint = new CsmdFootprint();
                footprint.setUserId(userId);
                footprint.setGoodsId(id);
                footprintService.save(footprint);
                short num = 1;
                productService.addBrowse(id, num);// 新增商品点击量
            });
        }*/

        FutureTask<List> goodsAttributeListTask = new FutureTask<>(goodsAttributeListCallable);
        FutureTask<Object> objectCallableTask = new FutureTask<>(objectCallable);
        FutureTask<List> productListCallableTask = new FutureTask<>(productListCallable);
        FutureTask<List> issueCallableTask = new FutureTask<>(issueCallable);
//        FutureTask<Map> commentsCallableTsk = new FutureTask<>(commentsCallable);
        FutureTask<CsmdBrand> brandCallableTask = new FutureTask<>(brandCallable);
//        FutureTask<List> grouponRulesCallableTask = new FutureTask<>(grouponRulesCallable);

        executorService.submit(goodsAttributeListTask);
        executorService.submit(objectCallableTask);
        executorService.submit(productListCallableTask);
        executorService.submit(issueCallableTask);
//        executorService.submit(commentsCallableTsk);
        executorService.submit(brandCallableTask);
//        executorService.submit(grouponRulesCallableTask);

        Map<String, Object> data = new HashMap<>();

        try {
            data.put("info", info);
//            data.put("userHasCollect", userHasCollect);
            data.put("issue", issueCallableTask.get());
//            data.put("comment", commentsCallableTsk.get());
            data.put("specificationList", objectCallableTask.get());
            data.put("productList", productListCallableTask.get());
            data.put("attribute", goodsAttributeListTask.get());
            data.put("brand", brandCallableTask.get());
//            data.put("groupon", grouponRulesCallableTask.get());
        } catch (Exception e) {
            log.error("获取商品详情出错:{}", e.getMessage());
            e.printStackTrace();
        }

        // 商品分享图片地址
        data.put("shareImage", info.getShareUrl());

        log.info("【请求结束】获取商品详情成功!");// 这里不打印返回的信息，因为此接口查询量大，太耗日志空间
        return CommonResult.success(data);
    }

    /**
     * 商品分类类目
     *
     * @param id
     *            分类类目ID
     * @return 商品分类类目
     */
    @GetMapping("category")
    public Object category(@NotNull Integer id) {
        log.info("【请求开始】商品分类类目,请求参数,id:{}", id);

        CsmdCategory cur = categoryService.getById(id);
        CsmdCategory parent = null;
        List<CsmdCategory> children = null;

        if (cur.getPid() == 0) {
            parent = cur;
            children = categoryService.queryByPid(cur.getId());
            cur = children.size() > 0 ? children.get(0) : cur;
        } else {
            parent = categoryService.getById(cur.getPid());
            children = categoryService.queryByPid(cur.getPid());
        }
        Map<String, Object> data = new HashMap<>();
        data.put("currentCategory", cur);
        data.put("parentCategory", parent);
        data.put("brotherCategory", children);

        log.info("【请求结束】商品分类类目,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 根据条件搜素商品
     * <p>
     * 1. 这里的前五个参数都是可选的，甚至都是空 2. 用户是可选登录，如果登录，则记录用户的搜索关键字
     *
     * @param categoryId
     *            分类类目ID，可选
     * @param brandId
     *            品牌商ID，可选
     * @param keyword
     *            关键字，可选
     * @param isNew
     *            是否新品，可选
     * @param isHot
     *            是否热买，可选
     * @param userId
     *            用户ID
     * @param page
     *            分页页数
     * @param size
     *            分页大小

     * @param order
     *            排序类型，顺序或者降序
     * @return 根据条件搜素的商品详情
     */
    @GetMapping("list")
    public Object list(Integer categoryId, Integer brandId, String keyword, Boolean isNew, Boolean isHot,
                       @LoginUser Integer userId, @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(defaultValue = "desc") String order) {

        log.info("【请求开始】根据条件搜素商品,请求参数,categoryId:{},brandId:{},keyword:{}", categoryId, brandId, keyword);

        // 添加到搜索历史
        if (userId != null && !StringUtils.isNullOrEmpty(keyword)) {
            CsmdSearchHistory searchHistoryVo = new CsmdSearchHistory();
            searchHistoryVo.setKeyword(keyword);
            searchHistoryVo.setUserId(userId);
            searchHistoryVo.setFrom("wx");
            searchHistoryService.save(searchHistoryVo);
        }
//
//        // 查询列表数据
//        List<CsmdGoods> goodsList = goodsService.querySelective(categoryId, brandId, keyword, isHot, isNew, page, size,
//                sort, order);

        // 查询商品所属类目列表。
     /*   List<Integer> goodsCatIds = goodsService.getCatIds(brandId, keyword, isHot, isNew);
        List<CsmdCategory> categoryList = null;
        if (goodsCatIds.size() != 0) {
            categoryList = categoryService.queryL2ByIds(goodsCatIds);
        } else {
            categoryList = new ArrayList<>(0);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goodsList", goodsList);
        data.put("count", PageInfo.of(goodsList).getTotal());
        data.put("filterCategoryList", categoryList);*/
/*
        log.info("【请求结束】根据条件搜素商品,响应结果:查询的商品数量:{}", goodsList.size());
        return CommonResult.success(data);*/

        return null;
    }

    /**
     * 商品详情页面“大家都在看”推荐商品
     *
     * @param id,
     *            商品ID
     * @return 商品详情页面推荐商品
     */
    @GetMapping("related")
    public Object related(@NotNull Integer id) {
        log.info("【请求开始】商品详情页面“大家都在看”推荐商品,请求参数,id:{}", id);

        CsmdGoods goods = goodsService.getById(id);
        if (goods == null) {
            return CommonResult.badArgumentValue();
        }

        // 目前的商品推荐算法仅仅是推荐同类目的其他商品
        int cid = goods.getCategoryId().intValue();
        int brandId = goods.getBrandId().intValue();

        // 查找六个相关商品,同店铺，同类优先
       /* int limitBid = 10;
        List<CsmdGoods> goodsListBrandId = goodsService.queryByBrandId(brandId, cid, 0, limitBid);
        List<CsmdGoods> relatedGoods = goodsListBrandId == null ? new ArrayList<CsmdGoods>() : goodsListBrandId;
        if (goodsListBrandId == null || goodsListBrandId.size() < 6) {// 同店铺，同类商品小于6件，则获取其他店铺同类商品
            int limitCid = 6;
            List<CsmdGoods> goodsListCategory = goodsService.queryByCategoryAndNotSameBrandId(brandId, cid, 0, limitCid);
            relatedGoods.addAll(goodsListCategory);
        }*/

       /* Map<String, Object> data = new HashMap<>();
        data.put("goodsList", relatedGoods);

        log.info("【请求结束】商品详情页面“大家都在看”推荐商品,响应结果:查询的商品数量:{}", relatedGoods.size());
        return CommonResult.success(data);*/
        return null;
    }

    /**
     * 在售的商品总数
     *
     * @return 在售的商品总数
     */
    @GetMapping("count")
    public Object count() {
        log.info("【请求开始】在售的商品总数...");
//        Integer goodsCount = goodsService.queryOnSale();
        Map<String, Object> data = new HashMap<>();
//        data.put("goodsCount", goodsCount);

        log.info("【请求结束】在售的商品总数,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

  

}
