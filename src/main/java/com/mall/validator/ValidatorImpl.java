package com.mall.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @Description: 校验实现
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 14:04
 */
@Component
public class ValidatorImpl implements InitializingBean {
    /**
     * 校验器接口
     */
    private Validator validator;

    /**
     * 校验实现
     * @param bean 校验对象
     * @return ValidationResult
     */
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        //若对应bean里边的一些参数的规则违背了对应Validation定义的Annotation(注释)，这个set中就会有值
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (constraintViolationSet.size() > 0){
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation ->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName,errMsg);
            } );
        }
        return result;
    }

    /**
     * 通过工厂校验的方式使hibernate validator实例化
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
