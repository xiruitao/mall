package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Description: 回复视图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 15:13
 */
public class ReplyVO {
    /**
     * 回复ID
     */
    private Integer replyId;

    /**
     * 回复用户ID
     */
    private Integer userId;

    /**
     * 回复用户名
     */
    private String userName;

    /**
     *回复评论ID
     */
    private Integer commentId;

    /**
     * 回复评论内容
     */
    private String comments;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 回复创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
