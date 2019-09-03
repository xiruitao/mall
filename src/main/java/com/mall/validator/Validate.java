package com.mall.validator;

import com.mall.error.BusinessException;
import com.mall.error.EmBusinessError;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 校验对象
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/31 11:10
 */
@Component
public class Validate {

    /**
     * 声明Logger
     */
    private static Logger logger = Logger.getLogger(Validate.class);

    /**
     * 校验实现类
     */
    @Autowired
    private ValidatorImpl validator;

    /**
     * 校验
     * @param object 校验对象
     * @throws BusinessException 业务异常
     */
    public void valid(Object object) throws BusinessException{
        ValidationResult result = validator.validate(object);
        if (result.isHasErrors()){
            logger.info(result.getErrMsg());
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
    }
}
