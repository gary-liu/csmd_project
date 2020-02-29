package com.lxjy.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商会表
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CsmdCommerce extends Model<CsmdCommerce> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商会名称
     */
    private String name;

    /**
     * 商会地址
     */
    private String address;

    /**
     * 商会介绍
     */
    private String describe;

    /**
     * 商会联系方式
     */
    private String phone;

    /**
     * 背景图片
     */
    private String backPhoto;

    /**
     * 商会logo
     */
    private String logoUrl;

    /**
     * 分享链接
     */
    private String shareUrl;

    /**
     * 关注度
     */
    private Integer watchingStar;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 是否认证 0 否 1 是 
     */
    private Integer authenFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态 0 未删除 1 删除
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 修改人
     */
    private String updateUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
