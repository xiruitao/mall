package com.mall.controller.viewobject;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 评论实图对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 15:07
 */
public class CommentVO {
    /**
     * 评论ID
     */
    private Integer commentId;

    /**
     * 评论用户ID
     */
    private Integer userId;

    /**
     * 评论用户名
     */
    private String userName;

    /**
     * 评论商品ID
     */
    private Integer itemId;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品图片URL
     */
    private String itemImageUrl;

    /**
     * 商品价格
     */
    private BigDecimal itemPrice;

    /**
     * 评论内容
     */
    private String comments;

    /**
     * 评论创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
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
