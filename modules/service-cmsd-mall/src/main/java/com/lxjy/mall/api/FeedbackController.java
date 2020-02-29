package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.CsmdFeedback;
import com.lxjy.mall.entity.CsmdUser;
import com.lxjy.mall.service.CsmdFeedbackService;
import com.lxjy.mall.service.CsmdUserService;
import com.lxjy.mall.util.CommonResult;
import com.lxjy.mall.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 意见反馈服务
 */
@RestController
@Validated
@RequestMapping("/app/feedback")
@Slf4j
public class FeedbackController {
    @Autowired
    private CsmdFeedbackService feedbackService;
    @Autowired
    private CsmdUserService userService;

    private Object validate(CsmdFeedback feedback) {
        String content = feedback.getContent();
        if (StringUtils.isEmpty(content)) {
            return CommonResult.badArgument();
        }

        String type = feedback.getFeedType();
        if (StringUtils.isEmpty(type)) {
            return CommonResult.badArgument();
        }

        Boolean hasPicture = feedback.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            feedback.setPicUrls("");
        }

        // 测试手机号码是否正确
        String mobile = feedback.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return CommonResult.badArgument();
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return CommonResult.badArgument();
        }
        return null;
    }

    /**
     * 添加意见反馈
     *
     * @param userId
     *            用户ID
     * @param feedback
     *            意见反馈
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody CsmdFeedback feedback) {
        log.info("【请求开始】添加意见反馈,请求参数,userId:{},size:{}", userId, JSONObject.toJSONString(feedback));

        if (userId == null) {
            log.error("添加意见反馈失败:用户未登录！！！");
            return CommonResult.unlogin();
        }
        Object error = validate(feedback);
        if (error != null) {
            return error;
        }

        CsmdUser user = userService.getById(userId);
        String username = user.getUsername();
        feedback.setId(null);
        feedback.setUserId(userId);
        feedback.setUsername(username);
        // 状态默认是0，1表示状态已发生变化
        feedback.setStatus(1);
        feedbackService.save(feedback);

        log.info("【请求结束】添加意见反馈,响应结果:{}", JSONObject.toJSONString(feedback));
        return CommonResult.success();
    }

}
