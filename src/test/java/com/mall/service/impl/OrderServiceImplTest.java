package com.mall.service.impl;

import com.mall.service.OrderService;
import com.mall.service.model.OrderModel;
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
 * @Description: 订单功能测试
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/3 15:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:spring/spring-web.xml","classpath:spring/spring-service.xml",
        "classpath:spring/spring-dao.xml"})
public class OrderServiceImplTest {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;

    @Test
    public void listOrder(){
        List<OrderModel> orderList = orderService.listOrder();
        for (OrderModel orderModel:orderList){
            logger.info(orderModel);
        }
    }
}