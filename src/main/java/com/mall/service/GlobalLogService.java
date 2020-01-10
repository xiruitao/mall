package com.mall.service;

import com.mall.error.BusinessException;
import com.mall.service.model.GlobalLogModel;

import java.util.List;

/**
 * @Description: 日志功能接口
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 16:12
 */
public interface GlobalLogService {
    /**
     * 添加日志
     * @param globalLogModel 日志模型
     * @return boolean
     * @throws BusinessException 业务异常
     */
    boolean addLog(GlobalLogModel globalLogModel) throws BusinessException;

    /**
     * 分页展示日志记录
     * @param page 当前页码
     * @return List<GlobalLogModel>
     * @throws BusinessException 业务异常
     */
    List<GlobalLogModel> listLogPage(Integer page) throws BusinessException;

    /**
     * 日志记录分页数
     * @return int
     */
    int getLogPages();

    /**
     * 查询日志记录
     * @param field 查询字段
     * @return List<GlobalLogModel>
     */
    List<GlobalLogModel> searchLog(String field);
}
