package com.mall.dao;

import com.mall.entity.Reply;

import java.util.List;

/**
 * @Description: 评论访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/5 11:18
 */
public interface ReplyDao {
    /**
     * 通过回复用户Id查询回复
     * @param userId 用户Id
     * @return Reply
     */
    List<Reply> selectReplyByUserId(Integer userId);

    /**
     * 通过回复评论Id查询回复
     * @param commentId 回复评论Id
     * @return Reply
     */
    List<Reply> selectReplyByCommentId(Integer commentId);

    /**
     * 生成回复
     * @param reply 回复实体
     * @return int
     */
    int insertSelective(Reply reply);

    /**
     * 删除回复
     * @param replyId 回复ID
     * @return int
     */
    int deleteReply(Integer replyId);

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
    /**
     * 删除评论回复
     * @param commentId 评论ID
     * @return int
     */
    int deleteReplyOfComment(Integer commentId);
>>>>>>> little change
>>>>>>> little change

    /**
     * 查询用户评论的回复
     * @param commentIds 用户评论ID集合
     * @return List<Reply>
     */
    List<Reply> selectReplyForUser(List<Integer> commentIds);
}
