package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.CommentModel;
import com.mall.service.model.ReplyModel;

import java.util.List;

/**
 * @Description: 评论功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 13:52
 */

public interface CommentService {
    /**
     * 通过评论ID获取评论
     * @param commentId 评论ID
     * @return CommentModel
     */
    CommentModel getCommentById(Integer commentId);

    /**
     * 通过用户ID查找评论
     * @param userId 用户ID
     * @return CommentModel
     */
    List<CommentModel> selectCommentByUserId(Integer userId);

    /**
     * 通过商品ID查找评论
     * @param itemId 商品ID
     * @return CommentModel
     */
    List<CommentModel> listCommentOfItem(Integer itemId);

    /**
     * 发送评论
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param comments 评论内容
     * @param orderId 订单ID
     * @throws BusinessException 业务异常
     */
    void sendComments(Integer userId,Integer itemId,String comments,String orderId) throws BusinessException;

    /**
     * 删除评论
     * @param commentId 评论ID
     */
    void deleteComment(Integer commentId);

    /**
     * 通过回复用户Id查询回复
     * @param userId 回复用户Id
     * @return ReplyModel
     */
    List<ReplyModel> selectReplyByUserId(Integer userId);

    /**
     * 通过回复评论Id查询回复
     * @param commentId 回复评论Id
     * @return ReplyModel
     */
    List<ReplyModel> selectReplyByCommentId(Integer commentId);

    /**
     * 发送回复
     * @param userId 用户ID
     * @param commentId 回复评论ID
     * @param replyContent 回复内容
     * @throws BusinessException 业务异常
     */
    void sendReply(Integer userId,Integer commentId,String replyContent)throws BusinessException;

    /**
     * 删除回复
     * @param replyId 回复ID
     */
    void deleteReply(Integer replyId);

    /**
     * 查询用户评论的回复
     * @param userId 用户ID
     * @return List<ReplyModel>
     */
    List<ReplyModel> getReplyForUser(Integer userId)throws BusinessException;
}
