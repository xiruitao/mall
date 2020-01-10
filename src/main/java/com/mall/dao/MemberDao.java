package com.mall.dao;

import com.mall.entity.Member;

/**
 * @Description: 会员访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 10:49
 */
public interface MemberDao {
    /**
     * 查询会员信息
     * @param userId 用户ID
     * @return Member
     */
    Member selectByUserId(Integer userId);

    /**
     * 插入对象（只插入非空字段）
     * @param member 会员对象
     * @return int
     */
    int insertSelective(Member member);

    /**
     * 更新会员信息
     * @param member 会员对象
     * @return int
     */
    int updateById(Member member);
}
