package com.mall.dao;

import com.mall.entity.PointsMember;

import java.util.List;

/**
 * @Description: 积分等级访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 16:05
 */
public interface PointsMemberDao {
    /**
     * 积分等级规则表
     * @return List<PointsMember>
     */
    List<PointsMember> listPointsMember();

    /**
     * 通过积分等级ID查询积分等级
     * @param pointsMemberId 积分等级ID
     * @return PointsMember
     */
    PointsMember selectById(Integer pointsMemberId);

    /**
     * 创建新规则
     * @param pointsMember 积分等级
     * @return int
     */
    int insertSelective(PointsMember pointsMember);

    /**
     * 更新积分等级表
     * @param pointsMember 积分等级
     * @return int
     */
    int updatePointsMember(PointsMember pointsMember);
}
