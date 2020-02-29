package com.lxjy.mall.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账户流水表
 * </p>
 *
 * @author gary
 * @since 2020-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CsmdAccountTrace extends Model<CsmdAccountTrace> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作流水
     */
    private String traceSn;

    /**
     * 用户表的用户ID
     */
    private Integer userId;

    /**
     * 操作类型 0:系统结算 1:用户提现
     */
    private Integer type;

    /**
     * 操作金额
     */
    private BigDecimal amount;

    /**
     * 总申请金额
     */
    private BigDecimal totalAmount;

    /**
     * 申请时间
     */
    private LocalDateTime addTime;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 短信提取码
     */
    private String smsCode;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 消息内容
     */
    private String traceMsg;

    /**
     * 审批时间
     */
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
