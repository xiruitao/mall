package com.mall.dao;

import com.mall.entity.Address;
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Param;
>>>>>>> little change
>>>>>>> little change

import java.util.List;

/**
 * @Description: 地址访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 09:59
 */
public interface AddressDao {
    /**
     * 通过用户ID查询收货地址
     * @param userId 用户ID
     * @return Address
     */
    List<Address> selectByUserId(Integer userId);

    /**
     * 通过地址ID查询收货地址
     * @param addressId 地址ID
     * @return Address
     */
    Address selectByAddressId(Integer addressId);

    /**
     * 添加收货地址
     * @param address 地址实体
     * @return int
     */
    int insertSelective(Address address);

    /**
     * 修改收货地址
     * @param address 地址实体
     * @return int
     */
    int updateByAddressId(Address address);

    /**
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
     * 将用户除给出ID外其他地址设置为非默认
     * @param userId 用户ID
     * @param addressId 地址ID
     * @return int
     */
    int updateDefaultAddress(@Param("userId")Integer userId,@Param("addressId")Integer addressId);

    /**
>>>>>>> little change
>>>>>>> little change
     * 删除收货地址
     * @param addressId 地址ID
     * @return int
     */
    int deleteByAddressId(Integer addressId);
}
