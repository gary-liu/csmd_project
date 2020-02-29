package com.lxjy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxjy.mall.entity.CsmdRegion;

import java.util.List;

/**
 * <p>
 * 行政区域表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdRegionService extends IService<CsmdRegion> {

    CsmdRegion findByPid(Integer pid);

    List<CsmdRegion> getAll();

    List<CsmdRegion> queryByPid(Integer parentId);

    List<CsmdRegion> querySelective(String name, Integer code, Integer page,
                                    Integer size, String sort, String order);

    List<CsmdRegion> getDtsRegions();







}
