package com.mall.entity;

import java.util.Date;

/**
 * @Description: 回复实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 09:42
 */
public class Reply {
    /**
     * 回复ID
     */
    private Integer replyId;

    /**
     * 回复用户ID
     */
    private Integer userId;

    /**
     *回复评论ID
     */
    private Integer commentId;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复创建时间
     */
    private Date createTime;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
