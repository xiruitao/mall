package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.PromoItemModel;
import com.mall.service.model.PromoModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 促销活动功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 18:22
 */
public interface PromoService {
    /**
     * 创建活动
     * @param promoModel 活动模型
     * @return PromoModel
     * @throws BusinessException 业务异常
     */
    PromoModel addPromo(PromoModel promoModel)throws BusinessException;

    /**
     * 删除活动
     * @param promoId 活动ID
     */
    void deletePromo(Integer promoId);

    /**
     * 修改活动信息
     * @param promoModelForTran 活动模型
     * @return PromoModel
     * @throws BusinessException 业务异常
     */
    PromoModel updatePromo(PromoModel promoModelForTran)throws BusinessException;

    /**
     * 活动列表查看
     * @return List<PromoModel>
     */
    List<PromoModel> listPromo();

    /**
     * 查看活动详情
     * @param promoId 活动ID
     * @return PromoModel
     */
    PromoModel selectPromo(Integer promoId);

    /**
     * 添加活动商品
     * @param promoItemId 活动商品ID
     * @param promoId 活动ID
     * @throws BusinessException 业务异常
     */
    void addPromoItem(Integer promoItemId,Integer promoId)throws BusinessException;

    /**
     * 删除活动商品
     * @param promoItemId 活动商品ID
     */
    void deletePromoItem(Integer promoItemId);

    /**
     * 修改活动商品信息
     * @param promoItemId 活动商品ID
     * @param promoItemPrice 活动价格
     * @throws BusinessException 业务异常
     */
    void updatePromoItem(Integer promoItemId, BigDecimal promoItemPrice)throws BusinessException;

    /**
     * 活动商品列表
     * @param promoId 活动ID
     * @return List<PromoItemModel>
     */
    List<PromoItemModel> listPromoItem(Integer promoId);

    /**
     * 通过活动商品ID查询活动商品
     * @param promoItemId 活动商品ID
     * @return PromoItemModel
     */
    PromoItemModel getPromoItemById(Integer promoItemId);
}
