package com.mall.controller;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.controller.viewobject.PromoItemVO;
import com.mall.controller.viewobject.PromoVO;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import com.mall.service.ItemService;
import com.mall.service.PromoService;
import com.mall.service.model.ItemModel;
import com.mall.service.model.PromoItemModel;
import com.mall.service.model.PromoModel;
import com.mall.util.ImgUpUtil;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 促销活动业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/6 10:34
 */

@Controller("promo")
@RequestMapping("/promo")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class PromoController extends BaseController{
    /**
     * Logger对象
     */
    private static Logger logger = Logger.getLogger(PromoController.class);

    /**
     * 秒杀活动功能接口
     */
    @Autowired
    private PromoService promoService;

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

    /**
     * 活动列表查看
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listPromo",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listPromo(){
        List<PromoModel> promoModelList = promoService.listPromo();
        return CommonReturnType.create(convertFromModelList(promoModelList));
    }

    /**
     * 查看活动详情
     * @param promoId 活动ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/selectPromo",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType selectPromo(@RequestParam("promoId")Integer promoId){
        PromoModel promoModel = promoService.selectPromo(promoId);
        return CommonReturnType.create(convertFromModel(promoModel));
    }

    /**
     * 一个活动商品列表浏览
     * @param promoId 活动ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listPromoItem",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listPromoItem(@RequestParam("promoId")Integer promoId){
        List<PromoItemModel> promoItemModelList = promoService.listPromoItem(promoId);
        List<PromoItemVO> promoItemVOList = promoItemModelList.stream().map(promoItemModel -> {
            PromoItemVO promoItemVO =  this.convertFromPromoItemModel(promoItemModel);
            return promoItemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(promoItemVOList);
    }

    @RequestMapping(value = "/getPromoItem",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getPromoItem(@RequestParam("promoItemId")Integer promoItemId){
        PromoItemModel promoItemModel = promoService.getPromoItemById(promoItemId);
        return CommonReturnType.create(convertFromPromoItemModel(promoItemModel));
    }

    //管理员操作
    /**
     * 创建促销活动
     * @param promoName 活动名
     * @param startTime 活动开始时间
     * @param endTime 活动结束时间
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员创建活动")
    @RequestMapping(value = "/addPromo",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addPromo(@RequestParam("promoName")String promoName,
                                     @RequestParam("startTime")Date startTime,
                                     @RequestParam("endTime")Date endTime,
                                     @RequestParam("promoImgUrl")String promoImgUrl) throws BusinessException, ParseException {
        validateLogon.validateAdminLogon();

        if (startTime.getTime() < endTime.getTime()){
            logger.info("开始时间不能晚于结束时间");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"开始时间不能晚于结束时间");
        }

        PromoModel promoModel = new PromoModel();
        promoModel.setPromoName(promoName);
        promoModel.setStartTime(startTime);
        promoModel.setEndTime(endTime);
        promoModel.setPromoImgUrl(promoImgUrl);

        PromoModel newPromoModel = promoService.addPromo(promoModel);
        return CommonReturnType.create(convertFromModel(newPromoModel));
    }

    /**
     * 图片上传
     * @param file 传输文件
     * @return CommonReturnType 图片地址
     * @throws BusinessException 业务异常
     * @throws IOException I / O异常
     */
    @RequestMapping(value = "/imgUp",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getImage(@RequestParam(value = "file") MultipartFile file) throws BusinessException, IOException {
        String filePath = ImgUpUtil.writeFile(file);
        return CommonReturnType.create(filePath);
    }

    /**
     * 修改活动信息
     * @param promoId 活动ID
     * @param promoName 活动名
     * @param startTime 活动开始时间
     * @param endTime 活动结束时间
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员修改活动信息")
    @RequestMapping(value = "/updatePromo",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updatePromo(@RequestParam("promoId")Integer promoId,
                                        @RequestParam("promoName")String promoName,
                                        @RequestParam("startTime")Date startTime,
                                        @RequestParam("endTime")Date endTime,
                                        @RequestParam("promoImgUrl")String promoImgUrl)throws BusinessException{
        validateLogon.validateAdminLogon();
        PromoModel promoModelForTran = new PromoModel();
        promoModelForTran.setPromoId(promoId);
        promoModelForTran.setPromoName(promoName);
        promoModelForTran.setStartTime(startTime);
        promoModelForTran.setEndTime(endTime);
        promoModelForTran.setPromoImgUrl(promoImgUrl);

        PromoModel newPromoModel = promoService.updatePromo(promoModelForTran);
        return CommonReturnType.create(convertFromModel(newPromoModel));
    }

    /**
     * 删除活动
     * @param promoId 活动ID
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员删除活动")
    @RequestMapping(value = "/deletePromo",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deletePromo(@RequestParam("promoId")Integer promoId) throws BusinessException {
        validateLogon.validateAdminLogon();
        promoService.deletePromo(promoId);
        return CommonReturnType.create(null);
    }

    /**
     * 添加活动商品
     * @param promoItemId 活动商品ID
     * @param promoId 活动ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员添加活动商品")
    @RequestMapping(value = "/addPromoItem",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addPromoItem(@RequestParam("promoItemId")Integer promoItemId,
                                         @RequestParam("promoId")Integer promoId) throws BusinessException {
        validateLogon.validateAdminLogon();
        promoService.addPromoItem(promoItemId,promoId);
        return CommonReturnType.create(null);
    }

    /**
     * 删除活动商品
     * @param promoItemId 活动商品ID
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员删除活动商品")
    @RequestMapping(value = "/deletePromoItem",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deletePromoItem(@RequestParam("promoItemId")Integer promoItemId) throws BusinessException {
        validateLogon.validateAdminLogon();
        promoService.deletePromoItem(promoItemId);
        return CommonReturnType.create(null);
    }

    /**
     * 修改活动商品活动价格
     * @param promoItemId 活动商品ID
     * @param promoItemPrice 活动商品价格
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员修改商品活动价")
    @RequestMapping(value = "/updatePromoItem",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updatePromoItem(@RequestParam("promoItemId")Integer promoItemId,
                                            @RequestParam("promoItemPrice")BigDecimal promoItemPrice) throws BusinessException {
        validateLogon.validateAdminLogon();
        promoService.updatePromoItem(promoItemId,promoItemPrice);
        return CommonReturnType.create(null);
    }

    /**
     * List<PromoModel>转List<PromoVO>
     * @param promoModelList 活动模型集合
     * @return List<PromoVO>
     */
    private List<PromoVO> convertFromModelList(List<PromoModel> promoModelList){
        if (promoModelList == null){
            return null;
        }
        List<PromoVO> promoVOList = promoModelList.stream().map(promoModel -> {
            PromoVO promoVO = this.convertFromModel(promoModel);
            return promoVO;
        }) .collect(Collectors.toList());
        return promoVOList;
    }

    private final static Map<Integer,String> PROMO_NAME = new HashMap<Integer, String>(){{
        put(1,"未开始");
        put(2,"进行中");
        put(3,"已结束");
    }};

    /**
     * PromoModel转PromoVO
     * @param promoModel 活动模型
     * @return PromoVO
     */
    private PromoVO convertFromModel(PromoModel promoModel){
        if (promoModel == null){
            return null;
        }
        PromoVO promoVO = new PromoVO();
        BeanUtils.copyProperties(promoModel,promoVO);
        promoVO.setPromoStatusName(PROMO_NAME.get(promoVO.getPromoStatus()));
        return promoVO;
    }

    /**
     * PromoItemModel转化为PromoItemVO
     * @param promoItemModel 活动商品视图对象
     * @return PromoItemVO
     */
    private PromoItemVO convertFromPromoItemModel(PromoItemModel promoItemModel){
        if (promoItemModel == null){
            return null;
        }
        PromoItemVO promoItemVO = new PromoItemVO();
        BeanUtils.copyProperties(promoItemModel,promoItemVO);

        ItemModel itemModel = itemService.getItemByItemId(promoItemModel.getPromoItemId());

        promoItemVO.setItemPrice(itemModel.getItemPrice());
        promoItemVO.setItemName(itemModel.getItemName());
        promoItemVO.setItemCategoryName(itemService.selectItemCategory(itemModel.getItemCategoryId()).getItemCategoryName());
        promoItemVO.setItemDescription(itemModel.getItemDescription());
        promoItemVO.setItemImageUrl(itemModel.getItemImageUrl());
        promoItemVO.setItemSales(itemModel.getItemSales());

        return promoItemVO;
    }
}
