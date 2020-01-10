package com.mall.service.model;

/**
 * @Description: 商品类别模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 16:40
 */
public class ItemCategoryModel {
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
