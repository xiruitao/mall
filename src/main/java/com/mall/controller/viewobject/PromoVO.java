package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 活动视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/16 10:58
 */
public class PromoVO {
    /**
     * 促销活动ID
     */
    private Integer promoId;

    /**
     * 促销活动状态 1-未开始 2-进行中 3-已结束
     */
    private Integer promoStatus;

    /**
     * 活动状态名
     */
    private String promoStatusName;

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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 活动创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public Integer getPromoStatus() {
        return promoStatus;
    }

    public void setPromoStatus(Integer promoStatus) {
        this.promoStatus = promoStatus;
    }

    public String getPromoStatusName() {
        return promoStatusName;
    }

    public void setPromoStatusName(String promoStatusName) {
        this.promoStatusName = promoStatusName;
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
