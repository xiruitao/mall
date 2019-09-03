package com.mall.controller.viewobject;

import java.math.BigDecimal;

/**
 * @Description: 购物车视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 13:38
 */
public class CartVO {
    /**
     * 购物车ID
     */
    private Integer cartId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 商品ID
     */
    private Integer itemId;

    /**
     * 商品状态
     */
    private Byte shelves;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品图片
     */
    private String itemImageUrl;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 购买数量
     */
    private Integer amount;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Byte getShelves() {
        return shelves;
    }

    public void setShelves(Byte shelves) {
        this.shelves = shelves;
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
}
