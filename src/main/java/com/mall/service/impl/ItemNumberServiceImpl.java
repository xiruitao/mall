package com.mall.service.impl;

import com.mall.dao.ItemNumberDao;
import com.mall.entity.ItemNumber;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.ItemNumberService;
import com.mall.service.model.ItemNumberModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 商品库存功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 16:37
 */
@Service
public class ItemNumberServiceImpl implements ItemNumberService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(ItemNumberServiceImpl.class);
    /**
     * 库存访问对象
     */
    @Autowired
    private ItemNumberDao itemNumberDao;

    /**
     * 减库存
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return boolean
     */
    @Override
    public boolean decreaseItemNumber(Integer itemId, Integer amount){
        int affectRow =itemNumberDao.decreaseItemNumber(itemId,amount);
        if (affectRow == 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改库存
     * @param itemId 商品ID
     * @param itemNumbers 库存
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItemNumber(Integer itemId,Integer itemNumbers) throws BusinessException {
        ItemNumberModel itemNumberModel = getByItemId(itemId);
        itemNumberModel.setItemNumber(itemNumbers);

        ItemNumber itemNumber = convertFromModel(itemNumberModel);
        itemNumberDao.updateItemNumber(itemNumber);
    }

    /**
     * 通过商品ID查询商品库存
     * @param itemId 商品ID
     * @return ItemNumberModel
     */
    @Override
    public ItemNumberModel getByItemId(Integer itemId) {
        ItemNumber itemNumber = itemNumberDao.selectByItemId(itemId);
        return convertFromDO(itemNumber);
    }

    /**
     * 创建库存
     * @param itemNumberModel 库存模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void createItemNumber(ItemNumberModel itemNumberModel) throws BusinessException {
        if (itemNumberModel == null){
            logger.info("库存不存在");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"库存不存在");
        }
        ItemNumber itemNumber = convertFromModel(itemNumberModel);
        itemNumberDao.insertSelective(itemNumber);
    }

    /**
     * ItemNumberModel转化为ItemNumber
     * @param itemNumberModel 库存模型
     * @return ItemNumber
     */
    private ItemNumber convertFromModel(ItemNumberModel itemNumberModel){
        if (itemNumberModel == null){
            return null;
        }
        ItemNumber itemNumber = new ItemNumber();
        BeanUtils.copyProperties(itemNumberModel,itemNumber);
        return itemNumber;
    }

    /**
     * ItemNumber转化为ItemNumberModel
     * @param itemNumber 库存实体
     * @return ItemNumberModel
     */
    private ItemNumberModel convertFromDO(ItemNumber itemNumber){
        if (itemNumber == null){
            return null;
        }
        ItemNumberModel itemNumberModel = new ItemNumberModel();
        BeanUtils.copyProperties(itemNumber,itemNumberModel);
        return itemNumberModel;
    }
}
