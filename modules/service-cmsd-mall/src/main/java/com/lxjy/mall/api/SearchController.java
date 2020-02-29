package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxjy.mall.annotation.LoginUser;
import com.lxjy.mall.entity.CsmdKeyword;
import com.lxjy.mall.entity.CsmdSearchHistory;
import com.lxjy.mall.service.CsmdKeywordService;
import com.lxjy.mall.service.CsmdSearchHistoryService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 公告信息
 */
@RestController
@Validated
@RequestMapping("/app/search")
@Slf4j
public class SearchController {

    @Autowired
    private CsmdKeywordService keywordsService;
    @Autowired
    private CsmdSearchHistoryService searchHistoryService;

    /**
     * 搜索页面信息
     * <p>
     * 如果用户已登录，则给出用户历史搜索记录； 如果没有登录，则给出空历史搜索记录。
     *
     * @param userId
     *            用户ID，可选
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public CommonResult index(@LoginUser Integer userId) {
        log.info("【请求开始】搜索页面信息展示,请求参数,userId:{}", userId);

        QueryWrapper<CsmdKeyword> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_default", true).eq("deleted", false);

        // 取出输入框默认的关键词
        CsmdKeyword defaultKeyword = keywordsService.getOne(queryWrapper);

        // 取出热闹关键词
        List<CsmdKeyword> hotKeywordList = keywordsService.list(new QueryWrapper<CsmdKeyword>()
                .eq("is_hot", true).eq("deleted", false));

        List<CsmdSearchHistory> historyList = null;
        if (userId != null) {
            // 取出用户历史关键字
            historyList = searchHistoryService.list(new QueryWrapper<CsmdSearchHistory>().select("distinct keyword")
            .eq("user_id",userId).eq("deleted",false));
        } else {
            historyList = new ArrayList<>(0);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyList);
        data.put("hotKeywordList", hotKeywordList);

        log.info("【请求结束】搜索页面信息展示,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 关键字提醒
     * <p>
     * 当用户输入关键字一部分时，可以推荐系统中合适的关键字。
     *
     * @param keyword
     *            关键字
     * @return 合适的关键字
     */
    @GetMapping("helper")
    public CommonResult helper(@NotEmpty String keyword, @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】关键字提醒,请求参数,keyword:{}", keyword);

        QueryWrapper<CsmdKeyword> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct keyword").like("keyword", keyword).eq("deleted", false);
        IPage<CsmdKeyword> csmdKeywordIPage = keywordsService.page(new Page<>(page, size),
                queryWrapper);
        List<CsmdKeyword> keywordsList = csmdKeywordIPage.getRecords();
        String[] keys = new String[keywordsList.size()];
        int index = 0;
        for (CsmdKeyword key : keywordsList) {
            keys[index++] = key.getKeyword();
        }

        log.info("【请求结束】关键字提醒,响应结果:{}", JSONObject.toJSONString(keys));
        return CommonResult.success(keys);
    }

    /**
     * 清除用户搜索历史
     *
     * @param userId
     *            用户ID
     * @return 清理是否成功
     */
    @PostMapping("clearhistory")
    public CommonResult clearhistory(@LoginUser Integer userId) {
        log.info("【请求开始】清除用户搜索历史,请求参数,userId:{}", userId);

        if (userId == null) {
            return CommonResult.unlogin();
        }
        searchHistoryService.remove(new QueryWrapper<CsmdSearchHistory>().eq("user_id", userId));

        log.info("【请求结束】清除用户搜索历史,响应结果:{}", "成功");
        return CommonResult.success();
    }

}
