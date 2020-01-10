package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 商品视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/1 08:58
 */
public class ItemVO {
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
     * 商品类名
     */
    private String itemCategoryName;

    /**
     * 商品价格
     */
    private BigDecimal itemPrice;

    /**
     * 商品销量
     */
    private Integer itemSales;

    /**
     * 商品库存数
     */
    private Integer itemNumber;

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
     * 状态名：上架 or 下架
     */
    private String shelvesName;

    /**
     * 商品创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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

    public String getItemCategoryName() {
        return itemCategoryName;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
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

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
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

    public String getShelvesName() {
        return shelvesName;
    }

    public void setShelvesName(String shelvesName) {
        this.shelvesName = shelvesName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
