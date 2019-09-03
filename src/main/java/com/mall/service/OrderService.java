package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.OrderItemModel;
import com.mall.service.model.OrderModel;

import java.util.List;

/**
 * @Description: 订单功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 16:58
 */
public interface OrderService {

    /**
     * 创建订单（多商品）
     * @param userId 用户ID
     * @param cartIds 购物车ID集合
     * @return OrderModel
     */
    OrderModel createOrderMoreItem(Integer userId,List<Integer> cartIds)throws BusinessException;

    /**
     * 创建订单(单个商品)
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param amount 购买数量
     * @throws BusinessException 业务异常
     * @return OrderModel
     */
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount)throws BusinessException;

    /**
     * 订单完成支付（状态号改为 1 进行中，产生积分）
     * @param orderId 订单号
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    OrderModel payOrder(String orderId)throws BusinessException;

    /**
     * 确认订单完成（订单状态号改为 2 完成）
     * @param orderId 订单号
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    OrderModel endOrder(String orderId) throws BusinessException;

    /**
     * 设置收货地址
     * @param orderId 订单ID
     * @param addressId 地址ID
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    OrderModel setOrderAddress(String orderId,Integer addressId)throws BusinessException;

    /**
     * 查看所有订单（后台）
     * @return List<OrderModel>
     */
    List<OrderModel> listOrder();

    /**
     * 查询用户所有订单（不包含已完成、取消及删除）
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    List<OrderModel> listOrderOfUser(Integer userId);

    /**
     * 查询用户已完成订单
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    List<OrderModel> listEndOrderOfUser(Integer userId);

    /**
     * 查询用户所已取消订单
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    List<OrderModel> listOrderOfUserCancel(Integer userId);

    /**
     * 查看订单详情
     * @param orderId 订单ID
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    OrderModel getOrder(String orderId)throws BusinessException;

    /**
     * 用户删除订单（订单状态号改为 3 已归档）
     * @param orderId 订单ID
     * @return OrderModel
     */
    OrderModel deleteOrderUser(String orderId)throws BusinessException;

    /**
     * 取消订单（订单状态号改为 5 手动取消）
     * @param orderId 订单ID
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    OrderModel cancelOrder(String orderId) throws BusinessException;

    /**
     * 获取订单总数
     * @return int
     */
    int getOrderPages();

    /**
     * 订单分页查询
     * @param page 页码
     * @return List<OrderModel>
     * @throws BusinessException 业务异常
     */
    List<OrderModel> listOrderPages(Integer page) throws BusinessException;

    /**
     * 订单搜索
     * @param field 搜索字段
     * @return List<OrderModel>
     */
    List<OrderModel> orderSearch(String field);

    /**
     * 通过订单状态获取订单
     * @param orderStatus 订单状态
     * @return List<OrderModel>
     */
    List<OrderModel> getOrderByStatus(Byte orderStatus);

    /**
     * 查询订单商品
     * @param orderId 订单ID
     * @return List<OrderItemModel>
     */
    List<OrderItemModel> getOrderItem(String orderId);
}
