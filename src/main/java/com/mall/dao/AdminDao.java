package com.mall.dao;

import com.mall.entity.Admin;

/**
 * @Description: 管理员访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/12 13:42
 */
public interface AdminDao {
    /**
     * 插入管理员对象
     * @param admin 管理员实体对象
     * @return int
     */
    int insertSelective(Admin admin);

    /**
     * 通过邮箱查找管理员
     * @param adminMail 登录邮箱
     * @return Admin
     */
    Admin selectAdminByMail(String adminMail);
}
