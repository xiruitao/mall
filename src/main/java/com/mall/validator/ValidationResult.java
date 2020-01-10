package com.mall.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 校验结果对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 13:51
 */
public class ValidationResult {
    /**
     * 检验校验结果是否出错
     */
    private boolean hasErrors = false;

    /**
     * 存放错误信息的map
     */
    private Map<String,String> errMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }

    /**
     * 通过格式化字符串信息获取错误结果
     * @return String
     */
    public String getErrMsg(){
        return StringUtils.join(errMsgMap.values().toArray(),",");
    }
}
