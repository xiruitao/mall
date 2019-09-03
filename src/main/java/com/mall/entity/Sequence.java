package com.mall.entity;

/**
 * @Description: 序列实体
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/2 21:40
 */
public class Sequence {
    /**
     * 序列名
     */
    private String name;

    /**
     * 当前值
     */
    private Integer currentValue;

    /**
     * 步长
     */
    private Integer step;

    /**
     * 初始值
     */
    private Integer initValue;

    /**
     * 最大值
     */
    private Integer maxValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getInitValue() {
        return initValue;
    }

    public void setInitValue(Integer initValue) {
        this.initValue = initValue;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }
}
