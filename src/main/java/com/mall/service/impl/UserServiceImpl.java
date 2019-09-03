package com.mall.service.impl;

import com.mall.dao.MemberDao;
import com.mall.dao.UserDao;
import com.mall.dao.UserPasswordDao;
import com.mall.entity.Member;
import com.mall.entity.User;
import com.mall.entity.UserPassword;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.UserService;
import com.mall.service.model.UserModel;
import com.mall.validator.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 用户功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 15:38
 */

@Service
public class UserServiceImpl implements UserService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(UserServiceImpl.class);

    /**
     * 每页显示用户数
     */
    private static final int NUMBERS = 5;

    /**
     * 用户访问对象
     */
    @Autowired
    private UserDao userDao;

    /**
     * 用户密码访问对象
     */
    @Autowired
    private UserPasswordDao userPasswordDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 校验实现
     */
    @Autowired
    private Validate validate;

    /**
     * 获取用户基础信息（通过用户ID）
     * @param userId 用户ID
     * @return UserModel
     */
    @Override
    public UserModel getUserById(Integer userId) {
        User user =userDao.selectByUserId(userId);
        if (user == null){
            return null;
        }
        UserPassword userPassword = userPasswordDao.selectByUserId(user.getUserId());
        return convertFromDataObject(user,userPassword);
    }

    /**
     * 获取用户基础信息（通过用户手机号）
     * @param userPhone 用户手机号
     * @return UserModel
     */
    @Override
    public UserModel getUserByPhone(Long userPhone) {
        User user = userDao.selectByUserPhone(userPhone);
        if (user == null){
            return null;
        }
        UserPassword userPassword = userPasswordDao.selectByUserId(user.getUserId());
        return convertFromDataObject(user,userPassword);
    }

    /**
     * 展示所有用户信息
     * @return List<UserVO>
     */
    @Override
    public List<UserModel> listUser() {
        List<User> userList = userDao.listUser();
        List<UserModel> userModelList = convertFromUserList(userList);
        return userModelList;
    }

    /**
     * List<User>转化为List<UserModel>
     * @param userList 用户集合
     * @return List<UserModel>
     */
    private List<UserModel> convertFromUserList(List<User> userList){
        List<UserModel> userModelList = userList.stream().map(user -> {
            UserPassword userPassword = userPasswordDao.selectByUserId(user.getUserId());
            UserModel userModel = this.convertFromDataObject(user,userPassword);
            return userModel;
        }).collect(Collectors.toList());
        return userModelList;
    }

    /**
     * 搜索用户
     * @param field 搜索字段
     * @return List<UserModel>
     */
    @Override
    public List<UserModel> userSearch(String field) {
        List<User> userList = userDao.userSearch(field);
        return convertFromUserList(userList);
    }

    /**
     * 查询用户数
     * @return int
     */
    @Override
    public int getUserPages() {
        //用户数
        int userRows = userDao.selectUserRows();
        int pages = (int) Math.ceil((double)userRows/NUMBERS);
        return pages;
    }

    /**
     * 用户分页查询
     * @param page 当前页码
     * @return List<UserModel>
     * @throws BusinessException 业务异常
     */
    @Override
    public List<UserModel> listUserPage(Integer page) throws BusinessException {
        if (page > getUserPages() || page < 1){
            logger.info("页码错误");
            throw new BusinessException(EmBusinessError.PAGE_NUMBER_ERROR);
        }
        int offset = (page-1)*NUMBERS;
        List<User> userList = userDao.listUserPage(offset,NUMBERS);
        List<UserModel> userModelList = convertFromUserList(userList);
        return userModelList;
    }

    /**
     * 用户注册
     * @param userModel 用户模型
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null){
            logger.info("用户不存在");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        validate.valid(userModel);

        User user = convertFromModel(userModel);
        try {
            userDao.insertSelective(user);
        }catch (DuplicateKeyException ex){
            //唯一索引异常
            logger.info("手机号已注册");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }
        userModel.setUserId(user.getUserId());

        UserPassword userPassword = convertPasswordFromModel(userModel);
        userPasswordDao.insertSelective(userPassword);

        //初始化会员等级表
        Member member = new Member();
        member.setUserId(user.getUserId());
        memberDao.insertSelective(member);
    }

    /**
     * 用户登录
     * @param userPhone 用户手机号
     * @param encrptPassword 用户密码
     * @return UserModel
     * @throws BusinessException 业务异常
     */
    @Override
    public UserModel validateLogin(Long userPhone, String encrptPassword) throws BusinessException {
        //通过用户手机获取用户信息
        User user = userDao.selectByUserPhone(userPhone);
        if (user == null){
            logger.info("用户不存在");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserPassword userPassword = userPasswordDao.selectByUserId(user.getUserId());
        UserModel userModel = convertFromDataObject(user,userPassword);

        //比对用户信息内加密的密码是否和传输进来的相匹配
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            logger.info("用户手机号或密码不正确");
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        return userModel;
    }

    /**
     * 修改用户信息
     * @param userModel 用户模型
     */
    @Override
    public void modifyUser(UserModel userModel) throws BusinessException {
        if (userModel == null){
            logger.info("用户为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        User user = convertFromModel(userModel);
        userDao.updateByUserId(user);
    }

    /**
     * 登录中修改密码
     * @param userModel 用户模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void modifyPassword(UserModel userModel)throws BusinessException{
        if (userModel == null){
            logger.info("用户为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        validate.valid(userModel);

        UserPassword userPassword = convertPasswordFromModel(userModel);

        int affectRow = userPasswordDao.updateById(userPassword);
        if (affectRow == 0){
            logger.info("密码更新失败");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"密码更新失败");
        }
    }

    /**
     * 忘记密码修改密码
     * @param userPhone 用户手机号
     * @param newEncrptPassword 新加密后密码
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forgetPassword(Long userPhone,String newEncrptPassword) throws BusinessException {
        UserModel userModel = getUserByPhone(userPhone);
        userModel.setEncrptPassword(newEncrptPassword);
        UserPassword userPassword = convertPasswordFromModel(userModel);
        int affectRow = userPasswordDao.updateById(userPassword);
        if (affectRow == 0){
            logger.info("密码更新失败");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"密码更新失败");
        }
    }

    /**
     * 禁用或启用用户
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disOrEnableUser(Integer userId) throws BusinessException {
        UserModel userModel = getUserById(userId);
        if (userModel.getEnable() == 1){
            userDao.updateEnable(userId, (byte) 0);
            return 0;
        }
        if (userModel.getEnable() == 0){
            userDao.updateEnable(userId, (byte) 1);
            return 1;
        }
        return -1;
    }

    /**
     * UserModel转换得到User
     * @param userModel 用户模型
     * @return User 用户对象
     */
    private User convertFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(userModel,user);

        return user;
    }

    /**
     * UserModel转换得到UserPassword
     * @param userModel 用户模型
     * @return UserPassword
     */
    private UserPassword convertPasswordFromModel(UserModel userModel){
        if (userModel==null){
            return null;
        }
        UserPassword userPassword = new UserPassword();
        userPassword.setEncrptPassword(userModel.getEncrptPassword());
        userPassword.setUserId(userModel.getUserId());
        return userPassword;
    }

    /**
     * User 与 UserPassword 转换为 UserModel
     * @param user 用户对象
     * @param userPassword 用户密码对象
     * @return UserModel 用户模型
     */
    private UserModel convertFromDataObject(User user, UserPassword userPassword){
        if (user==null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user,userModel);

        if (userPassword!=null){
            userModel.setEncrptPassword(userPassword.getEncrptPassword());
        }
        return userModel;
    }
}
