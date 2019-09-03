package com.mall.controller;

import com.alibaba.druid.util.StringUtils;
import com.mall.annotation.AdminLogAnnotation;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import com.mall.service.AdminService;
import com.mall.service.model.AdminModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: 管理员业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/12 14:18
 */
@Controller("admin")
@RequestMapping("/admin")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class AdminController extends BaseController{
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(UserController.class);

    /**
     * http请求对象
     */
    @Autowired
    private HttpServletRequest httpServletRequest   ;


    /**
     * 管理员功能接口
     */
    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     * @param adminMail 登录邮箱
     * @param adminPassword 登录密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 不支持编码
     * @throws NoSuchAlgorithmException 加密算法在该环境中不可用
     */
    @AdminLogAnnotation(adminWork = "管理员登录")
    @RequestMapping(value = "/adminLogin",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType adminLogin(@RequestParam(name = "adminMail")String adminMail,
                                       @RequestParam(name = "adminPassword")String adminPassword) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isEmpty(String.valueOf(adminMail))
                || StringUtils.isEmpty(adminPassword)) {
            logger.info("入参为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        AdminModel adminModel = adminService.loginAdmin(adminMail,this.encodeByMd5(adminPassword));

        //将登陆凭证加入到管理登录成功的session中
        HttpSession httpSession = this.httpServletRequest.getSession();
        httpSession.setAttribute("ADMIN_IS_LOGIN",true);
        httpSession.setAttribute("LOGIN_ADMIN",adminModel);
        return CommonReturnType.create(null);
    }

    /**
     * 管理员注册
     * @param adminName 管理员名字
     * @param adminMail 登录邮箱
     * @param adminPassword 登录密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 不支持编码
     * @throws NoSuchAlgorithmException 加密算法在该环境中不可用
     */
    @AdminLogAnnotation(adminWork = "管理员注册")
    @RequestMapping(value = "/registerAdmin",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType registerAdmin(@RequestParam(name = "adminName")String adminName,
                                          @RequestParam(name = "adminMail")String adminMail,
                                          @RequestParam(name = "adminPassword")String adminPassword) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        AdminModel adminModel = new AdminModel();
        adminModel.setAdminName(adminName);
        adminModel.setAdminMail(adminMail);
        adminModel.setAdminPassword(this.encodeByMd5(adminPassword));
        adminService.registerAdmin(adminModel);

        return CommonReturnType.create(null);
    }

    /**
     * 将密码加密
     * @param str 明文密码
     * @return String
     * @throws NoSuchAlgorithmException 加密算法在该环境中不可用
     * @throws UnsupportedEncodingException 不支持编码
     */
    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //返回加密字符串
        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));

    }
}
