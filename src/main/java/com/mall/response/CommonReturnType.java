package com.mall.response;

/**
 * @Description: 统一返回对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 13:30
 */

public class CommonReturnType {
    /**
     * 对应请求返回结果 "success" or "fail"
     */
    private String status;

    /**
     * success -> data json数据；fail ->data 通用错误码
     */
    private Object data;

    /**
     * 返回正确数据
     * @param result 正确数据
     * @return CommonReturnType
     */
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    /**
     * 返回错误信息
     * @param result 错误信息
     * @param status 状态
     * @return CommonReturnType
     */
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type =new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
