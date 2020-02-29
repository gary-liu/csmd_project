package com.lxjy.mall.api;

import com.lxjy.mall.entity.CsmdArticle;
import com.lxjy.mall.service.CsmdArticleService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 公告信息
 */
@RestController
@Validated
@RequestMapping("/app/article")
@Slf4j
public class ArticleController {

    @Autowired
    private CsmdArticleService articleService;

    /**
     * 文章公告信息
     *
     * @param id
     *            文章ID
     * @return 文章详情
     */
    @GetMapping("detail")
    public CommonResult detail(@NotNull Integer id) {
        log.info("【请求开始】获取公告文章,请求参数,id:{}", id);
        CsmdArticle article = null;
        try {
            article = articleService.getById(id);
        } catch (Exception e) {
            log.error("获取文章公告失败,文章id：{}", id);
        }
        return CommonResult.success(article);
    }

}
