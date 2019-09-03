package com.mall.service.model;

import java.math.BigDecimal;

/**
 * @Description: 订单商品模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/19 10:47
 */
public class OrderItemModel {
    /**
     * 订单商品表主键ID
     */
    private Integer orderItemId;

    /**
     * 订单商品ID
     */
    private Integer itemId;

    /**
     * 订单商品名
     */
    private String itemName;

    /**
     * 商品图片
     */
    private String itemImageUrl;

    /**
     * 商品价格
     */
    private BigDecimal itemPrice;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 订单号
     */
    private String orderId;

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
