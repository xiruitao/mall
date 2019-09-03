package com.mall.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @Description: TODO
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 21:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class PointsDaoTest {

    @Autowired
    private PointsDao pointsDao;

    @Test
    public void selectUserPointsNumber() {
        if (pointsDao.selectUserPointsNumber(10) == null){
            System.out.println(0);
        }else {
            int points = pointsDao.selectUserPointsNumber(10);
            System.out.println(points);
        }
    }
}