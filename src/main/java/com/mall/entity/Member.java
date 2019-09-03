package com.mall.entity;

/**
 * @Description: 用户会员实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 10:43
 */
public class Member {
    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 会员等级
     */
    private Integer memberLevel;

    /**
     * 用户ID
     */
    private Integer userId;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
