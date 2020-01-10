package com.mall.controller;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.controller.viewobject.ItemNumberVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.ItemNumberService;
import com.mall.service.ItemService;
import com.mall.service.model.ItemModel;
import com.mall.service.model.ItemNumberModel;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 17:26
 */

@Controller("itemNumber")
@RequestMapping("/itemNumber")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemNumberController extends BaseController{
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(ItemNumberController.class);

    /**
     * 商品库存功能接口
     */
    @Autowired
    private ItemNumberService itemNumberService;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 登录校验
     */
    @Autowired
    private ValidateLogon validateLogon;

    //管理员操作
    /**
     * 获取商品库存
     * @param itemId 商品ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getItemNumberByItemId",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItemNumberByItemId(@RequestParam("itemId")Integer itemId) throws BusinessException {
        validateLogon.validateAdminLogon();
        ItemNumberModel itemNumberModel = itemNumberService.getByItemId(itemId);
        return CommonReturnType.create(convertFromModel(itemNumberModel));
    }


    /**
     * 修改库存
     * @param itemId  商品ID
     * @param itemNumber k库存
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员修改库存")
    @RequestMapping(value = "/updateItemNumber",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updateItemNumber(@RequestParam("itemId")Integer itemId,
                                             @RequestParam("itemNumber")Integer itemNumber) throws BusinessException {
        validateLogon.validateAdminLogon();
        itemNumberService.updateItemNumber(itemId,itemNumber);
        return CommonReturnType.create(null);
    }

    /**
     * ItemNumberModel转化为ItemNumberVO
     * @param itemNumberModel 库存模型
     * @return ItemNumberVO
     */
    private ItemNumberVO convertFromModel(ItemNumberModel itemNumberModel){
        if (itemNumberModel == null){
            return null;
        }
        ItemNumberVO itemNumberVO = new ItemNumberVO();
        BeanUtils.copyProperties(itemNumberModel,itemNumberVO);
        ItemModel itemModel = itemService.getItemByItemId(itemNumberModel.getItemId());
        itemNumberVO.setItemName(itemModel.getItemName());
        itemNumberVO.setItemImageUrl(itemModel.getItemImageUrl());
        return itemNumberVO;
    }
}
