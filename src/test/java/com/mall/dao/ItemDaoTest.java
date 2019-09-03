package com.mall.dao;

import com.mall.entity.Item;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description: 商品访问对象测试
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/3 16:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ItemDaoTest {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ItemDao itemDao;

    @Test
    public void listItem() {
        List<Item> itemList = itemDao.listItem();
        for (Item item:itemList){
            System.out.println(item);
        }
    }

    @Test
    public void selectItemRows(){
        int itemRows = itemDao.selectItemRows();
        System.out.println("商品条数为："+itemRows);
    }
}