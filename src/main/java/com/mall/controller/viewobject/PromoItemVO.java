package com.mall.controller.viewobject;

import java.math.BigDecimal;

/**
 * @Description: 活动商品视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 12:24
 */
public class PromoItemVO {
    /**
     * 活动商品ID
     */
    private Integer promoItemId;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品类名
     */
    private String itemCategoryName;

    /**
     * 活动商品价格
     */
    private BigDecimal promoItemPrice;

    /**
     * 商品原价
     */
    private BigDecimal itemPrice;

    /**
     * 商品描述（文字）
     */
    private String itemDescription;

    /**
     * 商品图片URL
     */
    private String itemImageUrl;

    /**
     * 商品销量
     */
    private Integer itemSales;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public BigDecimal getPromoItemPrice() {
        return promoItemPrice;
    }

    public void setPromoItemPrice(BigDecimal promoItemPrice) {
        this.promoItemPrice = promoItemPrice;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public Integer getItemSales() {
        return itemSales;
    }

    public void setItemSales(Integer itemSales) {
        this.itemSales = itemSales;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
