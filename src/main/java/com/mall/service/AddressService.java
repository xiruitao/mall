package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.AddressModel;

import java.util.List;

/**
 * @Description: 地址功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 10:22
 */
public interface AddressService {
    /**
     * 通过地址ID获得地址信息
     * @param addressId 地址ID
     * @return AddressModel
     */
    AddressModel getAddressById(Integer addressId);

    /**
     * 获取一个用户的收货地址
     * @param userId 用户ID
     * @return AddressModel
     */
    List<AddressModel> getAddressByUserId(Integer userId);

    /**
     * 添加收货地址
     * @param addressModel 地址模型
     * @throws BusinessException 业务异常
     */
    void addAddress(AddressModel addressModel) throws BusinessException;

    /**
     * 修改收货地址
     * @param addressModel 地址模型
     * @throws BusinessException 业务异常
     * @return boolean
     */
    void modifyAddress(AddressModel addressModel) throws BusinessException;

    /**
     * 删除地址
     * @param addressId 地址ID
     * @throws BusinessException 业务异常
     */
    void deleteAddress(Integer addressId) throws BusinessException;

    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    void setDefaultAddress(Integer addressId,Integer userId)throws BusinessException;
}
