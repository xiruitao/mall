package com.mall.controller;

import com.alibaba.druid.util.StringUtils;
import com.mall.annotation.AdminLogAnnotation;
import com.mall.annotation.UserLogAnnotation;
import com.mall.cache.OtpCache;
import com.mall.controller.viewobject.UserVO;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.response.CommonReturnType;
import com.mall.service.PointsService;
import com.mall.service.UserService;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import com.mall.validator.VerificationCode;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Description: 用户业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 16:30
 */

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(UserController.class);

    /**
     * 用户功能接口
     */
    @Autowired
    private UserService userService;

    /**
     * 积分功能接口
     */
    @Autowired
    private PointsService pointsService;

    /**
     * 验证码缓存
     */
    @Autowired
    private OtpCache otpCache;

    /**
     * http请求对象
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 校验登录工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 图片验证码
     */
    private String imgCode = null;

    /**
     * 用户登录接口
     * @param userPhone 用户手机号
     * @param password 密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 不支持编码
     * @throws NoSuchAlgorithmException 加密算法在该环境中不可用
     */
    @UserLogAnnotation(userWork = "用户登录")
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "userPhone")Long userPhone,
                                  @RequestParam(name = "password")String password,
                                  @RequestParam(name = "verificationCode")String verificationCode) throws BusinessException, IOException, NoSuchAlgorithmException {

        //入参校验，检验手机号与密码是否为空
        if (StringUtils.isEmpty(String.valueOf(userPhone))
                || StringUtils.isEmpty(password)) {
            logger.info("入参为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //检验验证码是否正确
        if (!this.imgCode.equals(verificationCode)){
            logger.info("验证码错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码错误");
        }

        //用户登录服务，用于检验用户登录是否合法
        UserModel userModel = userService.validateLogin(userPhone, this.encodeByMd5(password));

        //检验用户是否被禁用
        if (userModel.getEnable() == 1){
            logger.info("用户被禁用中");
            throw new BusinessException(EmBusinessError.USER_IS_DISABLED);
        }

        UserVO userVO = convertFromModel(userModel);

        //将登陆凭证加入到用户登录成功的session中
        HttpSession httpSession = this.httpServletRequest.getSession();

        httpSession.setAttribute("IS_LOGIN",true);
        httpSession.setAttribute("LOGIN_USER",userVO);

        return CommonReturnType.create(null);
    }

    /**
     * 生成验证码图片
     * @return CommonReturnType 图片地址
     * @throws IOException IO异常
     */
    @RequestMapping("/getImg")
    @ResponseBody
    public CommonReturnType getValidaImg() throws IOException {
        //生成验证码
        byte[] newImages = VerificationCode.getVerifyImg();
        imgCode = VerificationCode.imgCode.toString();
        String imgPath = "C:\\Users\\Administrator\\Documents\\MyDocuments\\testPages\\img\\imgCode.png";
        FileOutputStream fos = new FileOutputStream(imgPath);
        fos.write(newImages);
        fos.close();
        return CommonReturnType.create(imgPath);
    }

    /**
     * 用户注册接口
     * @param userName 用户名
     * @param userSex 用户性别
     * @param userPhone 用户手机号
     * @param otpCode 验证码
     * @param password 密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 不支持编码
     * @throws NoSuchAlgorithmException 加密算法在该环境中不可用
     */
    @UserLogAnnotation(userWork = "用户注册")
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "userName")String userName,
                                     @RequestParam(name = "userSex")Integer userSex,
                                     @RequestParam(name = "userPhone")Long userPhone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                      @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //从Redis获取动态验证码
        String inSessionOtpCode = otpCache.getOtpCode(String.valueOf(userPhone));
        if (inSessionOtpCode == null){
            logger.info("验证码过期，需重新获取验证码");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码过期，请重新获取验证码");
        }

        if (!StringUtils.equals(otpCode,inSessionOtpCode)){
            logger.info("验证码错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"验证码错误");
        }

        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setUserName(userName);
        userModel.setUserSex(new Byte(String.valueOf(userSex.intValue())));
        userModel.setUserPhone(userPhone);
        userModel.setEncrptPassword(this.encodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 用户获取otp短信接口
     * @param userPhone 用户手机号
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "userPhone")Long userPhone){
        //需要按一定的规则生成动态OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(90000) + 99999;
        String otpCode = String.valueOf(randomInt);

        //使用Redis存储动态验证码
        otpCache.putOtpCode(String.valueOf(userPhone),otpCode);

        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("userPhone = " + userPhone + " & otpCode = " + otpCode);

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

    /**
     * 注销登录
     * @return CommonReturnType
     */
    @UserLogAnnotation(userWork = "用户退出登录")
    @RequestMapping("/logout")
    @ResponseBody
    public CommonReturnType logout() throws BusinessException {
        validateLogon.validateLogon();
        httpServletRequest.getSession().removeAttribute("IS_LOGIN");
        httpServletRequest.getSession().invalidate();
        return CommonReturnType.create(null);
    }

    /**
     * 获取用户对象
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser() throws BusinessException {
        //校验登录
        UserModel userModel = validateLogon.validateLogon();
        UserVO userVO = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    /**
     * 修改用户信息
     * @param userName 用户名
     * @param userSex 用户性别
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @UserLogAnnotation(userWork = "用户修改个人信息")
    @RequestMapping(value = "/modify",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyUser(@RequestParam(name = "userName")String userName,
                                       @RequestParam(name = "userSex")Integer userSex) throws BusinessException {
        UserModel userModel = validateLogon.validateLogon();
        userModel.setUserName(userName);
        userModel.setUserSex(new Byte(String.valueOf(userSex.intValue())));

        userService.modifyUser(userModel);
        return CommonReturnType.create(null);
    }

    /**
     * 登录中修改密码
     * @param password 旧密码
     * @param newPassword 新密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 编码异常
     * @throws NoSuchAlgorithmException 加密算法异常
     */
    @UserLogAnnotation(userWork = "用户修改密码")
    @RequestMapping(value = "/modifyPasswordLogin",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modifyPasswordLogin(@RequestParam(name = "password")String password,
                                                @RequestParam(name = "newPassword")String newPassword) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        UserModel userModel = validateLogon.validateLogon();
        //检验旧密码是否正确
        if (userModel.getEncrptPassword() != this.encodeByMd5(password)){
            logger.info("旧密码错误,请重新输入");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"旧密码错误,请重新输入");
        }
        userModel.setEncrptPassword(this.encodeByMd5(newPassword));
        userService.modifyPassword(userModel);
        return CommonReturnType.create(convertFromModel(userModel));
    }

    /**
     * 忘记密码修改密码
     * @param userPhone 用户手机号
     * @param otpCode 验证码
     * @param newPassword 新密码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     * @throws UnsupportedEncodingException 编码异常
     * @throws NoSuchAlgorithmException 加密算法异常
     */
    @UserLogAnnotation(userWork = "用户重置密码")
    @RequestMapping(value = "/forgetPassword",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType forgetPassword(@RequestParam(name = "userPhone")Long userPhone,
                                              @RequestParam(name = "otpCode")String otpCode,
                                                @RequestParam(name = "newPassword")String newPassword) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号与对应的otpcode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(String.valueOf(userPhone));
        if (!StringUtils.equals(otpCode,inSessionOtpCode)){
            logger.info("验证码错误");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        userService.forgetPassword(userPhone,this.encodeByMd5(newPassword));

        return CommonReturnType.create(null);
    }


    //管理员对用户操作
    /**
     * 通过用户ID获取用户对象
     * @param userId 用户ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/adminGetUser")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam("userId")Integer userId) throws BusinessException {
        validateLogon.validateAdminLogon();
        UserModel userModel = userService.getUserById(userId);
        UserVO userVO = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    /**
     * 获取所有用户信息
     * @return CommonReturnType
     */
    @RequestMapping(value = "/adminListUser",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listUser() throws BusinessException {
        validateLogon.validateAdminLogon();
        List<UserModel> userModelList = userService.listUser();
        List<UserVO> userVOList = userModelList.stream().map(userModel -> {
            UserVO userVO = this.convertFromModel(userModel);
            return userVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(userVOList);
    }

    /**
     * 用户分页查询
     * @param page 页码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listUserPage",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listUserPage(@RequestParam("page")Integer page)throws BusinessException{
        validateLogon.validateAdminLogon();
        List<UserModel> userModelList = userService.listUserPage(page);
        List<UserVO> userVOList = userModelList.stream().map(userModel -> {
            UserVO userVO = this.convertFromModel(userModel);
            return userVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(userVOList);
    }

    /**
     * 获取用户分页数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getUserPages",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getUserPages() throws BusinessException {
        validateLogon.validateAdminLogon();
        int pages = userService.getUserPages();
        return CommonReturnType.create(pages);
    }

    /**
     * 用户搜索
     * @param field 搜索字段
     * @return CommonReturnType
     */
    @RequestMapping(value = "/userSearch",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType userSearch(@RequestParam("field")String field) throws BusinessException {
        validateLogon.validateAdminLogon();
        List<UserModel> userModelList = userService.userSearch(field);
        List<UserVO> userVOList = userModelList.stream().map(userModel -> {
            UserVO userVO =  this.convertFromModel(userModel);
            return userVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(userVOList);
    }

    /**
     * 禁用或启用用户
     * @param userId 用户ID
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员禁用或启用用户")
    @RequestMapping(value = "/disOrEnableUser",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType disOrEnableUser(@RequestParam(name = "userId")Integer userId) throws BusinessException {
        validateLogon.validateAdminLogon();
        int enable = userService.disOrEnableUser(userId);
        return CommonReturnType.create(enable);
    }

    private final static Map<Integer,String> ENABLE_NAME = new HashMap<Integer, String>(){{
        put(0,"已启用");
        put(1,"已禁用");
    }};

    /**
     * 将核心领域模型用户对象转化为可供UI使用的view object
     * @param userModel 用户模型
     * @return UserVO 用户视图模型
     */
    private UserVO convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        userVO.setPointsNumber(pointsService.getUserPoints(userModel.getUserId()));
        userVO.setMemberLevel(pointsService.getUserMemberLevel(userModel.getUserId()));
        userVO.setEnableName(ENABLE_NAME.get(new Integer(userVO.getEnable())));
        return userVO;
    }
}
