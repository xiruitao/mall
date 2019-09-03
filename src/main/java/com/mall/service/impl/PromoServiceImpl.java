package com.mall.service.impl;

import com.mall.dao.ItemDao;
import com.mall.dao.PromoDao;
import com.mall.dao.PromoItemDao;
import com.mall.entity.Item;
import com.mall.entity.Promo;
import com.mall.entity.PromoItem;
import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import com.mall.service.ItemService;
import com.mall.service.PromoService;
import com.mall.service.model.PromoItemModel;
import com.mall.service.model.PromoModel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 促销活动实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/6 09:33
 */
@Service
public class PromoServiceImpl implements PromoService {
    /**
     * 声明Logger对象
     */
    private static Logger logger = Logger.getLogger(PromoServiceImpl.class);

    /**
     * 活动访问对象
     */
    @Autowired
    private PromoDao promoDao;

    /**
     * 活动商品访问对象
     */
    @Autowired
    private PromoItemDao promoItemDao;

    /**
     * 商品功能接口
     */
    @Autowired
    private ItemService itemService;

    /**
     * 商品访问对象
     */
    @Autowired
    private ItemDao itemDao;

    /**
     * 添加活动
     * @param promoModel 活动模型
     * @throws BusinessException 业务异常
     */
    @Override
    public PromoModel addPromo(PromoModel promoModel) throws BusinessException {
        if (promoModel == null){
            logger.info("活动为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        Promo promo = convertFromModel(promoModel);
        promoDao.insertSelective(promo);

        PromoModel newPromoModel = selectPromo(promo.getPromoId());
        return newPromoModel;
    }

    /**
     * 删除活动
     * @param promoId 活动ID
     */
    @Override
    public void deletePromo(Integer promoId) {
        promoDao.deletePromo(promoId);
    }

    /**
     * 修改活动信息
     * @param promoModelForTran 活动模型
     * @throws BusinessException 业务异常
     */
    @Override
    public PromoModel updatePromo(PromoModel promoModelForTran) throws BusinessException {
        if (promoModelForTran == null){
            logger.info("活动为空");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        PromoModel promoModel = selectPromo(promoModelForTran.getPromoId());
        promoModel.setPromoName(promoModelForTran.getPromoName());
        promoModel.setStartTime(promoModelForTran.getStartTime());
        promoModel.setEndTime(promoModelForTran.getEndTime());
        promoModel.setPromoImgUrl(promoModel.getPromoImgUrl());

        Promo promo = convertFromModel(promoModel);
        promoDao.updatePromo(promo);

        return promoModel;
    }

    /**
     * 活动列表查看
     * @return List<PromoModel>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PromoModel> listPromo() {
        List<Promo> promoList = promoDao.listPromo();
        List<PromoModel> promoModelList = promoList.stream().map(promo -> {
            PromoModel promoModel = this.convertFromDO(promo);
            int status = 3;
            if (promoModel.getPromoStatus() == status) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> little change
                List<PromoItemModel> promoItemModelList = listPromoItem(promoModel.getPromoId());
                for (PromoItemModel promoItemModel : promoItemModelList) {
                    deletePromoItem(promoItemModel.getPromoItemId());
                }
<<<<<<< HEAD
=======
=======
                promoItemDao.deletePromoItemAll(promoModel.getPromoId());
>>>>>>> little change
>>>>>>> little change
            }
            return promoModel;
        }).collect(Collectors.toList());
        return promoModelList;
    }

    /**
     * 查看活动详情
     * @param promoId 活动ID
     * @return PromoModel
     */
    @Override
    public PromoModel selectPromo(Integer promoId) {
        Promo promo = promoDao.selectPromo(promoId);
        PromoModel promoModel = convertFromDO(promo);
        return promoModel;
    }

    /**
     * 添加活动商品
     * @param promoItemId 活动商品ID
     * @param promoId 活动ID
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPromoItem(Integer promoItemId,Integer promoId) throws BusinessException {
        PromoItemModel promoItemModel = new PromoItemModel();
        promoItemModel.setPromoId(promoId);
        promoItemModel.setPromoItemId(promoItemId);

        //将加入活动的商品设为下架，状态设为2，即只有活动界面可以购买此商品
        Item item = itemDao.selectByItemId(promoItemId);
        item.setShelves((byte) 2);
        itemDao.updateItemByItemId(item);

        promoItemModel.setPromoItemPrice(item.getItemPrice());

        try {
            promoItemDao.insertSelective(convertFromItemModel(promoItemModel));
        }catch (DuplicateKeyException ex){
            logger.info("该商品已加入活动");
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该商品已加入活动");
        }
    }

    /**
     * 删除活动商品
     * @param promoItemId 活动商品ID
     */
    @Override
    public void deletePromoItem(Integer promoItemId) {
        //将加入活动的商品设为上架，商品界面可以购买此商品
        itemService.updateShelves(promoItemId);
        promoItemDao.deletePromoItem(promoItemId);
    }

    /**
     * 修改活动商品信息
     * @param promoItemId 活动商品ID
     * @param promoItemPrice 活动价格
     * @throws BusinessException 业务异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePromoItem(Integer promoItemId, BigDecimal promoItemPrice) throws BusinessException{
        PromoItemModel promoItemModel = getPromoItemById(promoItemId);
        promoItemModel.setPromoItemPrice(promoItemPrice);
        PromoItem promoItem = convertFromItemModel(promoItemModel);
        promoItemDao.updatePromoItem(promoItem);
    }

    /**
     * 活动商品列表查看
     * @param promoId 活动ID
     * @return List<PromoItemModel>
     */
    @Override
    public List<PromoItemModel> listPromoItem(Integer promoId) {
        List<PromoItem> promoItemList = promoItemDao.selectPromoItem(promoId);
        List<PromoItemModel> promoItemModelList = promoItemList.stream().map(promoItem -> {
            PromoItemModel promoItemModel = this.convertFromItem(promoItem);
            return promoItemModel;
        }).collect(Collectors.toList());
        return promoItemModelList;
    }

    /**
     * 通过活动商品ID查询活动商品
     * @param promoItemId 活动商品ID
     * @return PromoItemModel
     */
    @Override
    public PromoItemModel getPromoItemById(Integer promoItemId) {
        PromoItem promoItem = promoItemDao.selectPromoItemById(promoItemId);
        return convertFromItem(promoItem);
    }

    /**
     * 判断当前时间秒杀活动状态 1-未开始 2-进行中 3-已结束
     * @param promoModel 活动模型
     * @return int
     */
    private int promoStatus(PromoModel promoModel){
        Date nowTime = new Date();
        int status = 1, status2 = 2, status3 = 3;
        if (nowTime.getTime() < promoModel.getStartTime().getTime()){
            return status;
        }else if (nowTime.getTime() > promoModel.getEndTime().getTime()){
            return status3;
        }else {
            return status2;
        }
    }

    /**
     * promoModel -> Promo
     * @param promoModel 活动模型
     * @return Promo
     */
    private Promo convertFromModel(PromoModel promoModel){
        if (promoModel == null){
            return null;
        }
        Promo promo = new Promo();
        BeanUtils.copyProperties(promoModel,promo);

        return promo;
    }

    /**
     * Promo -> PromoModel
     * @param promo 活动实体
     * @return PromoModel
     */
    private PromoModel convertFromDO(Promo promo){
        if (promo == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promo,promoModel);
        promoModel.setPromoStatus(promoStatus(promoModel));
        return promoModel;
    }

    /**
     * PromoItemModel -> PromoItem
     * @param promoItemModel 活动商品模型
     * @return PromoItem
     */
    private PromoItem convertFromItemModel(PromoItemModel promoItemModel){
        if (promoItemModel == null){
            return null;
        }
        PromoItem promoItem = new PromoItem();
        BeanUtils.copyProperties(promoItemModel,promoItem);
        return promoItem;
    }

    /**
     * PromoItem -> PromoItemModel
     * @param promoItem 活动商品实体
     * @return PromoItemModel
     */
    private PromoItemModel convertFromItem(PromoItem promoItem){
        if (promoItem == null){
            return null;
        }
        PromoItemModel promoItemModel = new PromoItemModel();
        BeanUtils.copyProperties(promoItem,promoItemModel);
        return promoItemModel;
    }
}
