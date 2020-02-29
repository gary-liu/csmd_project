package com.lxjy.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxjy.mall.entity.CsmdCategory;

import java.util.List;

/**
 * <p>
 * 类目表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdCategoryService extends IService<CsmdCategory> {


    List<CsmdCategory> queryL1();

    List<CsmdCategory> queryByPid(Integer pid);

}
