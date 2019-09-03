package com.mall.error;

/**
 * @Description: 业务异常构造
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 14:34
 */
public class BusinessException extends Exception implements CommonError {
    /**
     * 公共错误信息
     */
    private CommonError commonError;

    /**
     * 接受EmBusinessError的传参构造业务异常
     * @param commonError 错误
     */
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    /**
     * 接受自定义errMsg构造业务异常
     * @param commonError
     * @param errMsg
     */
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
