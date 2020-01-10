package com.mall.controller;

import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 基础控制类，被所有其他控制类继承
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 14:45
 */
public class BaseController {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(BaseController.class);

    /**
     * 指定处理请求的提交内容类型
     */
    static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    /**
     * 定义exception handler解决未被controller层吸收的exception
     * 为业务逻辑处理上的问题或业务逻辑错误而并非服务端不能处理的错误
     * @param ex 捕获的异常
     * @return CommonReturnType
     * ExceptionHandler 指明收到什么样的exception之后才会进入它的处理环节，此处定义为根类
     * ResponseStatus 捕获到controller抛出的exception，并返回HttpStatus.OK,即status=200
     * ResponseBody 处理viewobject类对应的@ResponseBody形式
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonReturnType handlerException(Exception ex){
        Map<String,Object> responseData = new HashMap<>(1600);
        if (ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        logger.info(ex);
        return CommonReturnType.create(responseData,"fail");
    }
}
