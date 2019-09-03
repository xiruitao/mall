package com.mall.validator;

import com.mall.controller.viewobject.UserVO;
import com.mall.error.BusinessException;
import com.mall.error.CommonError;
import com.mall.error.EmBusinessError;
import com.mall.service.model.AdminModel;
import com.mall.service.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 登录验证
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 14:03
 */
@Component
public class ValidateLogon {
    /**
     * 声明Logger
     */
    private static Logger logger = Logger.getLogger(ValidateLogon.class);

    /**
     * http请求对象
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 检验管理员是否登录
     * @return AdminModel
     * @throws BusinessException 业务异常
     */
    public AdminModel validateAdminLogon() throws BusinessException{
        //获取管理员登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("ADMIN_IS_LOGIN");
        if (isLogin == null){
            logger.info("管理员还未登录");
            throw new BusinessException(EmBusinessError.ADMIN_NOT_LOGIN);
        }
        AdminModel adminModel = (AdminModel) httpServletRequest.getSession().getAttribute("LOGIN_ADMIN");
        return adminModel;
    }

    /**
     * 校验用户是否登录
     * @return UserModel
     * @throws BusinessException 业务异常
     */
    public UserModel validateLogon() throws BusinessException {
        //获取用户登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (isLogin == null){
            logger.info("用户还未登录");
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserVO userVO = (UserVO)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        return convertFromUserVO(userVO);
    }

    /**
     * 将UserVO转化为UserModel
     * @param userVO 用户视图模型
     * @return UserModel
     */
    private UserModel convertFromUserVO(UserVO userVO){
        if (userVO==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userVO,userModel);
        return userModel;
    }
}
