package com.mall.service.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 收货地址模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 10:19
 */
public class AddressModel {
    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 收件人姓名
     */
    @NotBlank(message = "收件人姓名不能为空")
    private String recipientName;

    /**
     * 收件人手机号
     */
    @NotNull(message = "收件人手机号不能为空")
    private Long recipientPhone;

    /**
     * 详细地址
     */
    @NotBlank(message = "地址不能为空")
    private String addressDetail;

    /**
     * 默认地址
     */
    private Byte defaultAddress;

    /**
     * 用户ID
     */
    private Integer userId;

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

    public Long getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(Long recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public byte getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Byte defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
