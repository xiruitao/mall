package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Description: 日志异常视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/21 10:50
 */
public class GlobalLogVO {
    /**
     * 日志ID
     */
    private Integer logId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户操作
     */
    private String userWork;

    /**
     * 操作记录时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date recordTime;

    /**
     * 操作结果
     */
    private String result;

    /**
     * 标记
     */
    private String mark;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

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

    public String getUserWork() {
        return userWork;
    }

    public void setUserWork(String userWork) {
        this.userWork = userWork;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
