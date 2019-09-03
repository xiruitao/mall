package com.mall.service.impl;

import com.mall.dao.MemberDao;
import com.mall.dao.PointsDao;
import com.mall.dao.PointsMemberDao;
import com.mall.entity.Member;
import com.mall.entity.Points;
import com.mall.entity.PointsMember;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.PointsService;
import com.mall.service.model.PointsMemberModel;
import com.mall.service.model.PointsModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 积分功能实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 14:32
 */
@Service
public class PointsServiceImpl implements PointsService {

    /**
     * 每页显示订单数
     */
    private static final int NUMBERS = 5;

    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(ItemServiceImpl.class);

    /**
     * 积分访问对象
     */
    @Autowired
    private PointsDao pointsDao;

    /**
     * 积分等级表访问对象
     */
    @Autowired
    private PointsMemberDao pointsMemberDao;

    /**
     * 会员等级访问对象
     */
    @Autowired
    private MemberDao memberDao;

    /**
     * 查询用户积分
     * @param userId 用户id
     * @return int
     */
    @Override
    public int getUserPoints(Integer userId) {
        if (pointsDao.selectUserPointsNumber(userId) == null){
            return 0;
        }else {
            return pointsDao.selectUserPointsNumber(userId);
        }
    }

    /**
     * 用户积分明细表
     * @param userId 用户ID
     * @return List<PointsModel>
     */
    @Override
    public List<PointsModel> selectUserPoints(Integer userId) {
        List<Points> pointsList = pointsDao.selectPointsByUserId(userId);
        return convertFromPointsList(pointsList);
    }

    /**
     * 通过订单ID查询积分明细
     * @param orderId 订单ID
     * @return PointsModel
     */
    @Override
    public PointsModel selectPointsByOrderId(String orderId) {
        Points points = pointsDao.selectByOrderId(orderId);
        return convertFromDO(points);
    }

    /**
     * 分页查询积分明细
     * @param page 页码
     * @return List<PointsModel>
     * @throws BusinessException 业务异常
     */
    @Override
    public List<PointsModel> listPointsPages(Integer page) throws BusinessException {
        if (page > getPointsPages() || page < 1){
            logger.info("页码错误");
            throw new BusinessException(EmBusinessError.PAGE_NUMBER_ERROR);
        }
        int offset = (page-1)*NUMBERS;
        List<Points> pointsList = pointsDao.listPointsPages(offset,NUMBERS);
        return convertFromPointsList(pointsList);
    }

    /**
     * 分页数
     * @return int
     */
    @Override
    public int getPointsPages() {
        int pointsRows = pointsDao.selectPointsRows();
        int pages = (int)Math.ceil((double)pointsRows/NUMBERS);
        return pages;
    }

    /**
     * 积分明细搜索
     * @param field 搜索字段(用户名)
     * @return List<PointsModel>
     */
    @Override
    public List<PointsModel> pointsSearch(String field) {
        List<Points> pointsList = pointsDao.pointsSearch(field);
        return convertFromPointsList(pointsList);
    }

    /**
     * 生成积分明细
     * @param pointsModel 积分模型
     */
    @Override
    public void generatePoints(PointsModel pointsModel) throws BusinessException{
        if (pointsModel == null){
            logger.info("积分为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        pointsDao.insertSelective(convertFromModel(pointsModel));
    }

    /**
     * 更新积分表
     * @param pointsModel 积分模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void updatePoints(PointsModel pointsModel) throws BusinessException{
        if (pointsModel == null){
            logger.info("积分为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        pointsDao.updateById(convertFromModel(pointsModel));
    }

    /**
     * 积分等级规则表
     * @return List<PointsMemberModel>
     */
    @Override
    public List<PointsMemberModel> listPointsMember() {
        List<PointsMember> pointsMemberList = pointsMemberDao.listPointsMember();
        List<PointsMemberModel> pointsMemberModelList = pointsMemberList.stream().map(pointsMember -> {
            PointsMemberModel pointsMemberModel = new PointsMemberModel();
            BeanUtils.copyProperties(pointsMember,pointsMemberModel);
            return pointsMemberModel;
        }).collect(Collectors.toList());
        return pointsMemberModelList;
    }

    /**
     * 创建新规则
     * @param pointsMemberModel 积分等级模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void createPointsMember(PointsMemberModel pointsMemberModel) throws BusinessException {
        if (pointsMemberModel == null){
            logger.info("积分等级为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"积分等级为空");
        }
        PointsMember pointsMember = new PointsMember();
        BeanUtils.copyProperties(pointsMemberModel,pointsMember);
        try{
            pointsMemberDao.insertSelective(pointsMember);
        }catch (DuplicateKeyException ex){
            //唯一索引异常
            logger.info("等级重复");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"等级重复");
        }
    }

    /**
     * 更新积分等级规则
     * @param pointsMemberModel 积分等级模型
     * @throws BusinessException 业务异常
     */
    @Override
    public void updatePointsMember(PointsMemberModel pointsMemberModel) throws BusinessException {
        if (pointsMemberModel == null){
            logger.info("积分等级为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"积分等级为空");
        }
        PointsMember pointsMember = new PointsMember();
        BeanUtils.copyProperties(pointsMemberModel,pointsMember);
        pointsMemberDao.updatePointsMember(pointsMember);
    }

    /**
     * 通过积分等级ID查询
     * @param pointsMemberId 积分等级ID
     * @return PointsMemberModel
     */
    @Override
    public PointsMemberModel selectById(Integer pointsMemberId) {
        PointsMember pointsMember = pointsMemberDao.selectById(pointsMemberId);
        PointsMemberModel pointsMemberModel = new PointsMemberModel();
        BeanUtils.copyProperties(pointsMember,pointsMemberModel);
        return pointsMemberModel;
    }

    /**
     * 获取用户会员等级
     * @param userId 用户ID
     * @return int
     */
    @Override
    public int getUserMemberLevel(Integer userId) {
        Member member = memberDao.selectByUserId(userId);
        return member.getMemberLevel();
    }

    /**
     * PointsModel转化为points
     * @param pointsModel 积分模型
     * @return Points
     */
    private Points convertFromModel(PointsModel pointsModel){
        if (pointsModel == null){
            return null;
        }
        Points points = new Points();
        BeanUtils.copyProperties(pointsModel,points);
        return points;
    }

    /**
     * points转化为pointsModel
     * @param points 积分实体
     * @return PointsModel
     */
    private PointsModel convertFromDO(Points points){
        if (points == null){
            return null;
        }
        PointsModel pointsModel = new PointsModel();
        BeanUtils.copyProperties(points,pointsModel);
        return pointsModel;
    }

    /**
     * List<Points>转化为List<PointsModel>
     * @param pointsList 积分实体集合
     * @return List<PointsModel>
     */
    private List<PointsModel> convertFromPointsList(List<Points> pointsList){
        List<PointsModel> pointsModelList = pointsList.stream().map(points -> {
            PointsModel pointsModel = this.convertFromDO(points);
            return pointsModel;
        }).collect(Collectors.toList());
        return pointsModelList;
    }
}
