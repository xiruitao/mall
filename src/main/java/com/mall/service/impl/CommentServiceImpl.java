package com.mall.service.impl;

import com.mall.dao.CommentDao;
import com.mall.dao.ReplyDao;
import com.mall.entity.Comment;
import com.mall.entity.Reply;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.CommentService;
import com.mall.service.OrderService;
import com.mall.service.model.CommentModel;
import com.mall.service.model.OrderModel;
import com.mall.service.model.ReplyModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 评论功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 14:04
 */
@Service
public class CommentServiceImpl implements CommentService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(CommentServiceImpl.class);

    /**
     * 评论访问对象
     */
    @Autowired
    private CommentDao commentDao;

    /**
     * 回复访问对象
     */
    @Autowired
    private ReplyDao replyDao;

    /**
     * 订单功能接口
     */
    @Autowired
    private OrderService orderService;

    /**
     * 通过评论ID获取评论
     * @param commentId 评论ID
     * @return CommentModel
     */
    @Override
    public CommentModel getCommentById(Integer commentId) {
        Comment comment = commentDao.selectCommentById(commentId);
        return convertFromDO(comment);
    }

    /**
     * 通过用户ID查找评论
     * @param userId 用户ID
     * @return CommentModel
     */
    @Override
    public List<CommentModel> selectCommentByUserId(Integer userId) {
        List<Comment> commentList = commentDao.selectCommentByUserId(userId);
        List<CommentModel> commentModelList = commentList.stream().map(comment -> {
            CommentModel commentModel = this.convertFromDO(comment);
            return commentModel;
        }).collect(Collectors.toList());
        return commentModelList;
    }

    /**
     * 通过回复用户Id查询回复
     * @param userId 回复用户Id
     * @return ReplyModel
     */
    @Override
    public List<ReplyModel> selectReplyByUserId(Integer userId) {
        List<Reply> replyList = replyDao.selectReplyByUserId(userId);
        List<ReplyModel> replyModelList = replyList.stream().map(reply -> {
            ReplyModel replyModel = this.convertFromReply(reply);
            return replyModel;
        }).collect(Collectors.toList());
        return replyModelList;
    }

    /**
     * 通过回复评论Id查询回复
     * @param commentId 回复评论Id
     * @return ReplyModel
     */
    @Override
    public List<ReplyModel> selectReplyByCommentId(Integer commentId) {
        List<Reply> replyList = replyDao.selectReplyByCommentId(commentId);
        List<ReplyModel> replyModelList = replyList.stream().map(reply -> {
            ReplyModel replyModel = this.convertFromReply(reply);
            return replyModel;
        }).collect(Collectors.toList());
        return replyModelList;
    }

    /**
     * 展示商品的评论
     * @param itemId 商品ID
     * @return CommentModel
     */
    @Override
    public List<CommentModel> listCommentOfItem(Integer itemId) {
        List<Comment> commentList = commentDao.selectCommentByItemId(itemId);
        List<CommentModel> commentModelList = commentList.stream().map(comment -> {
            CommentModel commentModel = this.convertFromDO(comment);
            return commentModel;
        }).collect(Collectors.toList());
        return commentModelList;
    }

    /**
     * 发送评论
     * @param userId 用户ID
     * @param itemId 商品ID
     * @param comments 评论内容
     * @throws BusinessException 业务异常
     */
    @Override
    public void sendComments(Integer userId,Integer itemId,String comments,String orderId) throws BusinessException {
        if (comments == null){
            logger.info("评论内容不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"评论内容不能为空");
        }
        //检查用户是否完成购买此商品（订单状态为已完成）
        OrderModel orderModel = orderService.getOrder(orderId);
        int status = 2;
        if (orderModel.getOrderStatus() != status){
            logger.info("订单还未完成");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"订单还未完成，完成后才能评价哦");
        }

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setItemId(itemId);
        comment.setComments(comments);
        commentDao.insertSelective(comment);
    }

    /**
     * 删除评论
     * @param commentId 评论ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Integer commentId) {
        //删除评论回复
        replyDao.deleteReplyOfComment(commentId);
        //删除评论
        commentDao.deleteComment(commentId);
    }

    /**
     * 发送回复
     * @param userId 用户ID
     * @param commentId 回复评论ID
     * @param replyContent 回复内容
     * @throws BusinessException 业务异常
     */
    @Override
    public void sendReply(Integer userId,Integer commentId,String replyContent) throws BusinessException{
        if (replyContent == null){
            logger.info("回复内容不能为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"回复内容不能为空");
        }
        Reply reply = new Reply();
        reply.setUserId(userId);
        reply.setCommentId(commentId);
        reply.setReplyContent(replyContent);
        replyDao.insertSelective(reply);
    }

    /**
     * 删除回复
     * @param replyId 回复ID
     */
    @Override
    public void deleteReply(Integer replyId) {
        replyDao.deleteReply(replyId);
    }

    /**
     * 查询用户评论的回复
     * @param userId 用户ID
     * @return List<ReplyModel>
     */
    @Override
    public List<ReplyModel> getReplyForUser(Integer userId) throws BusinessException {
        List<CommentModel> commentModelList = selectCommentByUserId(userId);
        if (commentModelList == null || commentModelList.size() == 0){
            logger.info("用户还没有评论");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户还没有评论");
        }
        List<Integer> commentIds = new ArrayList<>();
        for(CommentModel commentModel : commentModelList){
            commentIds.add(commentModel.getCommentId());
        }
        List<Reply> replyList = replyDao.selectReplyForUser(commentIds);
        List<ReplyModel> replyModelList = replyList.stream().map(reply -> {
            ReplyModel replyModel = this.convertFromReply(reply);
            return replyModel;
        }).collect(Collectors.toList());
        return replyModelList;
    }

    /**
     * commentModel -> Comment
     * @param commentModel 评论模型
     * @return Comment
     */
    private Comment convertFromModel(CommentModel commentModel){
        if (commentModel == null){
            return null;
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentModel,comment);
        return comment;
    }

    /**
     * comment -> CommentModel
     * @param comment 评论实体
     * @return CommentModel
     */
    private CommentModel convertFromDO(Comment comment){
        if (comment == null){
            return null;
        }
        CommentModel commentModel = new CommentModel();
        BeanUtils.copyProperties(comment,commentModel);
        return commentModel;
    }

    /**
     * replyModel -> Reply
     * @param replyModel 回复模型
     * @return Reply
     */
    private Reply convertFromReplyModel(ReplyModel replyModel){
        if (replyModel == null){
            return null;
        }
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyModel,reply);
        return reply;
    }

    /**
     * Reply -> ReplyModel
     * @param reply 回复实体
     * @return ReplyModel
     */
    private ReplyModel convertFromReply(Reply reply){
        if (reply == null){
            return null;
        }
        ReplyModel replyModel = new ReplyModel();
        BeanUtils.copyProperties(reply,replyModel);
        return replyModel;
    }
}
