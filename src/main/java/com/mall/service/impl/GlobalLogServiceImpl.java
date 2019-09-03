package com.mall.service.impl;

import com.mall.dao.GlobalLogDao;
import com.mall.entity.GlobalLog;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.GlobalLogService;
import com.mall.service.model.GlobalLogModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 日志功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 16:16
 */

@Service
public class GlobalLogServiceImpl implements GlobalLogService {
    /**
     * 每页显示日志记录数
     */
    private final static int NUMBERS = 10;

    /**
     * Logger对象
     */
    private static final Logger logger = Logger.getLogger(GlobalLogServiceImpl.class);

    /**
     * 日志访问对象
     */
    @Autowired
    private GlobalLogDao globalLogDao;

    @Override
    public boolean addLog(GlobalLogModel globalLogModel) throws BusinessException {
        if (globalLogModel == null){
            logger.info("日志对象为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"日志对象为空");
        }
        GlobalLog globalLog = new GlobalLog();
        BeanUtils.copyProperties(globalLogModel, globalLog);
        int affectRow = globalLogDao.insertSelective(globalLog);
        return affectRow > 0;
    }

    /**
     * 分页查询日志记录
     * @param page 当前页码
     * @return List<GlobalLogModel>
     * @throws BusinessException 业务异常
     */
    @Override
    public List<GlobalLogModel> listLogPage(Integer page) throws BusinessException {
        //每页商品条数
        if (page > getLogPages() || page < 1){
            logger.info("页码错误");
            throw new BusinessException(EmBusinessError.PAGE_NUMBER_ERROR);
        }
        int offset = (page-1)*NUMBERS;
        List<GlobalLog> globalLogList = globalLogDao.listLog(offset,NUMBERS);
        List<GlobalLogModel> globalLogModelList = globalLogList.stream().map(globalLog -> {
            GlobalLogModel globalLogModel = new GlobalLogModel();
            BeanUtils.copyProperties(globalLog,globalLogModel);
            return globalLogModel;
        }).collect(Collectors.toList());
        return globalLogModelList;
    }

    /**
     * 分页数
     * @return int
     */
    @Override
    public int getLogPages() {
        int logRows = globalLogDao.selectLogRows();
        int pages = (int) Math.ceil((double)logRows/NUMBERS);
        return pages;
    }

    /**
     * 查询日志记录
     * @param field 查询字段
     * @return List<GlobalLogModel>
     */
    @Override
    public List<GlobalLogModel> searchLog(String field) {
        List<GlobalLog> globalLogList = globalLogDao.searchLog(field);
        List<GlobalLogModel> globalLogModelList = globalLogList.stream().map(globalLog -> {
            GlobalLogModel globalLogModel = new GlobalLogModel();
            BeanUtils.copyProperties(globalLog,globalLogModel);
            return globalLogModel;
        }).collect(Collectors.toList());
        return globalLogModelList;
    }
}
