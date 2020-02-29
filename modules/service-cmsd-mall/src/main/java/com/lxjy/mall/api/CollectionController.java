package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.CsmdCollect;
import com.lxjy.mall.entity.CsmdGoods;
import com.lxjy.mall.service.CsmdCollectService;
import com.lxjy.mall.service.CsmdGoodsService;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 *  用户收藏服务
 */
@RestController
@RequestMapping("/app/collect")
@Validated
@Slf4j
public class CollectionController {

    @Autowired
    private CsmdCollectService collectService;
    @Autowired
    private CsmdGoodsService goodsService;

    /**
     * 用户收藏列表
     *
     * @param userId
     *            用户ID
     * @param type
     *            类型，如果是0则是商品收藏，如果是1则是专题收藏
     * @param page
     *            分页页数
     * @param size
     *            分页大小
     * @return 用户收藏列表
     */
    @GetMapping("list")
    public CommonResult list(@LoginUser Integer userId, @NotNull Byte type, @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】用户收藏列表查询,请求参数,userId:{}", userId);
        if (userId == null) {
            log.error("获取用户收藏列表查询失败:用户未登录！！！");
            return CommonResult.unlogin();
        }
        IPage<CsmdCollect> csmdCollectIPage = collectService.page(new Page<>(page, size),
                new QueryWrapper<CsmdCollect>().eq("user_id", userId).eq("type", type));

        List<CsmdCollect> collectList = csmdCollectIPage.getRecords();
        long totalPages = csmdCollectIPage.getTotal();
        List<Object> collects = new ArrayList<>(collectList.size());
        for (CsmdCollect collect : collectList) {
            Map<String, Object> c = new HashMap<String, Object>();
            c.put("id", collect.getId());
            c.put("type", collect.getType());
            c.put("valueId", collect.getValueId());

            CsmdGoods goods = goodsService.getById(collect.getValueId());
            c.put("name", goods.getName());
            c.put("brief", goods.getBrief());
            c.put("picUrl", goods.getPicUrl());
            c.put("retailPrice", goods.getRetailPrice());

            collects.add(c);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("collectList", collects);
        result.put("totalPages", totalPages);

        log.info("【请求结束】用户收藏列表查询,响应结果：{}", JSONObject.toJSONString(result));
        return CommonResult.success(result);
    }

    /**
     * 用户收藏添加或删除
     * <p>
     * 如果商品没有收藏，则添加收藏；如果商品已经收藏，则删除收藏状态。
     *
     * @param userId
     *            用户ID
     * @param body
     *            请求内容，{ type: xxx, valueId: xxx }
     * @return 操作结果
     */
    @PostMapping("addordelete")
    public CommonResult addordelete(@LoginUser Integer userId, @RequestBody String body) {
        log.info("【请求开始】用户收藏添加或删除,请求参数,userId:{},body:{}", userId, body);

        if (userId == null) {
            log.error("用户收藏添加或删除失败:用户未登录！！！");
            return CommonResult.unlogin();
        }

        Byte type = JacksonUtil.parseByte(body, "type");
        Integer valueId = JacksonUtil.parseInteger(body, "valueId");
        if (type == null || valueId == null) {
            return CommonResult.badArgument();
        }

        CsmdCollect collect =collectService.getOne(new QueryWrapper<CsmdCollect>().eq("user_id", userId)
                .eq("type", type).eq("value_id", valueId));


        String handleType = null;
        if (collect != null) {
            handleType = "delete";
            collectService.removeById(collect.getId());
        } else {
            handleType = "add";
            collect = new CsmdCollect();
            collect.setUserId(userId);
            collect.setValueId(valueId);
            collect.setType((int) type);
            collectService.saveOrUpdate(collect);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", handleType);

        log.info("【请求结束】用户收藏添加或删除,响应结果：{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

}
