package com.mall.dao;

import com.mall.entity.Item;
import com.mall.entity.ItemNumber;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 商品库存访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 15:43
 */
public interface ItemNumberDao {
    /**
     * 修改商品库存（管理）
     * @param itemNumber 商品实体
     * @return int
     */
    int updateItemNumber(ItemNumber itemNumber);

    /**
     * 减库存（交易成功）
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return int
     */
    int decreaseItemNumber(@Param("itemId") Integer itemId, @Param("amount") Integer amount);

    /**
     * 通过商品ID查询商品库存
     * @param itemId 商品ID
     * @return ItemNumber
     */
    ItemNumber selectByItemId(Integer itemId);

    /**
     * 库存插入
     * @param itemNumber 库存实体
     * @return int
     */
    int insertSelective(ItemNumber itemNumber);
}
