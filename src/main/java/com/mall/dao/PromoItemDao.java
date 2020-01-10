package com.mall.dao;

import com.mall.entity.PromoItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 活动商品访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/6 08:34
 */
public interface PromoItemDao {
    /**
     * 添加活动商品
     * @param promoItem 活动商品实体
     * @return int
     */
    int insertSelective(PromoItem promoItem);

    /**
     * 删除活动商品（单个）
     * @param promoItemId 活动商品ID
     * @return int
     */
    int deletePromoItem(Integer promoItemId);

    /**
     * 删除活动所有商品
     * @param promoId 活动ID
     * @return int
     */
    int deletePromoItemAll(Integer promoId);

    /**
     * 修改活动商品价格
     * @param promoItem 活动商品
     * @return int
     */
    int updatePromoItem(PromoItem promoItem);

    /**
     * 查询所有活动商品
     * @param promoId 活动ID
     * @return List<PromoItem>
     */
    List<PromoItem> selectPromoItem(Integer promoId);

    /**
     * 通过活动商品ID查询活动商品
     * @param promoItemId 活动商品ID
     * @return PromoItem
     */
    PromoItem selectPromoItemById(Integer promoItemId);

}
