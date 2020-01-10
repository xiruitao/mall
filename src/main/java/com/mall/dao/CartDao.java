package com.mall.dao;

import com.mall.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 购物车访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 10:29
 */
public interface CartDao {
    /**
     * 添加购物车
     * @param cart 购物车实体
     * @return int
     */
    int insertSelective(Cart cart);

    /**
     * 删除购物车单个物品
     * @param cartId 购物车id
     * @return int
     */
    int deleteCart(Integer cartId);

    /**
     * 批量删除商品
     * @param cartIds 购物车ID集合
     * @return int
     */
    int deleteCartMore(List<Integer> cartIds);

    /**
     * 清空购物车
     * @param userId 用户ID
     * @return int
     */
    int deleteCartOfUser(Integer userId);

    /**
     * 查询用户购物车
     * @param userId 用户ID
     * @return Cart
     */
    List<Cart> selectCart(Integer userId);

    /**
     * 通过购物车ID查询购物车中商品
     * @param cartId 商品ID
     * @return Cart
     */
    Cart selectCartById(Integer cartId);

    /**
     * 购买商品数量更改
     * @param cartId 购物车ID
     * @param numbers 改变数
     * @return int
     */
    int updateAmount(@Param("cartId")Integer cartId, @Param("numbers")Integer numbers);
}
