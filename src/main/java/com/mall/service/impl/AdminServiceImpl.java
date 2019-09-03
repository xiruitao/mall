package com.mall.service.impl;

import com.mall.dao.AdminDao;
import com.mall.entity.Admin;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.AdminService;
import com.mall.service.model.AdminModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 管理员功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/12 14:02
 */
@Service
public class AdminServiceImpl implements AdminService {
    /**
     * 管理员访问对象
     */
    @Autowired
    private AdminDao adminDao;

    private static Logger logger = Logger.getLogger(AdminService.class);

    /**
     * 管理员注册
     * @param adminModel 管理员模型
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAdmin(AdminModel adminModel) throws BusinessException {
        if (adminModel == null){
            logger.info("管理员为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        Admin admin = convertFromModel(adminModel);
        try {
            adminDao.insertSelective(admin);
        }catch (DuplicateKeyException ex){
            //唯一索引异常
            logger.info("邮箱已重复");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"邮箱已重复");
        }
    }

    /**
     * 管理员登录
     * @param adminMail 登录邮箱
     * @param adminPassword 登录密码
     * @return AdminModel
     * @throws BusinessException 业务异常
     */
    @Override
    public AdminModel loginAdmin(String adminMail, String adminPassword) throws BusinessException {
        Admin admin = adminDao.selectAdminByMail(adminMail);
        if (admin == null){
            logger.info("邮箱或密码不正确");
            throw new BusinessException(EmBusinessError.ADMIN_LOGIN_FAIL);
        }
        if (!StringUtils.equals(adminPassword,admin.getAdminPassword())){
            logger.info("邮箱或密码不正确");
            throw new BusinessException(EmBusinessError.ADMIN_LOGIN_FAIL);
        }
        return convertFromDO(admin);
    }

    /**
     * 将Admin转化为AdminModel
     * @param adminModel 管理员模型
     * @return Admin
     */
    private Admin convertFromModel(AdminModel adminModel){
        if (adminModel == null){
            return null;
        }
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminModel,admin);
        return admin;
    }

    /**
     * 将AdminModel转化为Admin
     * @param admin 管理员实体
     * @return AdminModel
     */
    private AdminModel convertFromDO(Admin admin){
        if (admin == null){
            return null;
        }
        AdminModel adminModel = new AdminModel();
        BeanUtils.copyProperties(admin,adminModel);
        return adminModel;
    }
}
