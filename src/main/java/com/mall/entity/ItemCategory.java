package com.mall.entity;

/**
 * @Description: 商品类别实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 14:55
 */
public class ItemCategory {
    /**
     * 商品类别ID
     */
    private Integer itemCategoryId;

    /**
     * 商品类名
     */
    private String itemCategoryName;

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
}
