package com.lxjy.mall.api;

import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.service.*;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.HomeCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * create by gary 2020/2/26
 * 技术交流请加QQ:498982703
 */
@RestController
@RequestMapping("/index")
@Slf4j
public class IndexController {

    private CsmdAdService adService;

    @Autowired
    private CsmdGoodsService goodsService;

    @Autowired
    private CsmdBrandService brandService;

    @Autowired
    private CsmdTopicService topicService;

    @Autowired
    private CsmdCategoryService categoryService;

    @Autowired
    private CsmdGrouponRulesService grouponRulesService;

    @Autowired
    private CsmdCouponService couponService;

    @Autowired
    private CsmdArticleService articleService;

    private final static ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);
    private final static RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    private static ThreadPoolExecutor executorService =
            new ThreadPoolExecutor(9, 9, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);


    @GetMapping("/clearCache")
    public CommonResult clearCache(@NotNull String key) {
        log.info("key:{}", key);
        if (!StringUtils.equals(key, HomeCacheManager.CACHE)) {
            log.error("清除缓存失败：非平台标识");
            return CommonResult.error();
        }
        HomeCacheManager.clearAll();
        return CommonResult.success();

    }

    /**
     * 当用户登录时的首页数据
     * @param userId
     * @return
     */
    @GetMapping("/getIndex")
    public CommonResult getIndex(@LoginUser Integer userId) {
        log.info("用户登录首页数据，userId:{}", userId);
        Map<String, Object> data = new HashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Callable<List> couponListCallable = null;
        /*try {

            if (userId == null) {// 调整，用户未登录，不发送优惠券
                couponListCallable = () -> couponService.queryList(0, 3);
            } else {
                couponListCallable = () -> couponService.queryAvailableList(userId, 0, 3);
            }
            FutureTask<List> couponListTask = new FutureTask<>(couponListCallable);
            executorService.submit(couponListTask);
            // 优先从缓存中读取
            if (HomeCacheManager.hasData(HomeCacheManager.INDEX)) {
                data = HomeCacheManager.getCacheData(HomeCacheManager.INDEX);
                if (data != null) {// 加上这个判断，排除判断后到获取数据之间时间段清理的情况
                    log.info("访问首页,存在缓存数据，除用户优惠券信息外，加载缓存数据");
                    data.put("couponList", couponListTask.get());
                    return CommonResult.success(data);
                }
            }

            Callable<List> bannerListCallable = () -> adService.queryIndex();

            Callable<List> articleListCallable = () -> articleService.queryList(0, 5, "add_time", "desc");

            Callable<List> channelListCallable = () -> categoryService.queryChannel();

            Callable<List> newGoodsListCallable = () -> goodsService.queryByNew(0, SystemConfig.getNewLimit());

            Callable<List> hotGoodsListCallable = () -> goodsService.queryByHot(0, SystemConfig.getHotLimit());

            Callable<List> brandListCallable = () -> brandService.queryVO(0, SystemConfig.getBrandLimit());

            Callable<List> topicListCallable = () -> topicService.queryList(0, SystemConfig.getTopicLimit());

            // 团购专区
            Callable<List> grouponListCallable = () -> grouponRulesService.queryList(0, 5);

            Callable<List> floorGoodsListCallable = this::getCategoryList;

            FutureTask<List> bannerTask = new FutureTask<>(bannerListCallable);
            FutureTask<List> articleTask = new FutureTask<>(articleListCallable);
            FutureTask<List> channelTask = new FutureTask<>(channelListCallable);
            FutureTask<List> newGoodsListTask = new FutureTask<>(newGoodsListCallable);
            FutureTask<List> hotGoodsListTask = new FutureTask<>(hotGoodsListCallable);
            FutureTask<List> brandListTask = new FutureTask<>(brandListCallable);
            FutureTask<List> topicListTask = new FutureTask<>(topicListCallable);
            FutureTask<List> grouponListTask = new FutureTask<>(grouponListCallable);
            FutureTask<List> floorGoodsListTask = new FutureTask<>(floorGoodsListCallable);

            executorService.submit(bannerTask);
            executorService.submit(articleTask);
            executorService.submit(channelTask);
            executorService.submit(newGoodsListTask);
            executorService.submit(hotGoodsListTask);
            executorService.submit(brandListTask);
            executorService.submit(topicListTask);
            executorService.submit(grouponListTask);
            executorService.submit(floorGoodsListTask);

            data.put("banner", bannerTask.get());
            data.put("articles", articleTask.get());
            data.put("channel", channelTask.get());
            data.put("couponList", couponListTask.get());
            data.put("newGoodsList", newGoodsListTask.get());
            data.put("hotGoodsList", hotGoodsListTask.get());
            data.put("brandList", brandListTask.get());
            data.put("topicList", topicListTask.get());
            data.put("grouponList", grouponListTask.get());
            data.put("floorGoodsList", floorGoodsListTask.get());

            // 缓存数据首页缓存数据
            HomeCacheManager.loadData(HomeCacheManager.INDEX, data);
            executorService.shutdown();

        } catch (Exception e) {
            log.error("首页信息获取失败：{}", e.getMessage());
        }
*/
        return CommonResult.success(data);



    }

   /* private List<Map> getCategoryList() {
        List<Map> categoryList = new ArrayList<>();
        List<CsmdCategory> catL1List = categoryService.queryL1WithoutRecommend(0, SystemConfig.getCatlogListLimit());
        for (CsmdCategory catL1 : catL1List) {
            List<CsmdCategory> catL2List = categoryService.queryByPid(catL1.getId());
            List<Integer> l2List = new ArrayList<>();
            for (CsmdCategory catL2 : catL2List) {
                l2List.add(catL2.getId());
            }

            List<CsmdGoods> categoryGoods;
            if (l2List.size() == 0) {
                categoryGoods = new ArrayList<>();
            } else {
                categoryGoods = goodsService.queryByCategory(l2List, 0, SystemConfig.getCatlogMoreLimit());
            }

            Map<String, Object> catGoods = new HashMap<>();
            catGoods.put("id", catL1.getId());
            catGoods.put("name", catL1.getName());
            catGoods.put("goodsList", categoryGoods);
            categoryList.add(catGoods);
        }
        return categoryList;
    }*/







}
