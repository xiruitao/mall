package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.PointsMemberModel;
import com.mall.service.model.PointsModel;

import java.util.List;

/**
 * @Description: 积分功能定义
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 14:01
 */
public interface PointsService {
    /**
     * 查询用户积分
     * @param userId 用户id
     * @return int
     */
    int getUserPoints(Integer userId);

    /**
     * 用户积分明细表
     * @param userId 用户ID
     * @return List<PointsModel>
     */
    List<PointsModel> selectUserPoints(Integer userId);

    /**
     * 通过订单ID查询积分明细
     * @param orderId 订单ID
     * @return PointsModel
     */
    PointsModel selectPointsByOrderId(String orderId);

    /**
     * 分页查询积分明细
     * @param page 页码
     * @return List<PointsModel>
     * @throws BusinessException 业务异常
     */
    List<PointsModel> listPointsPages(Integer page)throws BusinessException;

    /**
     * 获取积分分页总页数
     * @return int
     */
    int getPointsPages();

    /**
     * 积分明细搜索
     * @param field 搜索字段(用户名)
     * @return List<PointsModel>
     */
    List<PointsModel> pointsSearch(String field);

    /**
     * 生成积分明细
     * @param pointsModel 积分模型
     * @throws BusinessException 业务异常
     */
    void generatePoints(PointsModel pointsModel)throws BusinessException;

    /**
     * 更新积分表
     * @param pointsModel 积分模型
     * @throws BusinessException 业务异常
     */
    void updatePoints(PointsModel pointsModel)throws BusinessException;

    /**
     * 积分等级规则表
     * @return List<PointsMemberModel>
     */
    List<PointsMemberModel> listPointsMember();

    /**
     * 创建新规则
     * @param pointsMemberModel 积分等级模型
     * @throws BusinessException 业务异常
     */
    void createPointsMember(PointsMemberModel pointsMemberModel)throws BusinessException;

    /**
     * 更新积分等级规则
     * @param pointsMemberModel 积分等级模型
     * @throws BusinessException 业务异常
     */
    void updatePointsMember(PointsMemberModel pointsMemberModel)throws BusinessException;

    /**
     * 通过积分等级ID查询
     * @param pointsMemberId 积分等级ID
     * @return PointsMemberModel
     */
    PointsMemberModel selectById(Integer pointsMemberId);

    /**
     * 获取用户会员等级
     * @param userId 用户ID
     * @return int
     */
    int getUserMemberLevel(Integer userId);
}
