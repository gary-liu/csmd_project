package com.lxjy.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 品牌商表
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CsmdBrand extends Model<CsmdBrand> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 品牌商名称
     */
    private String name;

    /**
     * 品牌商简介
     */
    private String descr;

    /**
     * 品牌商页的品牌商图片
     */
    private String picUrl;

    private Integer sortOrder;

    /**
     * 品牌商的商品低价，仅用于页面展示
     */
    private BigDecimal floorPrice;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 分享二维码图片
     */
    private String shareUrl;

    /**
     * 管理用户id
     */
    private Integer adminId;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean deleted;

    private String commpany;

    /**
     * 自动监控更新商品
     */
    private Boolean autoUpdateGood;

    /**
     * 店铺url地址
     */
    private String shopUrl;

    /**
     * 默认的商品类别id
     */
    private Integer defaultCategoryId;

    /**
     * 默认商品页面数
     */
    private Integer defaultPages;

    /**
     * 店铺商品溢价
     */
    private Integer addPrecent;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
