package com.mall.service.impl;

import com.mall.dao.MemberDao;
import com.mall.dao.OrderDao;
import com.mall.dao.OrderItemDao;
import com.mall.dao.PointsMemberDao;
import com.mall.dao.SequenceDao;
import com.mall.entity.Member;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.entity.PointsMember;
import com.mall.entity.Sequence;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.AddressService;
import com.mall.service.CartService;
import com.mall.service.ItemNumberService;
import com.mall.service.ItemService;
import com.mall.service.OrderService;
import com.mall.service.PointsService;
import com.mall.service.PromoService;
import com.mall.service.UserService;
import com.mall.service.model.AddressModel;
import com.mall.service.model.CartModel;
import com.mall.service.model.ItemModel;
import com.mall.service.model.ItemNumberModel;
import com.mall.service.model.OrderItemModel;
import com.mall.service.model.OrderModel;
import com.mall.service.model.PointsModel;
import com.mall.service.model.PromoItemModel;
import com.mall.service.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 订单功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 17:08
 */

@Service
public class OrderServiceImpl implements OrderService {
    /**
     * 声明Logger对象
     */
    private final static Logger logger = Logger.getLogger(OrderServiceImpl.class);

    /**
     * 每页显示订单数
     */
    private static final int NUMBERS = 5;

    /**
     * 订单访问对象
     */
    @Autowired
    private OrderDao orderDao;

    /**
     * 订单商品访问对象
     */
    @Autowired
    private OrderItemDao orderItemDao;

    /**
     * 用户会员访问对象
     */
    @Autowired
    private MemberDao memberDao;

    /**
     * 积分等级表访问对象
     */
    @Autowired
    private PointsMemberDao pointsMemberDao;

    /**
     * 序列访问对象
     */
    @Autowired
    private SequenceDao sequenceDao;

    /**
     * 积分访功能接口
     */
    @Autowired
    private PointsService pointsService;

    /**
     * 地址功能接口
     */
    @Autowired
    private AddressService addressService;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 商品库存功能接口
     */
    @Autowired
    private ItemNumberService itemNumberService;

    /**
     * 用户功能接口
     */
    @Autowired
    private UserService userService;

    /**
     * 活动功能接口
     */
    @Autowired
    private PromoService promoService;

    /**
     * 购物车功能接口
     */
    @Autowired
    private CartService cartService;

    /**
     * 创建订单（多商品）
     * @param userId 用户ID
     * @param cartIds 购物车ID集合
     * @return OrderModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel createOrderMoreItem(Integer userId, List<Integer> cartIds) throws BusinessException {
        //检验用户是否存在
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            logger.info("用户不存在");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //订单号
        String orderId = generateOrderNo();
        BigDecimal totalPrice = new BigDecimal(0);
        //对购物车中商品操作
        for (Integer cartId : cartIds){
            CartModel cartModel = cartService.getCartById(cartId);
            ItemModel itemModel = itemService.getItemByItemId(cartModel.getItemId());
            ItemNumberModel itemNumberModel = itemNumberService.getByItemId(cartModel.getItemId());
            //检验商品是否下架
            if (itemModel.getShelves() == 0){
                logger.info("商品已下架");
                throw new BusinessException(EmBusinessError.ITEM_OFF_SHELVES);
            }
            //检验购买数量是否符合要求
            int maxAmount = 99;
            if (cartModel.getAmount() <= 0 || cartModel.getAmount() > maxAmount){
                logger.info("购买数量错误( < 0 或 > 99 )");
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息错误");
            }
            if ( cartModel.getAmount() > itemNumberModel.getItemNumber()){
                logger.info("商品库存不足");
                throw new BusinessException(EmBusinessError.ITEM_NUMBER_NOT_ENOUGH);
            }
            //落单减库存
            boolean result = itemNumberService.decreaseItemNumber(cartModel.getItemId(),cartModel.getAmount());
            if (!result){
                logger.info("减库存失败");
                throw new BusinessException(EmBusinessError.ITEM_NUMBER_NOT_ENOUGH);
            }

            //订单商品入库
            OrderItemModel orderItemModel = new OrderItemModel();
            orderItemModel.setItemId(itemModel.getItemId());
            orderItemModel.setItemName(itemModel.getItemName());
            orderItemModel.setItemImageUrl(itemModel.getItemImageUrl());
            //设置订单商品价格，判断是否为活动商品
            PromoItemModel promoItemModel = promoService.getPromoItemById(itemModel.getItemId());
            if (promoItemModel != null){
                orderItemModel.setItemPrice(promoItemModel.getPromoItemPrice());
            }else {
                orderItemModel.setItemPrice(itemModel.getItemPrice());
            }
            orderItemModel.setAmount(cartModel.getAmount());
            orderItemModel.setOrderId(orderId);
            //订单总价计算
            totalPrice = totalPrice.add(orderItemModel.getItemPrice().multiply(new BigDecimal(orderItemModel.getAmount())));

            OrderItem orderItem = convertFromOrderItemModel(orderItemModel);
            orderItemDao.insertSelective(orderItem);

            //加销量
            itemService.increaseItemSales(cartModel.getItemId(),cartModel.getAmount());
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setOrderPrice(totalPrice);
        orderModel.setOrderId(orderId);

        //如果存在默认地址，设置收货地址为此地址
        OrderModel orderModel2 = confirmDefaultAddress(orderModel,userId);

        Order order = convertFromModel(orderModel2);
        orderDao.insertSelective(order);

        //删除购物车中该商品
        cartService.deleteCartMore(cartIds);

        return getOrder(orderId);
    }

    /**
     * 创建订单
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return OrderModel
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //检验用户是否存在
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            logger.info("用户不存在");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //检验商品是否存在
        ItemModel itemModel = itemService.getItemByItemId(itemId);
        if (itemModel == null){
            logger.info("商品不存在");
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        //检验商品是否下架
        if (itemModel.getShelves() == 0){
            logger.info("商品已下架");
            throw new BusinessException(EmBusinessError.ITEM_OFF_SHELVES);
        }
        //检验购买数量是否符合要求
        int maxAmount = 99;
        if (amount <= 0 || amount > maxAmount){
            logger.info("数量信息错误( < 0 或 > 99 )");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息错误");
        }
        //落单减库存
        boolean result = itemNumberService.decreaseItemNumber(itemId,amount);
        if (!result){
            logger.info("商品库存不足");
            throw new BusinessException(EmBusinessError.ITEM_NUMBER_NOT_ENOUGH);
        }
        //订单号
        String orderId = generateOrderNo();

        //订单商品入库
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setItemId(itemId);
        orderItemModel.setItemName(itemModel.getItemName());
        orderItemModel.setItemImageUrl(itemModel.getItemImageUrl());
        //设置订单商品价格，判断是否为活动商品
        PromoItemModel promoItemModel = promoService.getPromoItemById(itemId);
        if (promoItemModel != null){
            orderItemModel.setItemPrice(promoItemModel.getPromoItemPrice());
        }else {
            orderItemModel.setItemPrice(itemModel.getItemPrice());
        }
        orderItemModel.setAmount(amount);
        orderItemModel.setOrderId(orderId);

        OrderItem orderItem = convertFromOrderItemModel(orderItemModel);
        orderItemDao.insertSelective(orderItem);

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setOrderPrice(orderItemModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setOrderId(orderId);

        //如果用户存在默认地址，设置收货地址为此地址
        OrderModel orderModel2 = confirmDefaultAddress(orderModel,userId);

        Order order = convertFromModel(orderModel2);
        orderDao.insertSelective(order);

        //加销量
        itemService.increaseItemSales(itemId,amount);

        //删除购物车中该商品
        List<CartModel> cartModelList = cartService.selectCart(userId);
        for (CartModel cartModel : cartModelList){
            if (cartModel.getItemId().equals(itemId)){
                cartService.deleteCart(cartModel.getCartId());
            }
        }

        return getOrder(orderId);
    }

    /**
     * 订单号生成
     * @return String
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    protected String generateOrderNo(){

        //订单号16位，前8位为时间，中6位为自增序列，后2位为分库分表位(设为00)
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);

        //获取当前sequence
        Sequence sequence = sequenceDao.getSequenceByName("order_info");
        int sequenceValue = sequence.getCurrentValue();

        //当当前sequence值大于最大值，则将其赋值为初始值，形成循环
        int currentValue = sequence.getCurrentValue() + sequence.getStep();
        if (currentValue > sequence.getMaxValue()){
            sequence.setCurrentValue(sequence.getInitValue());
        }else {
            sequence.setCurrentValue(currentValue);
        }
        sequenceDao.updateByName(sequence);

        String sequenceStr = String.valueOf(sequenceValue);
        int figures = 6;
        for (int i = 0;i < figures - sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);

        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    /**
     * 订单完成支付（状态号改为 1 进行中，产生积分）
     * @param orderId 订单号
     * @return OrderModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel payOrder(String orderId)throws BusinessException {
        Order order = orderDao.selectOrder(orderId);
        if (order.getAddressId() == 0){
            logger.info("收货地址未选择");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请先选择收货地址");
        }
        if (order.getOrderStatus() >= 1){
            logger.info("订单已支付或已取消");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单已支付或已取消");
        }
        order.setOrderStatus((byte) 1);
        BigDecimal orderPrice = order.getOrderPrice();
        //产生积分数为消费金额（大于该数的最小整数）
        int pointsNumber = orderPrice.intValue() + 1;
        order.setPointsNumber(pointsNumber);
        orderDao.updateOrder(order);

        UserModel userModel = userService.getUserById(order.getUserId());

        //积分产生记录插入积分明细表
        PointsModel pointsModel = new PointsModel();
        pointsModel.setPointsNumber(pointsNumber);
        pointsModel.setUserId(userModel.getUserId());
        pointsModel.setUserName(userModel.getUserName());
        pointsModel.setOrderId(orderId);
        pointsService.generatePoints(pointsModel);

        //确认用户会员等级
        int pointsNumbers = pointsService.getUserMemberLevel(userModel.getUserId());
        Member member = memberDao.selectByUserId(order.getUserId());
        List<PointsMember> pointsMemberList = pointsMemberDao.listPointsMember();
        for (PointsMember pointsMember : pointsMemberList){
            if (pointsNumbers < pointsMember.getPointsNumbers()){
                member.setMemberLevel(pointsMember.getMemberLevel());
                memberDao.updateById(member);
                break;
            }
        }
        return convertFromDO(order);
    }
    /**
     * 确认订单完成（订单状态号改为 2 完成）
     * @param orderId 订单号
     * @return OrderModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel endOrder(String orderId) throws BusinessException {
        Order order = orderDao.selectOrder(orderId);
        int status = 2;
        if (order.getOrderStatus() == status){
            logger.info("订单已完成");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单已完成");
        }else if (order.getOrderStatus() == 0){
            logger.info("订单还未支付");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单还未支付");
        }
        order.setOrderStatus((byte) status);
        orderDao.updateOrder(order);
        return convertFromDO(order);
    }

    /**
     * 设置订单收货地址
     * @param orderId 订单ID
     * @param addressId 地址ID
     * @return OrderModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel setOrderAddress(String orderId, Integer addressId)throws BusinessException {
        Order order = orderDao.selectOrder(orderId);
        order.setAddressId(addressId);
        AddressModel addressModel = addressService.getAddressById(addressId);
        order.setRecipientName(addressModel.getRecipientName());
        order.setRecipientPhone(String.valueOf(addressModel.getRecipientPhone()));
        order.setAddressDetail(addressModel.getAddressDetail());
        int affectRow =  orderDao.updateOrder(order);
        if (affectRow == 0){
            logger.info("选择地址失败");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"选择地址失败");
        }
        return convertFromDO(order);
    }

    /**
     * 查看所有订单（后台）
     * @return List<OrderModel>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OrderModel> listOrder() {
        List<Order> orderList = orderDao.selectOrderAll();
        List<OrderModel> orderModelList = orderList.stream().map(order -> {
            Order order2 = confirmOrderPayInTime(order);
            OrderModel orderModel = this.convertFromDO(order2);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    /**
     * 分页数
     * @return int
     */
    @Override
    public int getOrderPages() {
        int orderRows = orderDao.selectOrderRows();
        int pages = (int)Math.ceil((double)orderRows/NUMBERS);
        return pages;
    }

    /**
     * 订单分页查询
     * @param page 页码
     * @return List<OrderModel>
     * @throws BusinessException 业务异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OrderModel> listOrderPages(Integer page) throws BusinessException {
        if (page > getOrderPages() || page < 1){
            logger.info("页码错误");
            throw new BusinessException(EmBusinessError.PAGE_NUMBER_ERROR);
        }
        int offset = (page-1)*NUMBERS;
        List<Order> orderList = orderDao.listOrderPages(offset,NUMBERS);
        List<OrderModel> orderModelList = orderList.stream().map(order -> {
            Order order2 = confirmOrderPayInTime(order);
            OrderModel orderModel = this.convertFromDO(order2);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    /**
     * 订单搜索
     * @param field 搜索字段
     * @return List<OrderModel>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OrderModel> orderSearch(String field) {
        List<Order> orderList = orderDao.orderSearch(field);
        List<OrderModel> orderModelList = orderList.stream().map(order -> {
            Order order2 = confirmOrderPayInTime(order);
            OrderModel orderModel = this.convertFromDO(order2);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    /**
     * 查询用户所有订单（订单状态 < 2）
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OrderModel> listOrderOfUser(Integer userId) {
        List<Order> orderListOfUser = orderDao.selectOrderOfUser(userId);
        List<OrderModel> orderModels = orderListOfUser.stream().map(order -> {
            Order order2 = confirmOrderPayInTime(order);
            OrderModel orderModel = this.convertFromDO(order2);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModels;
    }

    /**
     * 查询用户已完成订单（订单状态 2）
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    @Override
    public List<OrderModel> listEndOrderOfUser(Integer userId) {
        List<Order> orderList = orderDao.selectEndOrderOfUser(userId);
        return convertFromOrderList(orderList);
    }

    /**
     * 查询用户已取消订单（订单状态 3或4）
     * @param userId 用户ID
     * @return List<OrderModel>
     */
    @Override
    public List<OrderModel> listOrderOfUserCancel(Integer userId) {
        List<Order> orderList = orderDao.selectOrderOfUserCancel(userId);
        return convertFromOrderList(orderList);
    }

    /**
     * 查看订单详情
     * @param orderId 订单ID
     * @return OrderModel
     */
    @Override
    public OrderModel getOrder(String orderId) throws BusinessException{
        if (orderId == null){
            logger.info("订单ID为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单ID为空");
        }
        Order order = orderDao.selectOrder(orderId);
        Order order2 = confirmOrderPayInTime(order);
        return convertFromDO(order2);
    }

    /**
     * 查询订单商品
     * @param orderId 订单ID
     * @return List<OrderItemModel>
     */
    @Override
    public List<OrderItemModel> getOrderItem(String orderId) {
        List<OrderItem> orderItemList = orderItemDao.selectOrderItemByOrderId(orderId);
        List<OrderItemModel> orderItemModelList = orderItemList.stream().map(orderItem -> {
            OrderItemModel orderItemModel = new OrderItemModel();
            BeanUtils.copyProperties(orderItem,orderItemModel);
            return orderItemModel;
        }).collect(Collectors.toList());
        return orderItemModelList;
    }

    /**
     * 通过订单状态获取订单
     * @param orderStatus 订单状态
     * @return List<OrderModel>
     */
    @Override
    public List<OrderModel> getOrderByStatus(Byte orderStatus) {
        List<Order> orderList = orderDao.selectOrderByStatus(orderStatus);
        return convertFromOrderList(orderList);
    }

    /**
     * 用户删除订单（订单状态号改为 5 已删除）
     * @param orderId 订单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel deleteOrderUser(String orderId) throws BusinessException{
        Order order = orderDao.selectOrder(orderId);
        int requireMinStatus = 2;
        if (order.getOrderStatus() < requireMinStatus){
            logger.info("请先取消或完成订单");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请先取消或完成订单");
        }
        int status = 5;
        order.setOrderStatus((byte) status);
        orderDao.updateOrder(order);
        return convertFromDO(order);
    }

    /**
     * 取消订单（订单状态号改为 3 手动取消）
     * @param orderId 订单ID
     * @return OrderModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderModel cancelOrder(String orderId) throws BusinessException {
        Order order = orderDao.selectOrder(orderId);
        int overStatus = 2;
        //订单待支付
        if (order.getOrderStatus() == 0){
            //恢复库存、销量
            recoverItem(orderId);
        }//订单为进行中
        else if (order.getOrderStatus() == 1){
            //退款，这里不做操作
            //产生负积分明细
            generationPoints(order);
            //恢复库存、销量
            recoverItem(orderId);
        }//订单为已完成
        else if (order.getOrderStatus() == overStatus){
            logger.info("订单已完成");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单已完成");
        }
        int status = 3;
        order.setOrderStatus((byte) status);
        orderDao.updateOrder(order);
        return convertFromDO(order);
    }

    /**
     * 取消产生负积分明细
     * @param order 订单实体
     * @throws BusinessException 业务异常
     */
    private void generationPoints(Order order)throws BusinessException{
        BigDecimal orderPrice = order.getOrderPrice();
        //产生积分数为消费金额（大于该数的最小整数）
        int pointsNumber = 0 - (orderPrice.intValue() + 1);

        PointsModel pointsModel = new PointsModel();

        pointsModel.setPointsNumber(pointsNumber);
        UserModel userModel = userService.getUserById(order.getUserId());
        pointsModel.setUserId(userModel.getUserId());
        pointsModel.setUserName(userModel.getUserName());
        pointsModel.setOrderId(order.getOrderId());

        pointsService.generatePoints(pointsModel);
    }

    /**
     * 恢复商品销量、库存
     * @param orderId 订单ID
     * @throws BusinessException 业务异常
     */
    private void recoverItem(String orderId) throws BusinessException {
        List<OrderItem> orderItemList = orderItemDao.selectOrderItemByOrderId(orderId);
        for (OrderItem orderItem : orderItemList){
            ItemModel itemModel = itemService.getItemByItemId(orderItem.getItemId());
            itemModel.setItemSales(itemModel.getItemSales() - orderItem.getAmount());
            itemService.modifyItemByItemId(itemModel);

            ItemNumberModel itemNumberModel = itemNumberService.getByItemId(orderItem.getItemId());
            itemNumberService.updateItemNumber(orderItem.getItemId(),
                    itemNumberModel.getItemNumber() + orderItem.getAmount());
        }
    }

    /**
     * 确认订单在15分钟内未支付自动取消
     * @param order 订单实体
     */
    private Order confirmOrderPayInTime(Order order){
        Date nowTime = new Date();
        int timing = 9000000;
        if (order.getOrderStatus() == 0 &&
                nowTime.getTime() - order.getCreateTime().getTime() >= timing){
            int status = 4;
            order.setOrderStatus((byte) status);
            orderDao.updateOrder(order);
            //恢复库存、销量
            try {
                recoverItem(order.getOrderId());
            } catch (BusinessException e) {
                logger.info(e);
            }
        }
        return order;
    }

    /**
     * 确认用户是否存在默认收货地址，若有，则设置为订单收货地址
     * @param orderModel 订单模型
     * @return OrderModel
     */
    private OrderModel confirmDefaultAddress(OrderModel orderModel,Integer userId){
        //如果存在默认地址，设置收货地址为此地址
        List<AddressModel> addressModelList = addressService.getAddressByUserId(userId);
        if (addressModelList != null){
            for (AddressModel addressModel : addressModelList){
                if (addressModel.getDefaultAddress() == 1){
                    orderModel.setAddressId(addressModel.getAddressId());
                    orderModel.setRecipientName(addressModel.getRecipientName());
                    orderModel.setRecipientPhone(String.valueOf(addressModel.getRecipientPhone()));
                    orderModel.setAddressDetail(addressModel.getAddressDetail());
                    break;
                }
            }
        }
        return orderModel;
    }

    /**
     * OrderItemModel转化为OrderItem
     * @param orderItemModel 订单商品模型
     * @return OrderItem
     */
    private OrderItem convertFromOrderItemModel(OrderItemModel orderItemModel){
        if (orderItemModel == null){
            return null;
        }
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemModel,orderItem);
        return orderItem;
    }

    /**
     * 将OrderModel转化为Order
     * @param orderModel 订单模型
     * @return Order
     */
    private Order convertFromModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderModel,order);
        return order;
    }

    /**
     * 将Order转化为OrderModel
     * @param order 订单实体
     * @return OrderModel
     */
    private OrderModel convertFromDO(Order order){
        if (order == null){
            return null;
        }
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(order,orderModel);
        return orderModel;
    }

    /**
     * 将List<Order>转化为List<OrderModel>
     * @param orderList 订单集合
     * @return List<OrderModel>
     */
    private List<OrderModel> convertFromOrderList(List<Order> orderList){
        if (orderList == null){
            return null;
        }
        List<OrderModel> orderModelList = orderList.stream().map(order -> {
            OrderModel orderModel = this.convertFromDO(order);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }
}
