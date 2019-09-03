package com.mall.service.impl;

import com.mall.cache.ItemCache;
import com.mall.dao.ItemCategoryDao;
import com.mall.dao.ItemDao;
import com.mall.entity.Item;
import com.mall.entity.ItemCategory;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.ItemNumberService;
import com.mall.service.ItemService;
import com.mall.service.model.ItemCategoryModel;
import com.mall.service.model.ItemModel;
import com.mall.service.model.ItemNumberModel;
import com.mall.validator.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 商品功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 16:52
 */

@Service
public class ItemServiceImpl implements ItemService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(ItemServiceImpl.class);

    /**
     * 每页显示商品数
     */
    private static int numbers = 5;

    /**
     * 商品访问对象
     */
    @Autowired
    private ItemDao itemDao;

    /**
     * 商品缓存
     */
    @Autowired
    private ItemCache itemCache;

    /**
     * 商品类别访问对象
     */
    @Autowired
    private ItemCategoryDao itemCategoryDao;

    /**
     * 商品库存功能接口
     */
    @Autowired
    private ItemNumberService itemNumberService;

    /**
     * 校验对象
     */
    @Autowired
    private Validate validate;

    /**
     * 商品列表浏览
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> listItem() {
        List<Item> itemList =itemDao.listItem();
        return convertFromItemList(itemList);
    }

    /**
     * 商品详情浏览
     * @param itemId 商品ID
     * @return ItemModel
     */
    @Override
    public ItemModel getItemByItemId(Integer itemId) {
        Item item = itemCache.getItem(itemId);
        if (item == null){
            item = itemDao.selectByItemId(itemId);
            itemCache.putItem(item);
        }
        return convertModelFromDO(item);
    }

    /**
     * 销量最高五件产品
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> listPopItem() {
        List<Item> itemList =itemDao.listItemPage(0,5);
        return convertFromItemList(itemList);
    }

    /**
     * 最新6件产品
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> listNewItem() {
        List<Item> itemList = itemDao.listItemNew(0,6);
        return convertFromItemList(itemList);
    }

    /**
     * 商品分页展示
     * @param page 当前页码
     * @return List<ItemModel>
     * @throws BusinessException 业务异常
     */
    @Override
    public List<ItemModel> lisItemPage(Integer page) throws BusinessException {
        //每页商品条数
        if (page > getItemPages() || page < 1){
            logger.info("页码错误");
            throw new BusinessException(EmBusinessError.PAGE_NUMBER_ERROR);
        }
        int offset = (page-1)*numbers;
        List<Item> itemList = itemDao.listItemPage(offset,numbers);
        return convertFromItemList(itemList);
    }

    /**
     * 商品分页数
     * @return int 商品分页数
     */
    @Override
    public int getItemPages() {
        //商品总条数
        int itemRows = itemDao.selectItemRows();
        //商品分页数
        int pages = (int) Math.ceil((double)itemRows/numbers);
        return pages;
    }

    /**
     * 商品模糊查询
     * @param field 查询字段
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> fuzzySearch(String field) {
        List<Item> itemList = itemDao.fuzzySearch(field);
        return convertFromItemList(itemList);
    }

    /**
     * 分类查询商品
     * @param itemCategoryId 商品类别ID
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> listItemOfCategory(Integer itemCategoryId) {
        List<Item> itemList = itemDao.selectItemByItemCategoryId(itemCategoryId);
        return convertFromItemList(itemList);
    }

    /**
     * 按商品上下架状态查询
     * @param shelves 上下架状态
     * @return List<ItemModel>
     */
    @Override
    public List<ItemModel> selectItemByShelves(Byte shelves) {
        List<Item> itemList = itemDao.selectItemByShelves(shelves);
        return convertFromItemList(itemList);
    }

    /**
     * 创建商品
     * @param itemModel 商品模型
     * @return ItemModel
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemModel createItem(ItemModel itemModel) throws BusinessException {
        validate.valid(itemModel);

        Item item = this.convertItemFromItemModel(itemModel);
        itemDao.insertSelective(item);
        itemModel.setItemId(item.getItemId());

        //初始化商品库存
        ItemNumberModel itemNumberModel = new ItemNumberModel();
        itemNumberModel.setItemId(item.getItemId());
        itemNumberService.createItemNumber(itemNumberModel);

        return this.getItemByItemId(itemModel.getItemId());
    }

    /**
     * 销量增加
     * @param itemId 商品ID
     * @param amount 购买数量
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseItemSales(Integer itemId, Integer amount) throws BusinessException {
        itemDao.increaseItemSales(itemId,amount);
    }

    /**
     * 删除商品
     * @param itemId 商品ID
     */
    @Override
    public void deleteItemByItemId(Integer itemId) {
        itemDao.deleteItemByItemId(itemId);
    }

    /**
     * 多条删除商品
     * @param itemIds 商品id集合
     */
    @Override
    public void deleteItemMore(List<Integer> itemIds) throws BusinessException {
        if (itemIds == null || itemIds.size() == 0){
            logger.info("未选择商品");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择商品");
        }
        itemDao.deleteItemMore(itemIds);
    }

    /**
     * 商品信息修改
     * @param itemModel 商品模型
     */
    @Override
    public ItemModel modifyItemByItemId(ItemModel itemModel)throws BusinessException{
        if (itemModel == null){
            return null;
        }
        validate.valid(itemModel);
        Item item = convertItemFromItemModel(itemModel);
        itemDao.updateItemByItemId(item);

        //同时更新Redis缓存
        itemCache.updateItem(String.valueOf(item.getItemId()),item);

        return convertModelFromDO(item);
    }

    /**
     * 商品类别列表
     * @return List<ItemCategoryModel>
     */
    @Override
    public List<ItemCategoryModel> listItemCategory() {
        List<ItemCategory> itemCategoryList = itemCategoryDao.listItemCategory();
        List<ItemCategoryModel> itemCategoryModelList = itemCategoryList.stream().map(itemCategory -> {
            ItemCategoryModel itemCategoryModel = new ItemCategoryModel();
            BeanUtils.copyProperties(itemCategory,itemCategoryModel);
            return itemCategoryModel;
        }).collect(Collectors.toList());
        return itemCategoryModelList;
    }

    /**
     * 添加商品类别
     * @param itemCategoryModel 商品类别模型
     */
    @Override
    public void addItemCategory(ItemCategoryModel itemCategoryModel) throws BusinessException {
        if (itemCategoryModel == null){
            logger.info("商品类别为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ItemCategory itemCategory = new ItemCategory();
        BeanUtils.copyProperties(itemCategoryModel,itemCategory);
        itemCategoryDao.insertSelective(itemCategory);
    }

    /**
     * 删除商品类别
     * @param itemCategoryId 商品类别ID
     */
    @Override
    public void deleteItemCategory(Integer itemCategoryId) throws BusinessException {
        List<Item> itemList = itemDao.selectItemByItemCategoryId(itemCategoryId);
        if (itemList != null){
            logger.info("此类别还有商品");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"此类别还有商品");
        }
        itemCategoryDao.deleteItemCategory(itemCategoryId);
    }

    /**
     * 查询商品类别
     * @param itemCategoryId 商品类别ID
     * @return ItemCategoryModel
     */
    @Override
    public ItemCategoryModel selectItemCategory(Integer itemCategoryId) {
        ItemCategory itemCategory = itemCategoryDao.selectByItemCategoryId(itemCategoryId);
        ItemCategoryModel itemCategoryModel = new ItemCategoryModel();
        BeanUtils.copyProperties(itemCategory,itemCategoryModel);
        return itemCategoryModel;
    }

    /**
     * 更新商品上下架状态
     * @param itemId 商品ID
     * @return ItemModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemModel updateShelves(Integer itemId) {
        ItemModel itemModel = getItemByItemId(itemId);
        Item item = convertItemFromItemModel(itemModel);
        int two = 2;
        if (item.getShelves() == 0){
            itemDao.updateShelves(itemId, (byte) 1);
        }
        if (item.getShelves() == 1){
            itemDao.updateShelves(itemId, (byte) 0);
        }
        if (item.getShelves() == two){
            itemDao.updateShelves(itemId, (byte) 1);
        }
        Item item2 = itemDao.selectByItemId(itemId);

        //更新商品缓存
        itemCache.updateItem(String.valueOf(itemId),item2);

        return convertModelFromDO(item2);
    }


    /**
     * List<Item>转化为List<ItemModel>
     * @param itemList 商品实体列表
     * @return List<ItemModel>
     */
    private List<ItemModel> convertFromItemList(List<Item> itemList){
        if (itemList == null){
            return null;
        }
        List<ItemModel> itemModelList = itemList.stream().map(item -> {
            ItemModel itemModel = this.convertModelFromDO(item);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    /**
     * ItemModel转化为Item
     * @param itemModel 商品模型
     * @return Item
     */
    private Item convertItemFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        Item item = new Item();
        BeanUtils.copyProperties(itemModel,item);
        return item;
    }

    /**
     * Item + ItemNumber --> ItemModel
     * @param item 商品实体
     * @return ItemModel
     */
    private ItemModel convertModelFromDO(Item item){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(item,itemModel);
        return itemModel;
    }
}
