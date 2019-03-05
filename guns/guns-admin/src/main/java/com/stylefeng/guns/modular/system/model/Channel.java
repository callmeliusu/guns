package com.stylefeng.guns.modular.system.model;

import java.io.Serializable;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 渠道表
 * </p>
 *
 * @author stylefeng123
 * @since 2019-03-05
 */
@TableName("sys_channel")
public class Channel extends Model<Channel> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Integer id;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 渠道单价
     */
    private BigDecimal channelPrice;
    /**
     * 渠道金额
     */
    private BigDecimal channelMoney;
    /**
     * 已付金额
     */
    private BigDecimal payMoney;
    /**
     * 未付金额
     */
    private BigDecimal waitPayMoney;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(BigDecimal channelPrice) {
        this.channelPrice = channelPrice;
    }

    public BigDecimal getChannelMoney() {
        return channelMoney;
    }

    public void setChannelMoney(BigDecimal channelMoney) {
        this.channelMoney = channelMoney;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getWaitPayMoney() {
        return waitPayMoney;
    }

    public void setWaitPayMoney(BigDecimal waitPayMoney) {
        this.waitPayMoney = waitPayMoney;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Channel{" +
        "id=" + id +
        ", channel=" + channel +
        ", channelPrice=" + channelPrice +
        ", channelMoney=" + channelMoney +
        ", payMoney=" + payMoney +
        ", waitPayMoney=" + waitPayMoney +
        "}";
    }
}
