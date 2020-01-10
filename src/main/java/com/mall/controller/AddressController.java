package com.mall.controller;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.annotation.UserLogAnnotation;
import com.mall.controller.viewobject.AddressVO;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import com.mall.service.AddressService;
import com.mall.service.model.AddressModel;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 地址业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 11:31
 */

@Controller("address")
@RequestMapping("/address")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class AddressController extends BaseController {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(AddressController.class);

    /**
     * 地址功能接口
     */
    @Autowired
    private AddressService addressService;

    /**
     * 登录验证工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 通过地址ID获得地址信息
     * @param addressId 地址ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getAddress")
    @ResponseBody
    public CommonReturnType getAddress(@RequestParam("addressId") Integer addressId) throws BusinessException{
        AddressModel addressModel = addressService.getAddressById(addressId);
        if (addressModel == null){
            logger.info("地址不存在");
            throw new BusinessException(EmBusinessError.ADDRESS_NOT_EXIST);
        }
        AddressVO addressVO = convertFromModel(addressModel);
        return CommonReturnType.create(addressVO);
    }

    /**
     * 获取一个用户的收货地址
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getUserAddress")
    @ResponseBody
    public CommonReturnType getUserAddress() throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<AddressModel> addressModelList = addressService.getAddressByUserId(userModel.getUserId());
        List<AddressVO> addressVOList = addressModelList.stream().map(addressModel -> {
            AddressVO addressVO = convertFromModel(addressModel);
            return addressVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(addressVOList);
    }

    /**
     * 添加收货地址
     * @param recipientName 收件人姓名
     * @param recipientPhone 收件人手机号
     * @param addressDetail 详细地址
     * @param defaultAddress 默认地址（是/否）
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户添加收货地址")
    @RequestMapping(value = "/addAddress",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType addAddress(@RequestParam(name = "recipientName")String recipientName,
                                       @RequestParam(name = "recipientPhone")Long recipientPhone,
                                       @RequestParam(name = "addressDetail")String addressDetail,
                                       @RequestParam(name = "defaultAddress")Integer defaultAddress) throws BusinessException{


        UserModel userModel = validateLogon.validateLogon();

        AddressModel addressModel = new AddressModel();
        addressModel.setRecipientName(recipientName);
        addressModel.setRecipientPhone(recipientPhone);
        addressModel.setAddressDetail(addressDetail);
        addressModel.setDefaultAddress(new Byte(String.valueOf(defaultAddress.intValue())));
        addressModel.setUserId(userModel.getUserId());

        addressService.addAddress(addressModel);
        return CommonReturnType.create(null);
    }

    /**
     * 修改收货地址
     * @param addressId 地址ID
     * @param recipientName 收件人姓名
     * @param recipientPhone 收件人手机号
     * @param addressDetail 详细地址
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户修改收货地址")
    @RequestMapping(value = "/modifyAddress",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyAddress(@RequestParam(name = "addressId")Integer addressId,
                                          @RequestParam(name = "recipientName")String recipientName,
                                          @RequestParam(name = "recipientPhone")Long recipientPhone,
                                          @RequestParam(name = "addressDetail")String addressDetail)throws BusinessException{

        validateLogon.validateLogon();

        AddressModel addressModel = addressService.getAddressById(addressId);
        addressModel.setRecipientName(recipientName);
        addressModel.setRecipientPhone(recipientPhone);
        addressModel.setAddressDetail(addressDetail);

        addressService.modifyAddress(addressModel);
        return CommonReturnType.create(null);
    }

    /**
     * 删除地址
     * @param addressId 地址ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户删除收货地址")
    @RequestMapping(value = "/deleteAddress",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteAddress(@RequestParam(name = "addressId")Integer addressId) throws BusinessException{
        addressService.deleteAddress(addressId);
        return CommonReturnType.create(null);
    }

    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户设置默认收货地址")
    @RequestMapping(value = "/setDefaultAddress",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType setDefaultAddress(@RequestParam(name = "addressId")Integer addressId)throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        addressService.setDefaultAddress(addressId,userModel.getUserId());
        return CommonReturnType.create(null);
    }

    //管理员操作
    /**
     * 获取一个用户的收货地址
     * @param userId 用户ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/getUserAddressByAdmin")
    @ResponseBody
    public CommonReturnType getUserAddressByAdmin(@RequestParam(name = "userId")Integer userId) throws BusinessException{
        validateLogon.validateAdminLogon();
        List<AddressModel> addressModelList = addressService.getAddressByUserId(userId);
        List<AddressVO> addressVOList = addressModelList.stream().map(addressModel -> {
            AddressVO addressVO = convertFromModel(addressModel);
            return addressVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(addressVOList);
    }

    /**
     * 修改收货地址
     * @param addressId 地址ID
     * @param recipientName 收件人姓名
     * @param recipientPhone 收件人手机号
     * @param addressDetail 详细地址
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */

    @AdminLogAnnotation(adminWork = "管理员修改用户地址")
    @RequestMapping(value = "/modifyAddressByAdmin",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyAddressByAdmin(@RequestParam(name = "addressId")Integer addressId,
                                                 @RequestParam(name = "recipientName")String recipientName,
                                                 @RequestParam(name = "recipientPhone")Long recipientPhone,
                                                 @RequestParam(name = "addressDetail")String addressDetail)throws BusinessException{
        AddressModel addressModel = addressService.getAddressById(addressId);
        addressModel.setRecipientName(recipientName);
        addressModel.setRecipientPhone(recipientPhone);
        addressModel.setAddressDetail(addressDetail);

        addressService.modifyAddress(addressModel);
        return CommonReturnType.create(null);
    }

    /**
     * 将model转化为viewobject
     * @param addressModel 地址模型
     * @return AddressVO
     */
    private AddressVO convertFromModel(AddressModel addressModel){
        if (addressModel == null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressModel,addressVO);
        return addressVO;
    }
}
