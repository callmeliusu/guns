package com.stylefeng.guns.modular.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 每日收支表
 * </p>
 *
 * @author stylefeng123
 * @since 2019-01-10
 */
public class OrderDTO implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 日期
     */
    private String dataString;
    /**
     * 客户id
     */
    private Integer customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 下单量级
     */
    private BigDecimal order;
    /**
     * 渠道量级
     */
    private BigDecimal channelOrder;
    /**
     * 客户单价
     */
    private BigDecimal customerPrice;
    /**
     * 渠道单价
     */
    private BigDecimal channelPrice;
    /**
     * 收入
     */
    private BigDecimal income;
    /**
     * 渠道金额
     */
    private BigDecimal channelMoney;
    /**
     * 利润
     */
    private BigDecimal profit;
    /**
     * 实际收入
     */
    private BigDecimal realIncome;
    /**
     * 实际量级
     */
    private BigDecimal realOrder;
    /**
     * 实际利润
     */
    private BigDecimal realProfit;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public BigDecimal getOrder() {
        return order;
    }

    public void setOrder(BigDecimal order) {
        this.order = order;
    }

    public BigDecimal getChannelOrder() {
        return channelOrder;
    }

    public void setChannelOrder(BigDecimal channelOrder) {
        this.channelOrder = channelOrder;
    }

    public BigDecimal getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(BigDecimal customerPrice) {
        this.customerPrice = customerPrice;
    }

    public BigDecimal getChannelPrice() {
        return channelPrice;
    }

    public void setChannelPrice(BigDecimal channelPrice) {
        this.channelPrice = channelPrice;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getChannelMoney() {
        return channelMoney;
    }

    public void setChannelMoney(BigDecimal channelMoney) {
        this.channelMoney = channelMoney;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

    public BigDecimal getRealOrder() {
        return realOrder;
    }

    public void setRealOrder(BigDecimal realOrder) {
        this.realOrder = realOrder;
    }

    public BigDecimal getRealProfit() {
        return realProfit;
    }

    public void setRealProfit(BigDecimal realProfit) {
        this.realProfit = realProfit;
    }

}
