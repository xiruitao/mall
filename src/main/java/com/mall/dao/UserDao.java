package com.mall.dao;

import com.mall.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用户访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 08:37
 */
public interface UserDao {
    /**
     * 查询用户（通过id)
     * @param userId 用户ID
     * @return User
     */
    User selectByUserId(Integer userId);

    /**
     * 查询用户（通过phone）
     * @param userPhone 用户手机号
     * @return User
     */
    User selectByUserPhone(Long userPhone);

    /**
     * 查看所有用户(后台）
     * @return List<User>
     */
    List<User> listUser();

    /**
     * 搜索用户
     * @param field 搜索字段
     * @return List<User>
     */
    List<User> userSearch(@Param("field")String field);

    /**
     * 查询用户数
     * @return int
     */
    int selectUserRows();

    /**
     * 用户分页查询
     * @param offset 开始下标
     * @param limit 条数
     * @return List<User>
     */
    List<User> listUserPage(@Param("offset")Integer offset,@Param("limit")Integer limit);

    /**
     * 插入对象（只插入非空字段）
     * @param user 用户对象
     * @return int
     */
    int insertSelective(User user);

    /**
     * 删除用户（通过id）
     * @param userId 用户ID
     * @return int
     */
    int deleteByUserId(Integer userId);

    /**
     * 修改用户信息
     * @param user 用户对象
     * @return int
     */
    int updateByUserId(User user);

    /**
     * 用户禁用启用
     * @param userId 用户ID
     * @param enable 禁用启用状态码（0：启用 1：禁用）
     * @return int
     */
    int updateEnable(@Param("userId")Integer userId,@Param("enable")Byte enable);
}
