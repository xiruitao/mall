package com.mall.dao;

import com.mall.entity.Promo;

import java.util.List;

/**
 * @Description: 促销活动访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 17:51
 */
public interface PromoDao {

    /**
     * 创建活动
     * @param promo 活动实体
     * @return int
     */
    int insertSelective(Promo promo);

    /**
     * 删除活动
     * @param promoId 活动ID
     * @return int
     */
    int deletePromo(Integer promoId);

    /**
     * 查询活动
     * @param promoId 活动ID
     * @return Promo
     */
    Promo selectPromo(Integer promoId);

    /**
     * 查询所有活动信息
     * @return List<Promo>
     */
    List<Promo> listPromo();

    /**
     * 修改活动信息
     * @param promo 活动实体对象
     * @return int
     */
    int updatePromo(Promo promo);

}
