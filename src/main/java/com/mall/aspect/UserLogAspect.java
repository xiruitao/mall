package com.mall.aspect;

import com.mall.annotation.UserLogAnnotation;
import com.mall.controller.viewobject.UserVO;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.GlobalLogService;
import com.mall.service.model.GlobalLogModel;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Date;

/**
 * @Description: 用户日志切面
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 16:39
 */

@Component
@Aspect
public class UserLogAspect {
    /**
     * 日志功能接口
     */
    @Autowired
    private GlobalLogService globalLogService;

    /**
     * http请求对象
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 登录验证工具
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * Logger对象
     */
    private static final Logger logger = Logger.getLogger(UserLogAspect.class);

    /**
     * 环绕通知记录日志，通过注解匹配到需要增加日志功能的方法
     * @param point 环绕通知
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(com.mall.annotation.UserLogAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取添加注解方法
        Method realMethod = point.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), method.getParameterTypes());
        // 获取方法上面的注解
        UserLogAnnotation userLogAnnotation = realMethod.getAnnotation(UserLogAnnotation.class);
        // 获取用户操作
        String userWork = userLogAnnotation.userWork();
        // 创建日志对象
        GlobalLogModel globalLogModel = new GlobalLogModel();
        // 存入用户操作
        globalLogModel.setUserWork(userWork);

        String work2 = "用户退出登录";
        if (work2.equals(userWork)){
            // 获取session中存储的用户对象
            UserModel userModel = validateLogon.validateLogon();
            // 存入用户ID及用户名
            globalLogModel.setUserId(userModel.getUserId());
            globalLogModel.setUserName(userModel.getUserName());
        }

        Object result = null;
        try {
            // 使代理方法执行
            result = point.proceed();
            if (!work2.equals(userWork)) {
                String work = "用户注册";
                if (work.equals(userWork)) {
                    globalLogModel.setUserId(0);
                    globalLogModel.setUserName("new");
                } else {
                    // 获取session中存储的用户对象
                    UserModel userModel = validateLogon.validateLogon();
                    // 存入用户ID及用户名
                    globalLogModel.setUserId(userModel.getUserId());
                    globalLogModel.setUserName(userModel.getUserName());
                }
            }

            //存入操作结果
            globalLogModel.setResult("正常");
        }catch (SQLException e){
            globalLogModel.setResult("失败");
        }finally {
            // 存入记录时间及标记IP
            globalLogModel.setRecordTime(new Date());
            globalLogModel.setMark(getIpAddress(httpServletRequest));

            //业务异常（被捕获）
            if (globalLogModel.getResult() == null || globalLogModel.getResult() == ""){
                globalLogModel.setResult("失败");
            }
            //添加日志记录
            globalLogService.addLog(globalLogModel);
        }
        return result;
    }

    /**
     * 获取IP地址
     * @param request http请求对象
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
