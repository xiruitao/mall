package com.mall.entity;

/**
 * @Description: 商品库存实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 15:14
 */
public class ItemNumber {
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
}
