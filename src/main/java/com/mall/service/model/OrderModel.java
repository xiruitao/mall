package com.mall.service.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 订单模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 10:03
 */
public class OrderModel {
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 订单状态（0：待支付；1：进行中；2：完成）
     */
    private Byte orderStatus;

    /**
     * 产生积分数
     */
    private Integer pointsNumber;

    /**
     * 订单收货地址
     */
    private Integer addressId;

    /**
     * 收货人姓名
     */
    private String recipientName;

    /**
     * 联系电话
     */
    private String recipientPhone;

    /**
     * 详细地址
     */
    private String addressDetail;

    /**
     * 订单创建时间
     */
    private Date createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPointsNumber() {
        return pointsNumber;
    }

    public void setPointsNumber(Integer pointsNumber) {
        this.pointsNumber = pointsNumber;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
