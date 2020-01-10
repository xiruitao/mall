package com.mall.dao;

import com.mall.entity.UserPassword;

/**
 * @Description: 用户密码访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 09:26
 */
public interface UserPasswordDao {
    /**
     * 查询密码（通过passwordId）
     * @param passwordId 密码ID
     * @return UserPassword
     */
    UserPassword selectByPasswordId(Integer passwordId);

    /**
     * 查询密码（通过userId）
     * @param userId 用户ID
     * @return UserPassword
     */
    UserPassword selectByUserId(Integer userId);

    /**
     * 插入对象（只插入非空对象）
     * @param userPassword 密码对象
     * @return int
     */
    int insertSelective(UserPassword userPassword);

    /**
     * 修改密码
     * @param userPassword 密码对象
     * @return int
     */
    int updateById(UserPassword userPassword);
}
