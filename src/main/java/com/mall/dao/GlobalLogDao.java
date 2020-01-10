package com.mall.dao;

import com.mall.entity.GlobalLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用户日志访问对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 16:00
 */
public interface GlobalLogDao {

    /**
     * 插入日志记录
     * @param globalLog 用户日志实体
     * @return int
     */
    int insertSelective(GlobalLog globalLog);

    /**
     * 查询一定量日志记录
     * @param offset 下标
     * @param limit 条数
     * @return List<GlobalLog>
     */
    List<GlobalLog> listLog(@Param("offset")Integer offset, @Param("limit")Integer limit);

    /**
     * 查询日志记录总条数
     * @return int
     */
    int selectLogRows();

    /**
     * 搜索日志记录
     * @param field 搜索字段
     * @return List<GlobalLog>
     */
    List<GlobalLog> searchLog(@Param("field")String field);
}
