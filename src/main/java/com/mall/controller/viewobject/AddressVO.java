package com.mall.controller.viewobject;

/**
 * @Description: 地址视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 11:21
 */
public class AddressVO {
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
}
