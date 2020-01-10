package com.mall.dao;

import com.mall.entity.ItemCategory;

import java.util.List;

/**
 * @Description: 商品类别访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 15:33
 */
public interface ItemCategoryDao {
    /**
     * 商品类别列表
     * @return List<ItemCategory>
     */
    List<ItemCategory> listItemCategory();

    /**
     * 添加商品类别
     * @param itemCategory 商品类别实体
     * @return int
     */
    int insertSelective(ItemCategory itemCategory);

    /**
     * 删除商品类别
     * @param itemCategoryId 商品类别ID
     * @return int
     */
    int deleteItemCategory(Integer itemCategoryId);

    /**
     * 查询商品类别
     * @param itemCategoryId 商品类别ID
     * @return ItemCategory
     */
    ItemCategory selectByItemCategoryId(Integer itemCategoryId);
}
