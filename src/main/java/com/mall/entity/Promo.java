package com.mall.entity;

import java.util.Date;

/**
 * @Description: 促销活动实体对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 17:45
 */
public class Promo {
    /**
     * 促销活动ID
     */
    private Integer promoId;

    /**
     * 活动名
     */
    private String promoName;

    /**
     * 活动海报URL
     */
    private String promoImgUrl;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 活动创建时间
     */
    private Date createTime;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public String getPromoImgUrl() {
        return promoImgUrl;
    }

    public void setPromoImgUrl(String promoImgUrl) {
        this.promoImgUrl = promoImgUrl;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
