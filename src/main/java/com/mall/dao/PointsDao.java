package com.mall.dao;

import com.mall.entity.Points;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用户积分访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 09:50
 */
public interface PointsDao {
    /**
     * 查询用户积分
     * @param userId 用户id
     * @return int
     */
    Integer selectUserPointsNumber(Integer userId);

    /**
     * 用户积分明细表
     * @param userId 用户ID
     * @return List<Points>
     */
    List<Points> selectPointsByUserId(Integer userId);

    /**
     * 通过订单ID查询积分明细
     * @param orderId 订单ID
     * @return Points
     */
    Points selectByOrderId(String orderId);

    /**
     * 分页查询积分明细
     * @param offset 开始下标
     * @param limit 条数
     * @return List<Points>
     */
    List<Points> listPointsPages(@Param("offset")Integer offset, @Param("limit")Integer limit);

    /**
     * 查询积分明细总条数
     * @return int
     */
    int selectPointsRows();

    /**
     * 积分明细搜索
     * @param field 搜索字段(用户名)
     * @return List<Points>
     */
    List<Points> pointsSearch(@Param("field")String field);

    /**
     * 插入对象（只插入非空字段）
     * @param points 积分对象
     * @return int
     */
    int insertSelective(Points points);

    /**
     * 更新积分
     * @param points 积分对象
     * @return int
     */
    int updateById(Points points);
}
