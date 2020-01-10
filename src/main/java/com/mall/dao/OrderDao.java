package com.mall.dao;

import com.mall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 订单访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 15:08
 */
public interface OrderDao {
    /**
     * 生成订单
     * @param order 订单实体
     * @return int
     */
    int insertSelective(Order order);

    /**
     * 支付完成更新订单及确认完成、修改地址更新订单、删除更新订单
     * @param order 订单实体
     * @return int
     */
    int updateOrder(Order order);

    /**
     * 查看所有订单（后台）
     * @return List<Order>
     */
    List<Order> selectOrderAll();

    /**
     * 查询用户所有订单（不包含已完成、取消及删除）
     * @param userId 用户ID
     * @return List<Order>
     */
    List<Order> selectOrderOfUser(Integer userId);

    /**
     * 查询用户已完成订单
     * @param userId 用户ID
     * @return List<Order>
     */
    List<Order> selectEndOrderOfUser(Integer userId);

    /**
     * 查询用户已取消订单
     * @param userId 用户ID
     * @return List<Order>
     */
    List<Order> selectOrderOfUserCancel(Integer userId);

    /**
     * 查看订单详情
     * @param orderId 订单ID
     * @return Order
     */
    Order selectOrder(String orderId);

    /**
     * 彻底删除订单
     * @param orderId 订单号
     * @return int
     */
    int deleteOrder(String orderId);

    /**
     * 查询订单条数
     * @return int
     */
    int selectOrderRows();

    /**
     * 分页查询订单
     * @param offset 开始下标
     * @param limit 条数
     * @return List<Order>
     */
    List<Order> listOrderPages(@Param("offset")Integer offset,@Param("limit")Integer limit);

    /**
     * 订单搜索
     * @param field 搜索字段
     * @return List<Order>
     */
    List<Order> orderSearch(@Param("field")String field);

    /**
     * 通过订单状态搜索订单
     * @param orderStatus 订单状态
     * @return List<Order>
     */
    List<Order> selectOrderByStatus(@Param("orderStatus")Byte orderStatus);
}
