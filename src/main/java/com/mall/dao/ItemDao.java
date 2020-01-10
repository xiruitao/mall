package com.mall.dao;

import com.mall.entity.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 商品访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 15:20
 */
public interface ItemDao {
    /**
     * 获取商品列表
     * @return List<Item>
     */
    List<Item> listItem();

    /**
     * 查看商品详情
     * @param itemId 商品ID
     * @return Item
     */
    Item selectByItemId(Integer itemId);

    /**
     * 查询一定量商品
     * @param offset 下标
     * @param limit 条数
     * @return List<Item>
     */
    List<Item> listItemPage(@Param("offset")Integer offset,@Param("limit")Integer limit);

    /**
     * 查询最新的几件产品
     * @param offset 下标
     * @param limit 条数
     * @return List<Item>
     */
    List<Item> listItemNew(@Param("offset")Integer offset,@Param("limit")Integer limit);

    /**
     * 查询商品表行数
     * @return int 行数
     */
    int selectItemRows();

    /**
     * 商品模糊查询
     * @param field 查询字段
     * @return List<Item>
     */
    List<Item> fuzzySearch(@Param("field")String field);

    /**
     * 分类查询商品
     * @param itemCategoryId 商品类别ID
     * @return List<Item>
     */
    List<Item> selectItemByItemCategoryId(Integer itemCategoryId);

    /**
     * 按商品上下架状态查询
     * @param shelves 上下架状态
     * @return List<Item>
     */
    List<Item> selectItemByShelves(Byte shelves);

    /**
     * 添加商品
     * @param item 商品实体
     * @return int
     */
    int insertSelective(Item item);

    /**
     * 删除商品
     * @param itemId 商品ID
     * @return int
     */
    int deleteItemByItemId(Integer itemId);

    /**
     * 多条删除商品
     * @param itemIds 商品id集合
     * @return int
     */
    int deleteItemMore(List<Integer> itemIds);

    /**
     * 商品信息更改
     * @param item 商品实体
     * @return int
     */
    int updateItemByItemId(Item item);

    /**
     * 增加销量
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return int
     */
    int increaseItemSales(@Param("itemId")Integer itemId,@Param("amount")Integer amount);

    /**
     * 商品上下架状态
     * @param itemId 商品ID
     * @param shelves 上下架状态
     * @return int
     */
    int updateShelves(@Param("itemId")Integer itemId,@Param("shelves")Byte shelves);
}
