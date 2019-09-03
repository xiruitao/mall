package com.mall.service.impl;

import com.mall.dao.AddressDao;
import com.mall.entity.Address;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.AddressService;
import com.mall.service.model.AddressModel;
import com.mall.validator.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 地址功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 10:30
 */

@Service
public class AddressServiceImpl implements AddressService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(AddressServiceImpl.class);

    /**
     * 地址访问对象
     */
    @Autowired
    private AddressDao addressDao;

    /**
     * 校验对象
     */
    @Autowired
    private Validate validate;

    /**
     * 通过地址ID获得地址信息
     * @param addressId 地址ID
     * @return AddressModel
     */
    @Override
    public AddressModel getAddressById(Integer addressId) {
        Address address = addressDao.selectByAddressId(addressId);
        if (address == null){
            return null;
        }
        return convertFromDO(address);
    }

    /**
     * 获取一个用户的收货地址
     * @param userId 用户ID
     * @return AddressModel
     */
    @Override
    public List<AddressModel> getAddressByUserId(Integer userId) {
        List<Address> addressList = addressDao.selectByUserId(userId);
        List<AddressModel> addressModelList = addressList.stream().map(address -> {
            AddressModel addressModel = this.convertFromDO(address);
            return addressModel;
        }).collect(Collectors.toList());
        return addressModelList;
    }

    /**
     * 添加收货地址
     * @param addressModel 地址模型
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void addAddress(AddressModel addressModel) throws BusinessException {
        if (addressModel == null){
            logger.info("地址为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        validate.valid(addressModel);
        //若设置新建收货地址为默认地址，将其他地址设为非默认
        if (addressModel.getDefaultAddress() == 1){
            setDefaultAddress(addressModel.getAddressId(),addressModel.getUserId());
        }
        Address address = convertFromModel(addressModel);
        addressModel.setAddressId(address.getAddressId());
        addressDao.insertSelective(address);
    }

    /**
     * 修改收货地址
     * @param addressModel 地址模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void modifyAddress(AddressModel addressModel) throws BusinessException{
        if (addressModel == null){
            logger.info("地址为空");
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        validate.valid(addressModel);
        Address address = convertFromModel(addressModel);
        addressDao.updateByAddressId(address);
    }

    /**
     * 删除地址
     * @param addressId 地址ID
     */
    @Override
    public void deleteAddress(Integer addressId) throws BusinessException{
        int affectRow = addressDao.deleteByAddressId(addressId);
        if (affectRow != 1){
            logger.info("地址删除失败");
            throw new BusinessException(EmBusinessError.ADDRESS_DELETE_FAIL);
        }
    }

    /**
     * 设置默认地址
     * @param addressId 地址ID
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(Integer addressId,Integer userId) throws BusinessException {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> little change
        List<Address> addressList = addressDao.selectByUserId(userId);
        for (Address address : addressList){
            if (address.getAddressId().equals(addressId)){
                address.setDefaultAddress((byte) 1);
                addressDao.updateByAddressId(address);
            }else {
                address.setDefaultAddress((byte) 0);
                addressDao.updateByAddressId(address);
            }
        }
<<<<<<< HEAD
=======
=======
        //将该地址设置为默认地址
        Address address = addressDao.selectByAddressId(addressId);
        address.setDefaultAddress((byte) 1);
        addressDao.updateByAddressId(address);
        //将其他地址设置为非默认地址
        addressDao.updateDefaultAddress(userId,addressId);
>>>>>>> little change
>>>>>>> little change
    }

    /**
     * 将实体对象Address转化为模型对象AddressModel
     * @param address 地址实体
     * @return AddressModel
     */
    private AddressModel convertFromDO(Address address){
        if (address == null){
            return null;
        }
        AddressModel addressModel = new AddressModel();
        BeanUtils.copyProperties(address,addressModel);
        return addressModel;
    }

    /**
     * 将模型对象AddressModel转化为实体对象Address
     * @param addressModel 地址模型
     * @return Address
     */
    private Address convertFromModel(AddressModel addressModel){
        if (addressModel == null){
            return null;
        }
        Address address = new Address();
        BeanUtils.copyProperties(addressModel,address);
        return address;
    }
}
