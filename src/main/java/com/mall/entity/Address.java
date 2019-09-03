package com.mall.entity;

/**
 * @Description: 收件地址实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 09:53
 */
public class Address {
    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 收件人姓名
     */
    private String recipientName;

    /**
     * 收件人手机号
     */
    private Long recipientPhone;

    /**
     * 详细地址
     */
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
