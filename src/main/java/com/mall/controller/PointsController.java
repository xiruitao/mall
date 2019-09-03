package com.mall.controller;

import com.mall.annotation.AdminLogAnnotation;
import com.mall.controller.viewobject.PointsVO;
import com.mall.error.BusinessException;
import com.mall.response.CommonReturnType;
import com.mall.service.PointsService;
import com.mall.service.model.PointsMemberModel;
import com.mall.service.model.PointsModel;
import com.mall.service.model.UserModel;
import com.mall.validator.ValidateLogon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 积分业务实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/15 15:06
 */
@Controller("points")
@RequestMapping("/points")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class PointsController extends BaseController{
    /**
     * 积分功能接口
     */
    @Autowired
    private PointsService pointsService;

    /**
     * 登录校验工具类
     */
    @Autowired
    private ValidateLogon validateLogon;

    /**
     * 获取用户积分明细表
     * @return CommonReturnType
     */
    @RequestMapping(value = "/selectUserPoints",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType selectUserPoints()throws BusinessException{
        UserModel userModel = validateLogon.validateLogon();
        List<PointsModel> pointsModelList = pointsService.selectUserPoints(userModel.getUserId());
        return CommonReturnType.create(convertFromPointsModelList(pointsModelList));
    }

    /**
     * 积分等级规则表
     * @return CommonReturnType
     */
    @RequestMapping(value = "/listPointsMember",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listPointsMember(){
        List<PointsMemberModel> pointsMemberModelList = pointsService.listPointsMember();
        return CommonReturnType.create(pointsMemberModelList);
    }

    //管理员积分操作
    /**
     * 分页查询所有积分明细
     * @param page 页码
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/listPointsPages",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType listPointsPages(@RequestParam("page")Integer page)throws BusinessException{
        validateLogon.validateAdminLogon();
        List<PointsModel> pointsModelList = pointsService.listPointsPages(page);
        List<PointsVO> pointsVOList = convertFromPointsModelList(pointsModelList);
        return CommonReturnType.create(pointsVOList);
    }

    /**
     * 获取分页数
     * @return CommonReturnType
     */
    @RequestMapping(value = "/getPointsPages",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getPointsPages()throws BusinessException{
        validateLogon.validateAdminLogon();
        return CommonReturnType.create(pointsService.getPointsPages());
    }

    /**
     * 积分明细搜索（用户名）
     * @param field 搜索字段
     * @return CommonReturnType
     */
    @RequestMapping(value = "/pointsSearch",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType pointsSearch(@RequestParam("field")String field)throws BusinessException{
        validateLogon.validateAdminLogon();
        List<PointsModel> pointsModelList = pointsService.pointsSearch(field);
        return CommonReturnType.create(convertFromPointsModelList(pointsModelList));
    }

    /**
     * 创建新的积分等级规则
     * @param memberLevel 等级
     * @param pointsNumbers 所需积分
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员创建新的积分等级规则")
    @RequestMapping(value = "/createPointsMember",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createPointsMember(@RequestParam("memberLevel")Integer memberLevel,
                                               @RequestParam("pointsNumbers")Integer pointsNumbers)throws BusinessException{
        validateLogon.validateAdminLogon();
        PointsMemberModel pointsMemberModel = new PointsMemberModel();
        pointsMemberModel.setMemberLevel(memberLevel);
        pointsMemberModel.setPointsNumbers(pointsNumbers);
        pointsService.createPointsMember(pointsMemberModel);
        return CommonReturnType.create(null);
    }

    /**
     * 更新积分规则
     * @param pointsMemberId 积分等级ID
     * @param pointsNumbers 所需积分
     * @return CommonReturnType
     * @throws BusinessException 业务异常
     */
    @AdminLogAnnotation(adminWork = "管理员更新积分规则")
    @RequestMapping(value = "/updatePointsMember",method = RequestMethod.POST,consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updatePointsMember(@RequestParam("pointsMemberId")Integer pointsMemberId,
                                               @RequestParam("pointsNumbers")Integer pointsNumbers)throws BusinessException{
        validateLogon.validateAdminLogon();
        PointsMemberModel pointsMemberModel = pointsService.selectById(pointsMemberId);
        pointsMemberModel.setPointsNumbers(pointsNumbers);
        pointsService.updatePointsMember(pointsMemberModel);
        return CommonReturnType.create(null);
    }

    /**
     * PointsModel转化为PointsVO
     * @param pointsModel 积分模型
     * @return PointsVO
     */
    private PointsVO convertFromModel(PointsModel pointsModel){
        if (pointsModel == null){
            return null;
        }
        PointsVO pointsVO = new PointsVO();
        BeanUtils.copyProperties(pointsModel,pointsVO);
        return pointsVO;
    }

    /**
     * List<PointsModel>转化为List<PointsVO>
     * @param pointsModelList 积分模型集合
     * @return List<PointsModel>
     */
    private List<PointsVO> convertFromPointsModelList(List<PointsModel> pointsModelList){
        List<PointsVO> pointsVOList = pointsModelList.stream().map(pointsModel -> {
            PointsVO pointsVO = this.convertFromModel(pointsModel);
            return pointsVO;
        }).collect(Collectors.toList());
        return pointsVOList;
    }
}
