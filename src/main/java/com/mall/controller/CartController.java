package com.mall.controller;

import com.mall.annotation.UserLogAnnotation;
import com.mall.controller.viewobject.CartVO;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import com.mall.service.CartService;
import com.mall.service.ItemService;
import com.mall.service.PromoService;
import com.mall.service.impl.CartServiceImpl;
import com.mall.service.model.*;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 购物车业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 13:39
 */

@Controller("cart")
@RequestMapping("/cart")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class CartController extends BaseController{
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(CartServiceImpl.class);
    /**
     * 购物车功能接口
     */
    @Autowired
    private CartService cartService;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 活动功能接口
     */
    @Autowired
    private PromoService promoService;

    /**
     * 登录校验工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 商品加入购物车
     * @param itemId 商品ID
     * @param amount 购买数量
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户添加商品到购物车")
    @RequestMapping(value = "/addCart",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addCart(@RequestParam("itemId")Integer itemId,
                                    @RequestParam("amount")Integer amount)throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        //检验购买数量是否准确
        int maxAmount = 99;
        if (amount <= 0 || amount > maxAmount){
            logger.info("数量信息错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息错误");
        }
        cartService.addCart(userModel.getUserId(),itemId,amount);
        return CommonReturnType.create(null);
    }

    /**
     * 删除购物车中某个商品
     * @param cartId 购物车ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户删除购物车某个商品")
    @RequestMapping(value = "/deleteCart",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteCart(@RequestParam("cartId")Integer cartId) throws BusinessException {
        validateLogon.validateLogon();
        cartService.deleteCart(cartId);
        return CommonReturnType.create(null);
    }

    /**
     * 批量删除商品
     * @param cartIdStr 购物车ID字符串
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户批量删除购物车商品")
    @RequestMapping(value = "/deleteCartMore",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteCartMore(@RequestParam("cartIdStr")String cartIdStr) throws BusinessException {
        validateLogon.validateLogon();
        String[] cartIdStrArr = cartIdStr.split(",");
        List<Integer> cartIds = new ArrayList<>();
        for (String cartId : cartIdStrArr){
            if (cartId != null) {
                cartIds.add(Integer.valueOf(cartId));
            }
        }
        cartService.deleteCartMore(cartIds);
        return CommonReturnType.create(null);
    }

    /**
     * 清空购物车
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户清空购物车商品")
    @RequestMapping(value = "/deleteCartAll",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType deleteCartAll()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        cartService.deleteCartAll(userModel.getUserId());
        return CommonReturnType.create(null);
    }

    /**
     * 获取用户购物车信息
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listUserCart",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType selectCart() throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        List<CartModel> cartModelList = cartService.selectCart(userModel.getUserId());
        List<CartVO> cartVOList = cartModelList.stream().map(cartModel -> {
            CartVO cartVO = this.convertFromModel(cartModel);
            return cartVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(cartVOList);
    }

    /**
     * 修改购买数量
     * @param cartId 购物车ID
     * @param number 更改数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/modifyAmount",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyAmount(@RequestParam("cartId")Integer cartId,
                                         @RequestParam("number")Integer number){
        cartService.updateAmount(cartId,number);
        return CommonReturnType.create(null);
    }

    private CartVO convertFromModel(CartModel cartModel){
        if (cartModel == null){
            return null;
        }
        CartVO cartVO = new CartVO();
        BeanUtils.copyProperties(cartModel,cartVO);

        ItemModel itemModel = itemService.getItemByItemId(cartVO.getItemId());
        cartVO.setShelves(itemModel.getShelves());

        cartVO.setItemImageUrl(itemModel.getItemImageUrl());
        cartVO.setItemName(itemModel.getItemName());

        PromoItemModel promoItemModel = promoService.getPromoItemById(cartVO.getItemId());
        if (promoItemModel != null){
            cartVO.setItemPrice(promoItemModel.getPromoItemPrice());
        }else {
            cartVO.setItemPrice(itemModel.getItemPrice());
        }

        return cartVO;
    }

}
