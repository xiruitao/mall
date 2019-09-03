package com.mall.service.model;

/**
 * @Description: 积分等级模型
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 16:32
 */
public class PointsMemberModel {
    /**
     * 积分等级ID
     */
    private Integer pointsMemberId;

    /**
     * 等级
     */
    private Integer memberLevel;

    /**
     * 对应最低积分数
     */
    private Integer pointsNumbers;

    public Integer getPointsMemberId() {
        return pointsMemberId;
    }

    public void setPointsMemberId(Integer pointsMemberId) {
        this.pointsMemberId = pointsMemberId;
    }

    public Integer getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(Integer memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getPointsNumbers() {
        return pointsNumbers;
    }

    public void setPointsNumbers(Integer pointsNumbers) {
        this.pointsNumbers = pointsNumbers;
    }
}
