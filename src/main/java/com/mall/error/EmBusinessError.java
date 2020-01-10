package com.mall.error;

/**
 * @Description: 业务异常枚举
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 14:24
 */
public enum  EmBusinessError implements CommonError {
    //通用错误类型
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    //用户信息错误
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003,"用户还未登录"),
    USER_IS_DISABLED(2004,"用户被禁用中"),

    //地址信息错误
    ADDRESS_NOT_EXIST(30001,"地址不存在"),
    ADDRESS_DELETE_FAIL(30002,"地址删除失败"),

    //商品信息错误
    ITEM_NOT_EXIST(40001,"商品不存在"),
    ITEM_OFF_SHELVES(40002,"商品已下架"),

    //交易信息错误
    ITEM_NUMBER_NOT_ENOUGH(50001,"库存不足"),

    //管理员登录失败
    ADMIN_LOGIN_FAIL(60001,"邮箱或密码不正确"),
    ADMIN_NOT_LOGIN(60002,"管理员还未登录"),

    //分页
    PAGE_NUMBER_ERROR(70001,"页码错误"),
    ;

    /**
     * 构造函数
     * @param errCode 错误码
     * @param errMsg 错误信息
     */
    EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * 错误码
     */
    private int errCode;

    /**
     * 错误信息
     */
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
