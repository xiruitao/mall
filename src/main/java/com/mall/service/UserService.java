package com.mall.service;

import com.mall.controller.viewobject.UserVO;
import com.mall.entity.User;
import com.mall.error.BusinessException;
import com.mall.service.model.UserModel;

import java.util.List;

/**
 * @Description: 用户功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 15:20
 */
public interface UserService {
    /**
     * 获取用户基础信息（通过用户ID）
     * @param userId 用户ID
     * @return UserModel
     */
    UserModel getUserById(Integer userId);

    /**
     * 获取用户基础信息（通过用户手机号）
     * @param userPhone 用户手机号
     * @return UserModel
     */
    UserModel getUserByPhone(Long userPhone);

    /**
     * 查看所有用户
     * @return List<UserModel>
     */
    List<UserModel> listUser();

    /**
     * 搜索用户
     * @param field 搜索字段
     * @return List<UserModel>
     */
    List<UserModel> userSearch(String field);

    /**
     * 查询用户数
     * @return int
     */
    int getUserPages();

    /**
     * 用户分页查询
     * @param page 当前页码
     * @return List<UserModel>
     * @throws BusinessException 业务异常
     */
    List<UserModel> listUserPage(Integer page) throws BusinessException;

    /**
     * 用户注册
     * @param userModel 用户模型
     * @throws BusinessException 业务异常
     */
    void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登录
     * @param userPhone 用户手机号
     * @param encrptPassword 用户密码
     * @return UserModel
     * @throws BusinessException 业务异常
     */
    UserModel validateLogin(Long userPhone,String encrptPassword) throws BusinessException;

    /**
     * 修改用户信息
     * @param userModel 用户模型
     * @throws BusinessException 业务异常
     */
    void modifyUser(UserModel userModel) throws BusinessException;

    /**
     * 登录中修改密码
     * @param userModel 用户模型
     * @throws BusinessException 业务异常
     */
    void modifyPassword(UserModel userModel)throws BusinessException;

    /**
     * 忘记密码修改密码
     * @param userPhone 用户手机号
     * @param newEncrptPassword 新加密后密码
     * @throws BusinessException 业务异常
     */
    void forgetPassword(Long userPhone,String newEncrptPassword) throws BusinessException;

    /**
     * 禁用或启用用户
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    int disOrEnableUser(Integer userId) throws BusinessException;
}
