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
 * 
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CsmdGroupon extends Model<CsmdGroupon> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联的订单ID
     */
    private Integer orderId;

    /**
     * 参与的团购ID，仅当user_type不是1
     */
    private Integer grouponId;

    /**
     * 团购规则ID，关联csmd_groupon_rules表ID字段
     */
    private Integer rulesId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 创建者ID
     */
    private Integer creatorUserId;

    /**
     * 创建时间
     */
    private LocalDateTime addTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 团购分享图片地址
     */
    private String shareUrl;

    /**
     * 是否已经支付
     */
    private Boolean payed;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Boolean deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
