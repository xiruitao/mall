package com.mall.service.model;

import java.math.BigDecimal;

/**
 * @Description: 活动商品模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/6 09:21
 */
public class PromoItemModel {
    /**
     * 活动商品ID
     */
    private Integer promoItemId;

    /**
     * 活动商品价格
     */
    private BigDecimal promoItemPrice;

    /**
     * 活动ID
     */
    private Integer promoId;

    public Integer getPromoItemId() {
        return promoItemId;
    }

    public void setPromoItemId(Integer promoItemId) {
        this.promoItemId = promoItemId;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
