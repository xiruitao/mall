package com.mall.dao;

import com.mall.entity.OrderItem;

import java.util.List;

/**
 * @Description: 订单商品表访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/19 10:28
 */
public interface OrderItemDao {
    /**
     * 查询某个订单内商品
     * @param orderId 订单ID
     * @return List<OrderItem>
     */
    List<OrderItem> selectOrderItemByOrderId(String orderId);

    /**
     * 插入订单商品
     * @param orderItem 订单商品对象
     * @return int
     */
    int insertSelective(OrderItem orderItem);
}
