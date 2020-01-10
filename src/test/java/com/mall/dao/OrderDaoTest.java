package com.mall.dao;

import com.mall.entity.Order;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description: 订单访问对象测试
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/3 15:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class OrderDaoTest {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private OrderDao orderDao;

    @Test
    public void testSelectOrderAll() {
        List<Order> orderList = orderDao.selectOrderAll();
        for (Order order:orderList){
            System.out.println(order);
            System.out.println("----------------");
            logger.info(order);
        }
    }
}