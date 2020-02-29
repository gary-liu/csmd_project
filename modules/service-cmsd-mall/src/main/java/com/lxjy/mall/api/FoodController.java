package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.CsmdFootprint;
import com.lxjy.mall.entity.CsmdGoods;
import com.lxjy.mall.service.CsmdFootprintService;
import com.lxjy.mall.service.CsmdGoodsService;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 用户访问足迹服务
 */
@RestController
@Validated
@RequestMapping("/app/footprint")
@Slf4j
public class FoodController {

    @Autowired
    private CsmdFootprintService footprintService;
    @Autowired
    private CsmdGoodsService goodsService;

    /**
     * 删除用户足迹
     *
     * @param userId
     *            用户ID
     * @param body
     *            请求内容， { id: xxx }
     * @return 删除操作结果
     */
    @PostMapping("delete")
    public Object delete(@LoginUser Integer userId, @RequestBody String body) {
        log.info("【请求开始】删除用户足迹,请求参数,userId:{},body:{}", userId, body);

        if (userId == null) {
            log.error("删除用户足迹:用户未登录！！！");
            return CommonResult.unlogin();
        }
        if (body == null) {
            return CommonResult.badArgument();
        }

        Integer footprintId = JacksonUtil.parseInteger(body, "id");
        if (footprintId == null) {
            return CommonResult.badArgument();
        }
        CsmdFootprint footprint = footprintService.getById(footprintId);

        if (footprint == null) {
            return CommonResult.badArgumentValue();
        }
        if (!footprint.getUserId().equals(userId)) {
            return CommonResult.badArgumentValue();
        }

        footprintService.removeById(footprintId);

        log.info("【请求结束】删除用户足迹成功!");
        return CommonResult.success();
    }

    /**
     * 用户足迹列表
     *
     * @param page
     *            分页页数
     * @param size
     *            分页大小
     * @return 用户足迹列表
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId, @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】用户足迹列表查询,请求参数,userId:{}", userId);

        if (userId == null) {
            log.error("删除用户足迹:用户未登录！！！");
            return CommonResult.unlogin();
        }

        QueryWrapper<CsmdFootprint> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        IPage<CsmdFootprint> footprintIPage = footprintService.page(new Page<>(page, size), queryWrapper);
        List<CsmdFootprint> footprintList = footprintIPage.getRecords();
        long totalPages = footprintIPage.getTotal();

        List<Object> footprintVoList = new ArrayList<>(footprintList.size());
        for (CsmdFootprint footprint : footprintList) {
            Map<String, Object> c = new HashMap<String, Object>();
            c.put("id", footprint.getId());
            c.put("goodsId", footprint.getGoodsId());
            c.put("addTime", footprint.getAddTime());

            CsmdGoods goods = goodsService.getById(footprint.getGoodsId());
            c.put("name", goods.getName());
            c.put("brief", goods.getBrief());
            c.put("picUrl", goods.getPicUrl());
            c.put("retailPrice", goods.getRetailPrice());

            footprintVoList.add(c);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("footprintList", footprintVoList);
        result.put("totalPages", totalPages);

        log.info("【请求结束】添加意见反馈,响应结果:{}", JSONObject.toJSONString(result));
        return CommonResult.success(result);
    }

}
