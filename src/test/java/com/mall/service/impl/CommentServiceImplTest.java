package com.mall.service.impl;

import com.mall.entity.Comment;
import com.mall.error.BusinessException;
import com.mall.service.CommentService;
import com.mall.service.model.ReplyModel;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description: TODO
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/18 10:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/spring-web.xml","classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml"})
public class CommentServiceImplTest {

    private final Logger logger = Logger.getLogger(CommentServiceImplTest.class);

    @Autowired
    private CommentService commentService;

    @Test
    public void getReplyForUser() throws BusinessException {
        List<ReplyModel> replyModelList = commentService.getReplyForUser(10);
        logger.info(replyModelList);
    }
}