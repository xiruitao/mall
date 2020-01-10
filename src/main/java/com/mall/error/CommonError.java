package com.mall.error;

/**
 * @Description: 统一错误信息
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 14:20
 */
public interface CommonError {
    /**
     * 获取错误码
     * @return int 错误码
     */
    int getErrCode();

    /**
     * 获取错误信息
     * @return String 错误信息
     */
    String getErrMsg();

    /**
     * 设置错误信息
     * @param errMsg 错误信息
     * @return CommonError
     */
    CommonError setErrMsg(String errMsg);
}
