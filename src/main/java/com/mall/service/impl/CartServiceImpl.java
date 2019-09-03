package com.mall.service.impl;

import com.mall.dao.CartDao;
import com.mall.entity.Cart;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.CartService;
import com.mall.service.ItemService;
import com.mall.service.UserService;
import com.mall.service.model.CartModel;
import com.mall.service.model.ItemModel;
import com.mall.service.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 购物车功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 12:43
 */

@Service
public class CartServiceImpl implements CartService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(CartServiceImpl.class);

    /**
     * 购物车访问对象
     */
    @Autowired
    private CartDao cartDao;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 用户功能接口
     */
    @Autowired
    private UserService userService;

    /**
     * 商品加入购物车
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return CartModel
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCart(Integer userId,Integer itemId,Integer amount) throws BusinessException {
        //检验商品是否存在
        ItemModel itemModel = itemService.getItemByItemId(itemId);
        if (itemModel == null){
            logger.info("商品不存在");
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        //检验用户是否存在
        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            logger.info("用户不存在");
            throw new  BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //检验购买数量是否准确
        int maxAmount = 99;
        if (amount <= 0 || amount > maxAmount){
            logger.info("数量信息错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息错误");
        }

        //检验该用户购物车中是否已存在该商品，若存在，则只增加已存在商品购买数量
        //记录以后购物车表是否存在该商品
        int record = 0;
        //用于记录该商品的购物车ID
        int cartId = 0;
        List<Cart> cartList = cartDao.selectCart(userId);
        for (Cart cart : cartList){
            if (cart.getItemId().equals(itemId)){
                record = 1;
                cartId = cart.getCartId();
            }
        }
        if (record == 1){
            cartDao.updateAmount(cartId,amount);
        }else {
            //加入购物车
            CartModel cartModel = new CartModel();
            cartModel.setUserId(userId);
            cartModel.setItemId(itemId);
            cartModel.setAmount(amount);
            Cart cart2 = convertFromModel(cartModel);
            cartDao.insertSelective(cart2);
        }
    }

    /**
     * 删除购物车中某个商品
     * @param cartId 购物车ID
     */
    @Override
    public void deleteCart(Integer cartId) {
        cartDao.deleteCart(cartId);
    }

    /**
     * 批量删除商品
     * @param cartIds 购物车ID集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartMore(List<Integer> cartIds) {
        cartDao.deleteCartMore(cartIds);
    }

    /**
     * 清空购物车
     * @param userId 用户ID
     */
    @Override
    public void deleteCartAll(Integer userId) {
        cartDao.deleteCartOfUser(userId);
    }

    /**
     * 获取用户购物车信息
     * @param userId 用户ID
     * @return CartModel
     */
    @Override
    public List<CartModel> selectCart(Integer userId) {
        List<Cart> cartList = cartDao.selectCart(userId);
        List<CartModel> cartModelList = cartList.stream().map(cart -> {
            CartModel cartModel =  this.convertFromDO(cart);
            return cartModel;
        }).collect(Collectors.toList());
        return cartModelList;
    }

    /**
     * 通过购物车ID查询购物车中商品
     * @param cartId 商品ID
     * @return CartModel
     */
    @Override
    public CartModel getCartById(Integer cartId) {
        Cart cart = cartDao.selectCartById(cartId);
        return convertFromDO(cart);
    }

    /**
     * 修改购买数量
     * @param cartId 购物车ID
     * @param number 更改数
     */
    @Override
    public void updateAmount(Integer cartId, Integer number) {
        cartDao.updateAmount(cartId,number);
    }

    /**
     * CartModel转化为Cart
     * @param cartModel 购物车模型
     * @return Cart
     */
    private Cart convertFromModel(CartModel cartModel){
        if (cartModel == null){
            return null;
        }
        Cart cart = new Cart();
        BeanUtils.copyProperties(cartModel,cart);
        return cart;
    }

    /**
     * Cart转化为CartModel
     * @param cart 购物车实体
     * @return CartModel
     */
    private CartModel convertFromDO(Cart cart){
        if (cart == null){
            return null;
        }
        CartModel cartModel = new CartModel();
        BeanUtils.copyProperties(cart,cartModel);
        return cartModel;
    }
}
