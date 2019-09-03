package com.mall.controller;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.controller.viewobject.ItemVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.ItemNumberService;
import com.mall.service.ItemService;
import com.mall.service.model.ItemCategoryModel;
import com.mall.service.model.ItemModel;
import com.mall.service.model.ItemNumberModel;
import com.mall.util.ImgUpUtil;
import com.mall.validator.ValidateLogon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 商品业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/1 09:05
 */

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController{
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
     * 校验登录工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 商品列表页面浏览
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listItem",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 商品详情查看
     * @param itemId 商品ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getItem",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "itemId")Integer itemId){
        ItemModel itemModel = itemService.getItemByItemId(itemId);
        ItemVO itemVO = convertFromItemModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    /**
     * 分页浏览商品
     * @param page 当前页码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/lisItemPage",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType lisItemPage(@RequestParam("page")Integer page) throws BusinessException {
        List<ItemModel> itemModelList = itemService.lisItemPage(page);
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 获取商品总条数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getItemPages",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItemPages(){
        int itemRows = itemService.getItemPages();
        return CommonReturnType.create(itemRows);
    }

    /**
     * 销量最高五件产品
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listPopItem",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listPopItem(){
        List<ItemModel> itemModelList = itemService.listPopItem();
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 最新六件产品
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listNewItem",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listNewItem(){
        List<ItemModel> itemModelList = itemService.listNewItem();
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 商品模糊查询
     * @param field 查询字段
     * @return CommonReturnType
     */
    @RequestMapping(value = "/fuzzySearch",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType fuzzySearch(@RequestParam("field")String field){
        List<ItemModel> itemModelList = itemService.fuzzySearch(field);
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 分类查询商品
      * @param itemCategoryId 商品类别ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listItemOfCategory",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItemOfCategory(@RequestParam("itemCategoryId")Integer itemCategoryId){
        List<ItemModel> itemModelList = itemService.listItemOfCategory(itemCategoryId);
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 按商品上下架状态查询
     * @param shelves 上下架状态
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listItemByShelves",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItemByShelves(@RequestParam("shelves")Byte shelves){
        List<ItemModel> itemModelList = itemService.selectItemByShelves(shelves);
        return CommonReturnType.create(convertFromModelList(itemModelList));
    }

    /**
     * 商品类别查询
     * @param itemCategoryId 商品类别ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getItemCategory",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType selectItemCategory(@RequestParam("itemCategoryId")Integer itemCategoryId){
        ItemCategoryModel itemCategoryModel = itemService.selectItemCategory(itemCategoryId);
        return CommonReturnType.create(itemCategoryModel);
    }

    /**
     * 商品类别列表
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listItemCategory",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItemCategory(){
        List<ItemCategoryModel> itemCategoryModelList = itemService.listItemCategory();
        return CommonReturnType.create(itemCategoryModelList);
    }

    //管理员对商品操作
    /**
     * 创建商品
     * @param itemName 商品名
     * @param itemCategoryId 商品类别ID
     * @param itemPrice 商品价格
     * @param itemDescription 商品描述
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员创建商品")
    @RequestMapping(value = "/createItem",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "itemName")String itemName,
                                       @RequestParam(name = "itemCategoryId")Integer itemCategoryId,
                                       @RequestParam(name = "itemPrice") BigDecimal itemPrice,
                                       @RequestParam(name = "itemDescription")String itemDescription,
                                       @RequestParam(name = "itemImageUrl")String itemImageUrl) throws BusinessException {
        validateLogon.validateAdminLogon();
        ItemModel itemModel = new ItemModel();
        itemModel.setItemName(itemName);
        itemModel.setItemCategoryId(itemCategoryId);
        itemModel.setItemPrice(itemPrice);
        itemModel.setItemDescription(itemDescription);
        itemModel.setItemImageUrl(itemImageUrl);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertFromItemModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);
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
    public CommonReturnType getImage(@RequestParam(value = "file")MultipartFile file) throws BusinessException, IOException {
        String filePath = ImgUpUtil.writeFile(file);
        return CommonReturnType.create(filePath);
    }

    /**
     * 删除商品
     * @param itemId 商品ID
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员删除商品")
    @RequestMapping(value = "/deleteItem",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteItem(@RequestParam("itemId")Integer itemId) throws BusinessException {
        validateLogon.validateAdminLogon();
        itemService.deleteItemByItemId(itemId);
        return CommonReturnType.create(null);
    }

    /**
     * 多条删除商品
     * @param itemIds 商品id字符串，例：1,2,3,4...
     * @return
     */
    @RequestMapping(value = "/deleteItemMore",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteItemMore(@RequestParam("itemIds")String itemIds) throws BusinessException {
        validateLogon.validateAdminLogon();
        List<Integer> itemIdList = new ArrayList<>();
        String[] ids = itemIds.split(",");
        for (String e:ids){
            itemIdList.add(Integer.valueOf(e));
        }
        itemService.deleteItemMore(itemIdList);
        return CommonReturnType.create(null);
    }

    /**
     * 修改商品信息
     * @param itemName 商品名
     * @param itemCategoryId 商品类别ID
     * @param itemPrice 商品价格
     * @param itemDescription 商品描述
     * @param itemImageUrl 商品图片描述
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员修改商品信息")
    @RequestMapping(value = "/modifyItem",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyItem(@RequestParam(name = "itemId")Integer itemId,
                                       @RequestParam(name = "itemName")String itemName,
                                       @RequestParam(name = "itemCategoryId")Integer itemCategoryId,
                                       @RequestParam(name = "itemPrice") String itemPrice,
                                       @RequestParam(name = "itemDescription")String itemDescription,
                                       @RequestParam(name = "itemImageUrl")String itemImageUrl) throws BusinessException {
        validateLogon.validateAdminLogon();
        ItemModel itemModel = itemService.getItemByItemId(itemId);
        itemModel.setItemName(itemName);
        itemModel.setItemCategoryId(itemCategoryId);
        itemModel.setItemPrice(new BigDecimal(itemPrice));
        itemModel.setItemDescription(itemDescription);
        itemModel.setItemImageUrl(itemImageUrl);

        ItemModel itemModelForReturn = itemService.modifyItemByItemId(itemModel);
        ItemVO itemVO = convertFromItemModel(itemModelForReturn);

        return CommonReturnType.create(itemVO);
    }

    /**
     * 添加商品类别
     * @param itemCategoryName 商品类名
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员添加商品类别")
    @RequestMapping(value = "/addItemCategory",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addItemCategory(@RequestParam("itemCategoryName")String itemCategoryName) throws BusinessException {
        validateLogon.validateAdminLogon();
        ItemCategoryModel itemCategoryModel = new ItemCategoryModel();
        itemCategoryModel.setItemCategoryName(itemCategoryName);

        itemService.addItemCategory(itemCategoryModel);

        return CommonReturnType.create(null);
    }

    /**
     * 删除某个商品类别
     * @param itemCategoryId 商品类别ID
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员删除某个商品类别")
    @RequestMapping(value = "/deleteItemCategory",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteItemCategory(@RequestParam("itemCategoryId")Integer itemCategoryId) throws BusinessException {
        validateLogon.validateAdminLogon();
        itemService.deleteItemCategory(itemCategoryId);
        return CommonReturnType.create(null);
    }

    /**
     * 商品上下架
     * @param itemId 商品ID
     * @return CommonReturnType
     */
    @AdminLogAnnotation(adminWork = "管理员将商品上架或下架")
    @RequestMapping(value = "/itemOnOrOffShelves",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType itemOnShelves(@RequestParam("itemId")Integer itemId) throws BusinessException {
        validateLogon.validateAdminLogon();
        ItemModel itemModel = itemService.updateShelves(itemId);
        ItemVO itemVO = convertFromItemModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private final static Map<Integer,String> SHELVES_NAME = new HashMap<Integer, String>(){{
        put(0,"未上架");
        put(1,"已上架");
        put(2,"活动中");
    }};

    /**
     * itemModel转化为itemVO
     * @param itemModel 商品模型
     * @return ItemVO
     */
    private ItemVO convertFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        ItemNumberModel itemNumberModel = itemNumberService.getByItemId(itemModel.getItemId());
        itemVO.setItemNumber(itemNumberModel.getItemNumber());
        itemVO.setItemCategoryName(itemService.selectItemCategory(itemModel.getItemCategoryId()).getItemCategoryName());
        itemVO.setShelvesName(SHELVES_NAME.get(new Integer(itemVO.getShelves())));
        return itemVO;
    }

    /**
     * List<ItemModel> 转化为List<ItemVO>
     * @param itemModelList 商品模型集合
     * @return List<ItemVO>
     */
    private List<ItemVO> convertFromModelList(List<ItemModel> itemModelList){
        if (itemModelList == null){
            return null;
        }
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertFromItemModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return itemVOList;
    }
}
