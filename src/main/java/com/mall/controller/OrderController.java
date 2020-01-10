package com.mall.controller;

import com.mall.annotation.UserLogAnnotation;
import com.mall.controller.viewobject.OrderItemVO;
import com.mall.controller.viewobject.OrderVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.ItemService;
import com.mall.service.OrderService;
import com.mall.service.model.OrderItemModel;
import com.mall.service.model.OrderModel;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 订单业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/3 10:14
 */

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController{
    /**
     * 订单功能接口
     */
    @Autowired
    private OrderService orderService;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 校验登录工具类
     */
    @Autowired
    private ValidateLogon validateLogon;


    /**
     * 创建订单（多商品）
     * @param cartIdStr 购物车ID组合字符串
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户创建订单（可能多商品）")
    @RequestMapping(value = "/createOrderItemMore",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType createOrderItemMore(@RequestParam("cartIdStr")String cartIdStr)throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        String[] cartIdStrArr = cartIdStr.split(",");
        List<Integer> cartIds = new ArrayList<>();
        for (String cartId : cartIdStrArr){
            if (cartId != null) {
                cartIds.add(Integer.valueOf(cartId));
            }
        }
        OrderModel orderModel = orderService.createOrderMoreItem(userModel.getUserId(),cartIds);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 创建订单(单个商品)
     * @param itemId 订单ID
     * @param amount 购买数量
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户创建订单")
    @RequestMapping(value = "/createOrder",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam("itemId")Integer itemId,
                                        @RequestParam("amount")Integer amount)throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();

        OrderModel orderModel = orderService.createOrder(userModel.getUserId(),itemId,amount);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 订单完成支付
     * @param orderId 订单ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户完成订单支付")
    @RequestMapping(value = "/payOrder",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType payOrder(@RequestParam("orderId")String orderId)throws BusinessException{
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.payOrder(orderId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 确认订单完成
     * @param orderId 订单ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户确认订单完成")
    @RequestMapping(value = "/endOrder",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType endOrder(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.endOrder(orderId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 设置收货地址
     * @param orderId 订单Id
     * @param addressId 地址Id
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户设置订单收货地址")
    @RequestMapping(value = "/setOrderAddress",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType setOrderAddress(@RequestParam("orderId")String orderId,
                                            @RequestParam("addressId")Integer addressId)throws BusinessException{
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.setOrderAddress(orderId,addressId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 查询用户所有订单（不包含已完成、取消及删除）
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listOrderOfUser",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listOrderOfUser()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<OrderModel> orderModels = orderService.listOrderOfUser(userModel.getUserId());
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModels);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 查看用户已完成订单
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listEndOrderOfUser",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listEndOrderOfUser()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<OrderModel> orderModels = orderService.listEndOrderOfUser(userModel.getUserId());
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModels);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 查询用户已取消订单
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listOrderOfUserCancel",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listOrderOfUserCancel()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<OrderModel> orderModels = orderService.listOrderOfUserCancel(userModel.getUserId());
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModels);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrder(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.getOrder(orderId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 用户获取订单商品
     * @param orderId 订单ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getOrderItem",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderItem(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateLogon();
        List<OrderItemModel> orderItemModelList = orderService.getOrderItem(orderId);
        List<OrderItemVO> orderItemVOList = convertFromOrderItemList(orderItemModelList);
        return CommonReturnType.create(orderItemVOList);
    }

    /**
     * 用户删除订单(归档，订单状态设置为3）
     * @param orderId 用户ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户删除订单")
    @RequestMapping(value = "/deleteOrderUser",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType deleteOrderUser(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.deleteOrderUser(orderId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户取消订单")
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType cancelOrder(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateLogon();
        OrderModel orderModel = orderService.cancelOrder(orderId);
        return CommonReturnType.create(convertFromModel(orderModel));
    }

    //管理员对订单操作
    /**
     * 查看所有订单
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listOrder() throws BusinessException {
        validateLogon.validateAdminLogon();
        List<OrderModel> orderModelList = orderService.listOrder();
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModelList);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 管理员获取订单商品
     * @param orderId 订单ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getOrderItems",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderItems(@RequestParam("orderId")String orderId) throws BusinessException {
        validateLogon.validateAdminLogon();
        List<OrderItemModel> orderItemModelList = orderService.getOrderItem(orderId);
        List<OrderItemVO> orderItemVOList = convertFromOrderItemList(orderItemModelList);
        return CommonReturnType.create(orderItemVOList);
    }

    /**
     * 分页查询订单
     * @param page 页码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listOrderPages",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listOrderPages(@RequestParam("page")Integer page)throws BusinessException{
        validateLogon.validateAdminLogon();
        List<OrderModel> orderModelList = orderService.listOrderPages(page);
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModelList);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 订单搜索
     * @param field 搜索字段
     * @return CommonReturnType
     */
    @RequestMapping(value = "/orderSearch",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType orderSearch(@RequestParam("field")String field) throws BusinessException {
        validateLogon.validateAdminLogon();
        List<OrderModel> orderModelList = orderService.orderSearch(field);
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModelList);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 获取订单分页数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getOrderPages",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderPages() throws BusinessException {
        validateLogon.validateAdminLogon();
        int pages = orderService.getOrderPages();
        return CommonReturnType.create(pages);
    }

    /**
     * 通过订单状态获取订单
     * @param orderStatus 订单状态
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getOrderByStatus",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getOrderByStatus(@RequestParam("orderStatus")String orderStatus)throws BusinessException{
        validateLogon.validateAdminLogon();
        String nullStr = "null";
        if (orderStatus == null || "".equals(orderStatus) || orderStatus.equals(nullStr)){
            return CommonReturnType.create(null);
        }
        List<OrderModel> orderModelList = orderService.getOrderByStatus(new Byte(orderStatus));
        List<OrderVO> orderVOList = convertFromOrderModelList(orderModelList);
        return CommonReturnType.create(orderVOList);
    }

    /**
     * 订单状态码对应订单状态
     */
    private final static Map<Integer, String> ORDER_STATUS = new HashMap<Integer,String>(){{
        put(0,"待支付");
        put(1,"进行中");
        put(2,"已完成");
        put(3,"手动取消");
        put(4,"自动取消");
        put(5,"已删除");
    }};

    /**
     * 将OrderModel转化为OrderVO
     * @param orderModel 订单模型
     * @return OrderVO
     */
    private OrderVO convertFromModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderModel,orderVO);
        orderVO.setStatusName(ORDER_STATUS.get(new Integer(orderModel.getOrderStatus())));
        return orderVO;
    }

    /**
     * List<OrderModel>转List<OrderVO>
     * @param orderModelList 订单模型集合
     * @return List<OrderVO>
     */
    private List<OrderVO> convertFromOrderModelList(List<OrderModel> orderModelList){
        if (orderModelList == null){
            return null;
        }
        List<OrderVO> orderVOList = orderModelList.stream().map(orderModel -> {
            OrderVO orderVO = this.convertFromModel(orderModel);
            return orderVO;
        }).collect(Collectors.toList());
        return orderVOList;
    }

    /**
     * List<OrderItemModel> 转List<OrderItemVO>
     * @param orderItemModelList 订单商品模型集合
     * @return List<OrderItemVO>
     */
    private List<OrderItemVO> convertFromOrderItemList(List<OrderItemModel> orderItemModelList){
        if (orderItemModelList == null){
            return null;
        }
        List<OrderItemVO> orderItemVOList = orderItemModelList.stream().map(orderItemModel -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItemModel,orderItemVO);
            return orderItemVO;
        }).collect(Collectors.toList());

        return orderItemVOList;
    }
}
