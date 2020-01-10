package com.mall.entity;

/**
 * @Description: 管理员实体对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/12 13:40
 */
public class Admin {
    /**
     * 管理员ID
     */
    private Integer adminId;

    /**
     * 管理员名字
     */
    private String adminName;

    /**
     * 登录邮箱
     */
    private String adminMail;

    /**
     * 登录密码
     */
    private String adminPassword;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminMail() {
        return adminMail;
    }

    public void setAdminMail(String adminMail) {
        this.adminMail = adminMail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
