package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.ItemNumberModel;

/**
 * @Description: 商品库存功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 16:35
 */
public interface ItemNumberService {
    /**
     * 库存扣减
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return boolean
     * @throws BusinessException 业务异常
     */
    boolean decreaseItemNumber(Integer itemId,Integer amount) throws BusinessException;

    /**
     * 修改库存
     * @param itemId 商品ID
     * @param itemNumbers 库存
     * @throws BusinessException 业务异常
     */
    void updateItemNumber(Integer itemId,Integer itemNumbers)throws BusinessException;

    /**
     * 通过商品ID查询商品库存
     * @param itemId 商品ID
     * @return ItemNumberModel
     */
    ItemNumberModel getByItemId(Integer itemId);

    /**
     * 创建库存
     * @param itemNumberModel 库存模型
     * @throws BusinessException 业务异常
     */
    void createItemNumber(ItemNumberModel itemNumberModel)throws BusinessException;
}
