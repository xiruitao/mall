package com.mall.controller;

import com.mall.controller.viewobject.GlobalLogVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.GlobalLogService;
import com.mall.service.model.GlobalLogModel;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/21 10:53
 */

@Controller("Log")
@RequestMapping("/log")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class GlobalLogController extends BaseController{
    /**
     * 声明Logger对象
     */
    private final static Logger logger = Logger.getLogger(GlobalLogController.class);

    /**
     * 日志功能接口
     */
    @Autowired
    private GlobalLogService globalLogService;

    /**
     * 登录验证工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 分页查询日志记录
     * @param page 页码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listLogPage",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listLogPage(@RequestParam("page")Integer page)throws BusinessException{
        validateLogon.validateAdminLogon();
        List<GlobalLogModel> globalLogModelList = globalLogService.listLogPage(page);
        List<GlobalLogVO> globalLogVOList = globalLogModelList.stream().map(globalLogModel -> {
            GlobalLogVO globalLogVO = new GlobalLogVO();
            BeanUtils.copyProperties(globalLogModel,globalLogVO);
            return globalLogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(globalLogVOList);
    }

    /**
     * 获取分页数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getLogPages",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getLogPages() throws BusinessException {
        validateLogon.validateAdminLogon();
        int logPages = globalLogService.getLogPages();
        return CommonReturnType.create(logPages);
    }

    /**
     * 日志记录查询
     * @param field 查询字段
     * @return CommonReturnType
     */
    @RequestMapping(value = "/searchLog",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType searchLog(@RequestParam("field")String field) throws BusinessException {
        validateLogon.validateAdminLogon();
        List<GlobalLogModel> globalLogModelList = globalLogService.searchLog(field);
        List<GlobalLogVO> globalLogVOList = globalLogModelList.stream().map(globalLogModel -> {
            GlobalLogVO globalLogVO = new GlobalLogVO();
            BeanUtils.copyProperties(globalLogModel,globalLogVO);
            return globalLogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(globalLogVOList);
    }
}
