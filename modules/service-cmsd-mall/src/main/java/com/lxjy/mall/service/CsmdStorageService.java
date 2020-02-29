package com.lxjy.mall.service;

import com.lxjy.mall.entity.CsmdStorage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文件存储表 服务类
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
public interface CsmdStorageService extends IService<CsmdStorage> {


    CsmdStorage getBykey(String key);

}
