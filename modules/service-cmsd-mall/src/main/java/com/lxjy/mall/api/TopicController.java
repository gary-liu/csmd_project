package com.lxjy.mall.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lxjy.mall.entity.CsmdGoods;
import com.lxjy.mall.entity.CsmdTopic;
import com.lxjy.mall.service.CsmdGoodsService;
import com.lxjy.mall.service.CsmdTopicService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
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
@RequestMapping("/app/topic")
@Slf4j
public class TopicController {

    @Autowired
    private CsmdTopicService topicService;
    @Autowired
    private CsmdGoodsService goodsService;

    /**
     * 专题列表
     *
     * @param page
     *            分页页数
     * @param size
     *            分页大小
     * @return 专题列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size ) {

        log.info("【请求开始】获取专题列表,请求参数,page:{},size:{}", page, size);

        IPage<CsmdTopic> iPage = topicService.getPage(page, size);
        List<CsmdTopic> topicList = iPage.getRecords();
        long total = iPage.getTotal();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", topicList);
        data.put("count", total);

        log.info("【请求结束】获取专题列表,响应结果:{}", JSONObject.toJSONString(data));
        return CommonResult.success(data);
    }

    /**
     * 专题详情
     *
     * @param id
     *            专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@NotNull Integer id) {
        log.info("【请求开始】获取专题详情,请求参数,id:{}", id);

        Map<String, Object> data = new HashMap<>();
        CsmdTopic topic = topicService.getById(id);
        data.put("topic", topic);
        List<CsmdGoods> goods = new ArrayList<>();
       /* for (String i : topic.getGoods()) {
            CsmdGoods good = goodsService.findByIdVO(i);
            if (null != good)
                goods.add(good);
        }*/
        data.put("goods", goods);

        log.info("【请求结束】获取专题详情,响应结果:{}", "成功");
        return CommonResult.success(data);
    }

    /**
     * 相关专题
     *
     * @param id
     *            专题ID
     * @return 相关专题
     */
    @GetMapping("related")
    public Object related(@NotNull Integer id) {
        log.info("【请求开始】相关专题列表,请求参数,id:{}", id);

//        List<CsmdTopic> topicRelatedList = topicService.queryRelatedList(id, 0, 6);
        List<CsmdTopic> topicRelatedList = null;

        log.info("【请求结束】相关专题列表,响应结果:相关专题数{}", topicRelatedList == null ? 0 : topicRelatedList.size());
        return CommonResult.success(topicRelatedList);
    }
}
