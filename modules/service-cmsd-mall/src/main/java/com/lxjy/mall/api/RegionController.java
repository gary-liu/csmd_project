package com.lxjy.mall.api;

import com.lxjy.mall.entity.CsmdRegion;
import com.lxjy.mall.service.CsmdRegionService;
import com.lxjy.mall.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * create by gary 2020/2/27
 * 技术交流请加QQ:498982703
 * 区域服务
 */
@RestController
@Validated
@RequestMapping("/app/region")
@Slf4j
public class RegionController {

    @Autowired
    private CsmdRegionService regionService;

    /**
     * 区域数据
     * <p>
     * 根据父区域ID，返回子区域数据。 如果父区域ID是0，则返回省级区域数据；
     *
     * @param pid
     *            父区域ID
     * @return 区域数据
     */
    @GetMapping("list")
    public CommonResult list(@NotNull Integer pid) {
        log.info("【请求开始】根据pid获取子区域数据,请求参数,pid:{}", pid);
        List<CsmdRegion> regionList = regionService.queryByPid(pid);

        log.info("【请求结束】根据pid获取子区域数据成功!");
        return CommonResult.success(regionList);
    }

}
