package com.mall.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 自定义用户日志注解
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 16:28
 */

//方法注解；运行时可见
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLogAnnotation {
    /**
     * 记录用户日志操作
     * @return String
     */
    String userWork();
}
