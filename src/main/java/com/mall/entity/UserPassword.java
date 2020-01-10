package com.mall.entity;

/**
 * @Description: 用户密码实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 09:23
 */
public class UserPassword {
    /**
     * 密码ID
     */
    private Integer passwordId;

    /**
     * 加密密码
     */
    private String encrptPassword;

    /**
     * 用户ID
     */
    private Integer userId;

    public Integer getPasswordId() {
        return passwordId;
    }

    public void setPasswordId(Integer passwordId) {
        this.passwordId = passwordId;
    }

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
