package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxjy.mall.entity.CsmdRegion;
import com.lxjy.mall.mapper.CsmdRegionMapper;
import com.lxjy.mall.service.CsmdRegionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 行政区域表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdRegionServiceImpl extends ServiceImpl<CsmdRegionMapper, CsmdRegion> implements CsmdRegionService {

    private static List<CsmdRegion> csmdRegions;

    @Resource
    private CsmdRegionMapper regionMapper;

    @Override
    public List<CsmdRegion> getDtsRegions() {
        if (csmdRegions == null) {
            createRegion();
        }
        return csmdRegions;
    }

    private synchronized void createRegion() {
        if (csmdRegions == null) {
            csmdRegions = getAll();
        }
    }

    @Override
    public CsmdRegion findByPid(Integer pid) {
        return regionMapper.selectOne(new QueryWrapper<CsmdRegion>().eq("pid", pid));
    }

    @Override
    public List<CsmdRegion> getAll() {
        return regionMapper.selectList(new QueryWrapper<CsmdRegion>().ne("type", 4));
    }

    @Override
    public List<CsmdRegion> queryByPid(Integer parentId) {
        return regionMapper.selectList(new QueryWrapper<CsmdRegion>().eq("parent_id", parentId));
    }

    @Override
    public List<CsmdRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        QueryWrapper<CsmdRegion> queryWrapper = new QueryWrapper<>();
//        queryWrapper.
        
        return null;
    }
}
