package com.mall.entity;

import java.util.Date;

/**
 * @Description: 评论实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 09:40
 */
public class Comment {
    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 评论用户ID
     */
    private Integer userId;

    /**
     * 评论商品ID
     */
    private Integer itemId;

    /**
     * 评论内容
     */
    private String comments;

    /**
     * 评论创建时间
     */
    private Date createTime;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
