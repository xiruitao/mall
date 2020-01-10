package com.mall.aspect;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.GlobalLogService;
import com.mall.service.model.AdminModel;
import com.mall.service.model.GlobalLogModel;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;

import static com.mall.aspect.UserLogAspect.getIpAddress;

/**
 * @Description: 管理员日志切面
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/20 21:17
 */
@Component
@Aspect
public class AdminLogAspect {
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
     * Logger对象
     */
    private static final Logger logger = Logger.getLogger(UserLogAspect.class);

    /**
     * 环绕通知记录日志，通过注解匹配到需要增加日志功能的方法
     * @param point 环绕通知
     * @return Object
     * @throws Throwable 异常
     */
    @Around("@annotation(com.mall.annotation.AdminLogAnnotation)")
    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature)point.getSignature();
        // 获取方法
        Method method = methodSignature.getMethod();
        // 获取添加注解方法
        Method realMethod = point.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), method.getParameterTypes());
        // 获取方法上面的注解
        AdminLogAnnotation adminLogAnnotation = realMethod.getAnnotation(AdminLogAnnotation.class);
        // 获取管理员操作
        String adminWork = adminLogAnnotation.adminWork();
        // 创建日志对象
        GlobalLogModel globalLogModel = new GlobalLogModel();
        // 存入管理员操作
        globalLogModel.setUserWork(adminWork);

        Object result = null;
        try {
            // 使代理方法执行
            result = point.proceed();
            //存入操作结果
            globalLogModel.setResult("正常");
            //获取管理员登录信息
            Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("ADMIN_IS_LOGIN");
            if (isLogin == null){
                logger.info("管理员还未登录");
                throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
            }
            AdminModel adminModel = (AdminModel) httpServletRequest.getSession().getAttribute("LOGIN_ADMIN");
            // 存入管理员ID及管理员名
            globalLogModel.setUserId(adminModel.getAdminId());
            globalLogModel.setUserName(adminModel.getAdminName());
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
}
