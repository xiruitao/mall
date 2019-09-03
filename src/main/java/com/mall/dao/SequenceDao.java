package com.mall.dao;

import com.mall.entity.Sequence;

/**
 * @Description: 序列访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 21:42
 */
public interface SequenceDao {
    /**
     * 通过序列名name获取sequence
     * @param name 序列名
     * @return Sequence
     */
    Sequence getSequenceByName(String name);

    /**
     * 更新Sequence表
     * @param sequence 序列实体
     * @return int
     */
    int updateByName(Sequence sequence);
}
