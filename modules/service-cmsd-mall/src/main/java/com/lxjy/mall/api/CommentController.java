package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.CsmdComment;
import com.lxjy.mall.service.CsmdCommentService;
import com.lxjy.mall.service.CsmdGoodsService;
import com.lxjy.mall.service.CsmdTopicService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * 用户评论服务
 */
@RestController
@Validated
@RequestMapping("/app/article")
@Slf4j
public class CommentController {


    @Autowired
    private CsmdCommentService commentService;

    @Autowired
    private CsmdGoodsService goodsService;
    @Autowired
    private CsmdTopicService topicService;

    private Object validate(CsmdComment comment) {
        String content = comment.getContent();
        if (StringUtils.isEmpty(content)) {
            return CommonResult.badArgument();
        }

        Integer star = comment.getStar();
        if (star == null) {
            return CommonResult.badArgument();
        }
        if (star < 0 || star > 5) {
            return CommonResult.badArgumentValue();
        }

        Integer type = comment.getType();
        Integer valueId = comment.getValueId();
        if (type == null || valueId == null) {
            return CommonResult.badArgument();
        }
        if (type == 0) {
            if (goodsService.getById(valueId) == null) {
                return CommonResult.badArgumentValue();
            }
        } else if (type == 1) {
            if (topicService.getById(valueId) == null) {
                return CommonResult.badArgumentValue();
            }
        } else {
            return CommonResult.badArgumentValue();
        }
        Boolean hasPicture = comment.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            comment.setPicUrls("");
        }
        return null;
    }

    /**
     * 发表评论
     *
     * @param userId
     *            用户ID
     * @param comment
     *            评论内容
     * @return 发表评论操作结果
     */
    @PostMapping("post")
    public Object post(@LoginUser Integer userId, @RequestBody CsmdComment comment) {
        log.info("【请求开始】用户收藏列表查询,请求参数,userId:{},comment:{}", userId, JSONObject.toJSONString(comment));

        if (userId == null) {
            log.error("用户收藏列表查询失败:用户未登录！！！");
            return CommonResult.unlogin();
        }
        Object error = validate(comment);
        if (error != null) {
            return error;
        }
        comment.setUserId(userId);
        try {
            commentService.save(comment);
        } catch (Exception e) {
            log.error("用户收藏列表查询失败:存储评论数据到库出错！");
            e.printStackTrace();
        }

        log.info("【请求结束】用户收藏列表查询,响应内容:{}", JSONObject.toJSONString(comment));
        return CommonResult.success(comment);
    }

    /**
     * 评论数量
     *
     * @param type
     *            类型ID。 如果是0，则查询商品评论；如果是1，则查询专题评论。
     * @param valueId
     *            商品或专题ID。如果type是0，则是商品ID；如果type是1，则是专题ID。
     * @return 评论数量
     */
    @GetMapping("count")
    public Object count(@NotNull Byte type, @NotNull Integer valueId) {
        log.info("【请求开始】获取评论数量,请求参数,type:{},valueId:{}", type, valueId);
        QueryWrapper<CsmdComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type).eq("value_id", valueId).eq("deleted", false);

        int allCount = commentService.count(queryWrapper);
        queryWrapper.eq("has_picture", true);
        int hasPicCount = commentService.count(queryWrapper);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("allCount", allCount);
        data.put("hasPicCount", hasPicCount);

        log.info("【请求结束】获取评论数量,响应内容:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 评论列表
     *
     * @param type
     *            类型ID。 如果是0，则查询商品评论；如果是1，则查询专题评论。
     * @param valueId
     *            商品或专题ID。如果type是0，则是商品ID；如果type是1，则是专题ID。
     * @param showType
     *            显示类型。如果是0，则查询全部；如果是1，则查询有图片的评论。
     * @param page
     *            分页页数
     * @param size
     *            分页大小
     * @return 评论列表
     */
    @GetMapping("list")
    public Object list(@NotNull Byte type, @NotNull Integer valueId, @NotNull Integer showType,
                       @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】获取评论列表,请求参数,type:{},showType:{}", type, showType);

        QueryWrapper<CsmdComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type).eq("value_id", valueId).eq("deleted", false);
        if (showType == 0) {
            queryWrapper.eq("has_picture", false);
        } else if (showType == 1){
            queryWrapper.eq("has_picture", true);
        }else {
            throw new RuntimeException("not support ShowType");

        }
        IPage<CsmdComment> csmdCommentIPage = commentService.page(new Page<CsmdComment>(page, size), queryWrapper);

        List<CsmdComment> commentList = csmdCommentIPage.getRecords();

        long count = csmdCommentIPage.getTotal();
        List<Map<String, Object>> commentVoList = new ArrayList<>(commentList.size());
        for (CsmdComment comment : commentList) {
            Map<String, Object> commentVo = new HashMap<>();
            commentVo.put("addTime", comment.getAddTime());
            commentVo.put("content", comment.getContent());
            commentVo.put("picList", comment.getPicUrls());

//            UserInfo userInfo = userInfoService.getInfo(comment.getUserId());
//            commentVo.put("userInfo", userInfo);

   /*         String reply = commentService.queryReply(comment.getId());
            commentVo.put("reply", reply);
*/
            commentVoList.add(commentVo);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", commentVoList);
        data.put("count", count);
        data.put("currentPage", page);

        log.info("【请求结束】获取评论列表,响应内容:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

}
