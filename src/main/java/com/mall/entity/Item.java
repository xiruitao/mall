package com.mall.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商品实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 14:51
 */
public class Item {
    /**
     * 商品ID
     */
    private Integer itemId;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品类别ID
     */
    private Integer itemCategoryId;

    /**
     * 商品价格
     */
    private BigDecimal itemPrice;

    /**
     * 商品销量
     */
    private Integer itemSales;

    /**
     * 商品描述（文字）
     */
    private String itemDescription;

    /**
     * 商品图片URL
     */
    private String itemImageUrl;

    /**
     * 商品上下架状态（1：上架 0：下架）
     */
    private Byte shelves;

    /**
     * 商品创建时间
     */
    private Date createTime;

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

    public Integer getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(Integer itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getItemSales() {
        return itemSales;
    }

    public void setItemSales(Integer itemSales) {
        this.itemSales = itemSales;
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

    public Byte getShelves() {
        return shelves;
    }

    public void setShelves(Byte shelves) {
        this.shelves = shelves;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
