package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.CartModel;

import java.util.List;

/**
 * @Description: 购物车功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 12:25
 */
public interface CartService {
    /**
     * 将商品加入购物车
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param amount 购买数量
     * @throws BusinessException 业务异常
     * @return CartModel
     */
    void addCart(Integer userId,Integer itemId,Integer amount) throws BusinessException;

    /**
     * 删除购物车中某个商品
     * @param cartId 购物车ID
     */
    void deleteCart(Integer cartId);

    /**
     * 批量删除商品
     * @param cartIds 购物车ID集合
     */
    void deleteCartMore(List<Integer> cartIds);

    /**
     * 清空用户购物车
     * @param userId 用户ID
     */
    void deleteCartAll(Integer userId);

    /**
     * 用户购物车展示
     * @param userId 用户ID
     * @return CartModel
     */
    List<CartModel> selectCart(Integer userId);

    /**
     * 通过购物车ID查询购物车中商品
     * @param cartId 商品ID
     * @return CartModel
     */
    CartModel getCartById(Integer cartId);

    /**
     * 修改购买数量
     * @param cartId 购物车ID
     * @param number 更改数
     */
    void updateAmount(Integer cartId,Integer number);
}
