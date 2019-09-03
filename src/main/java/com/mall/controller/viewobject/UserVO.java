package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Description: 用户视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 16:27
 */
public class UserVO {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户性别
     */
    private Byte userSex;

    /**
     * 用户手机号
     */
    private Long userPhone;

    /**
     * 积分数
     */
    private Integer pointsNumber;

    /**
     * 会员等级
     */
    private Integer memberLevel;

    /**
     * 用户状态（1：禁用 0：启用）
     */
    private Byte enable;

    /**
     * 用户状态名
     */
    private String enableName;

    /**
     * 用户创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Byte getUserSex() {
        return userSex;
    }

    public void setUserSex(Byte userSex) {
        this.userSex = userSex;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getPointsNumber() {
        return pointsNumber;
    }

    public void setPointsNumber(Integer pointsNumber) {
        this.pointsNumber = pointsNumber;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Byte getEnable() {
        return enable;
    }

    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    public String getEnableName() {
        return enableName;
    }

    public void setEnableName(String enableName) {
        this.enableName = enableName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
