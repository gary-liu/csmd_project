package com.lxjy.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lxjy.mall.entity.CsmdCategory;
import com.lxjy.mall.mapper.CsmdCategoryMapper;
import com.lxjy.mall.service.CsmdCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 类目表 服务实现类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Service
public class CsmdCategoryServiceImpl extends ServiceImpl<CsmdCategoryMapper, CsmdCategory> implements CsmdCategoryService {

    @Resource
    private CsmdCategoryMapper csmdCategoryMapper;

    @Override
    public List<CsmdCategory> queryL1() {
        return csmdCategoryMapper.selectList(new QueryWrapper<CsmdCategory>()
                .eq("level", "L1").eq("deleted", false));
    }

    @Override
    public List<CsmdCategory> queryByPid(Integer pid) {
        return csmdCategoryMapper.selectList(new QueryWrapper<CsmdCategory>()
                .eq("pid", pid).eq("deleted", false));
    }
}
