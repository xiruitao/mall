package com.mall.dao;

import com.mall.entity.Comment;

import java.util.List;

/**
 * @Description: 评论访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 09:46
 */
public interface CommentDao {
    /**
     * 通过评论ID查询评论
     * @param commentId 评论ID
     * @return Comment
     */
    Comment selectCommentById(Integer commentId);

    /**
     * 通过用户ID查找评论
     * @param userId 用户ID
     * @return Comment
     */
    List<Comment> selectCommentByUserId(Integer userId);

    /**
     * 通过商品ID查找评论
     * @param itemId 商品ID
     * @return Comment
     */
    List<Comment> selectCommentByItemId(Integer itemId);

    /**
     * 生成评论
     * @param comment 评论实体
     * @return int
     */
    int insertSelective(Comment comment);

    /**
     * 通过评论ID删除评论
     * @param commentId 评论ID
     * @return int
     */
    int deleteComment(Integer commentId);
}
