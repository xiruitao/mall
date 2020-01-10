package com.mall.controller;

import com.mall.annotation.UserLogAnnotation;
import com.mall.controller.viewobject.CommentVO;
import com.mall.controller.viewobject.ReplyVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.CommentService;
import com.mall.service.ItemService;
import com.mall.service.UserService;
import com.mall.service.model.CommentModel;
import com.mall.service.model.ItemModel;
import com.mall.service.model.ReplyModel;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 评论业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 15:10
 */
@Controller
@RequestMapping("/comment")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class CommentController extends BaseController{
    /**
     * 评论功能接口
     */
    @Autowired
    private CommentService commentService;

    /**
     * 用户功能接口
     */
    @Autowired
    private UserService userService;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 登录校验工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 查询用户的评论
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/selectCommentByUserId",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType selectCommentByUserId()throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        List<CommentModel> commentModelList = commentService.selectCommentByUserId(userModel.getUserId());
        List<CommentVO> commentVOList = commentModelList.stream().map(commentModel -> {
            CommentVO commentVO = this.convertFromModel(commentModel);
            return commentVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(commentVOList);
    }

    /**
     * 查询用户的回复
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/selectReplyByUserId",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType selectReplyByUserId()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<ReplyModel> replyModelList = commentService.selectReplyByUserId(userModel.getUserId());
        List<ReplyVO> replyVOList = replyModelList.stream().map(replyModel -> {
            ReplyVO replyVO = this.convertFromReplyModel(replyModel);
            return replyVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(replyVOList);
    }

    /**
     * 查询评论的回复
     * @param commentId 回复评论ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/selectReplyByCommentId",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType selectReplyByCommentId(@RequestParam("commentId")Integer commentId){
        List<ReplyModel> replyModelList = commentService.selectReplyByCommentId(commentId);
        List<ReplyVO> replyVOList = replyModelList.stream().map(replyModel -> {
            ReplyVO replyVO = this.convertFromReplyModel(replyModel);
            return replyVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(replyVOList);
    }

    /**
     * 展示商品的评论
     * @param itemId 商品ID
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listCommentOfItem",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listCommentOfItem(@RequestParam("itemId")Integer itemId){
        List<CommentModel> commentModelList = commentService.listCommentOfItem(itemId);
        List<CommentVO> commentVOList = commentModelList.stream().map(commentModel -> {
            CommentVO commentVO = this.convertFromModel(commentModel);
            return commentVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(commentVOList);
    }

    /**
     * 获取用户评论的回复
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getReplyForUser",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getReplyForUser() throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        List<ReplyModel> replyModelList = commentService.getReplyForUser(userModel.getUserId());
        List<ReplyVO> replyVOList = replyModelList.stream().map(replyModel -> {
            ReplyVO replyVO = this.convertFromReplyModel(replyModel);
            return replyVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(replyVOList);
    }

    /**
     * 发送评论
     * @param itemId 商品ID
     * @param comments 评论内容
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户发送评论")
    @RequestMapping(value = "/sendComments",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType sendComments(@RequestParam("itemId")Integer itemId,
                                         @RequestParam("comments")String comments,
                                         @RequestParam("orderId")String orderId) throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        commentService.sendComments(userModel.getUserId(),itemId,comments,orderId);
        return CommonReturnType.create(null);
    }

    /**
     * 发送回复
     * @param commentId 回复评论ID
     * @param replyContent 回复内容
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户发送回复")
    @RequestMapping(value = "/sendReply",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType sendReply(@RequestParam("commentId")Integer commentId,@RequestParam("replyContent")String replyContent) throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        commentService.sendReply(userModel.getUserId(),commentId,replyContent);
        return CommonReturnType.create(null);
    }

    /**
     * 删除评论
     * @param commentId 评论ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户删除评论")
    @RequestMapping(value = "/deleteComment",method = RequestMethod.POST,consumes = CONTENT_TYPE_FORMED)
    @ResponseBody
    public CommonReturnType deleteComment(@RequestParam("commentId")Integer commentId) throws BusinessException {
        validateLogon.validateLogon();
        commentService.deleteComment(commentId);
        return CommonReturnType.create(null);
    }

    /**
     * 删除回复
     * @param replyId 回复ID
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户删除回复")
    @RequestMapping(value = "/deleteReply",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType deleteReply(@RequestParam("replyId")Integer replyId) throws BusinessException {
        validateLogon.validateLogon();
        commentService.deleteReply(replyId);
        return CommonReturnType.create(null);
    }

    /**
     * CommentModel -> CommentVO
     * @param commentModel 评论模型
     * @return CommentVO
     */
    private CommentVO convertFromModel(CommentModel commentModel){
        if (commentModel == null){
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(commentModel,commentVO);
        UserModel userModel = userService.getUserById(commentVO.getUserId());
        ItemModel itemModel = itemService.getItemByItemId(commentVO.getItemId());
        commentVO.setUserName(userModel.getUserName());
        commentVO.setItemName(itemModel.getItemName());
        commentVO.setItemImageUrl(itemModel.getItemImageUrl());
        commentVO.setItemPrice(itemModel.getItemPrice());
        return commentVO;
    }

    /**
     * ReplyModel -> ReplyVO
     * @param replyModel 回复模型
     * @return ReplyVO
     */
    private ReplyVO convertFromReplyModel(ReplyModel replyModel){
        if (replyModel == null){
            return null;
        }
        ReplyVO replyVO = new ReplyVO();
        BeanUtils.copyProperties(replyModel,replyVO);
        UserModel userModel = userService.getUserById(replyVO.getUserId());
        CommentModel commentModel = commentService.getCommentById(replyVO.getCommentId());
        replyVO.setUserName(userModel.getUserName());
        replyVO.setComments(commentModel.getComments());
        return replyVO;
    }
}
