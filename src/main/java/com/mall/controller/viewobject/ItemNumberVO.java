package com.mall.controller.viewobject;

/**
 * @Description: 商品库存视图模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 17:24
 */
public class ItemNumberVO {
    /**
     * 商品库存ID
     */
    private Integer itemNumberId;

    /**
     * 商品库存数
     */
    private Integer itemNumber;

    /**
     * 商品ID
     */
    private Integer itemId;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品图片URL
     */
    private String itemImageUrl;

    public Integer getItemNumberId() {
        return itemNumberId;
    }

    public void setItemNumberId(Integer itemNumberId) {
        this.itemNumberId = itemNumberId;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
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
}
