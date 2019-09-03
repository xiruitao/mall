package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.ItemCategoryModel;
import com.mall.service.model.ItemModel;

import java.util.List;

/**
 * @Description: 商品功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 16:25
 */
public interface ItemService {
    /**
     * 商品列表浏览
     * @return List<ItemModel>
     */
    List<ItemModel> listItem();

    /**
     * 商品详情浏览
     * @param itemId 商品ID
     * @return ItemModel
     */
    ItemModel getItemByItemId(Integer itemId);

    /**
     * 最受欢迎产品
     * @return List<ItemModel>
     */
    List<ItemModel> listPopItem();

    /**
     * 最新产品
     * @return List<ItemModel>
     */
    List<ItemModel> listNewItem();

    /**
     * 商品分页展示
     * @param page 当前页面
     * @return List<ItemModel>
     * @throws BusinessException 业务异常
     */
    List<ItemModel> lisItemPage(Integer page) throws BusinessException;

    /**
     * 商品分页数
     * @return int 商品分页数
     */
    int getItemPages();

    /**
     * 商品模糊查询
     * @param field 查询字段
     * @return List<ItemModel>
     */
    List<ItemModel> fuzzySearch(String field);

    /**
     * 分类查询商品
     * @param itemCategoryId 商品类别ID
     * @return List<ItemModel>
     */
    List<ItemModel> listItemOfCategory(Integer itemCategoryId);

    /**
     * 按商品上下架状态查询
     * @param shelves 上下架状态
     * @return List<ItemModel>
     */
    List<ItemModel> selectItemByShelves(Byte shelves);

    /**
     * 创建商品
     * @param itemModel 商品模型
     * @return ItemModel
     * @throws BusinessException 业务异常
     */
    ItemModel createItem(ItemModel itemModel) throws BusinessException;

    /**
     * 商品销量增加
     * @param itemId 商品ID
     * @param amount 购买数量
     * @throws BusinessException 业务异常
     */
    void increaseItemSales(Integer itemId,Integer amount) throws BusinessException;

    /**
     * 删除商品
     * @param itemId 商品ID
     */
    void deleteItemByItemId(Integer itemId);

    /**
     * 多条删除商品
     * @param itemIds 商品id集合
     * @throws BusinessException 业务异常
     */
    void deleteItemMore(List<Integer> itemIds) throws BusinessException;

    /**
     * 修改商品信息
     * @param itemModel 商品模型
     * @throws BusinessException 业务异常
     * @return ItemModel
     */
    ItemModel modifyItemByItemId(ItemModel itemModel) throws BusinessException;

    /**
     * 商品类别列表
     * @return List<ItemCategoryModel>
     */
    List<ItemCategoryModel> listItemCategory();

    /**
     * 添加商品类别
     * @param itemCategoryModel 商品类别模型
     * @throws BusinessException 业务异常
     */
    void addItemCategory(ItemCategoryModel itemCategoryModel)throws BusinessException;

    /**
     * 删除商品类别
     * @param itemCategoryId 商品类别ID
     * @throws BusinessException 业务异常
     */
    void deleteItemCategory(Integer itemCategoryId) throws BusinessException;

    /**
     * 查询商品类别
     * @param itemCategoryId 商品类别ID
     * @return ItemCategoryModel
     */
    ItemCategoryModel selectItemCategory(Integer itemCategoryId);

    /**
     * 更新商品上下架状态
     * @param itemId 商品ID
     * @return ItemModel
     */
    ItemModel updateShelves(Integer itemId);

}
