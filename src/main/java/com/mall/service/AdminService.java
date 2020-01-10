package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.AdminModel;

/**
 * @Description: 管理员功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/12 13:58
 */
public interface AdminService {
    /**
     * 管理员注册
     * @param adminModel 管理员模型
     * @throws BusinessException 业务异常
     */
    void registerAdmin(AdminModel adminModel) throws BusinessException;

    /**
     * 管理员登录
     * @param adminMail 登录邮箱
     * @param adminPassword 登录密码
     * @throws BusinessException 业务异常
     * @return AdminModel
     */
    AdminModel loginAdmin(String adminMail,String adminPassword) throws BusinessException;
}
