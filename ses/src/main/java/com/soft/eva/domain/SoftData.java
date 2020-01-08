package com.soft.eva.domain;

import java.io.Serializable;

/**
 * 软件度量数据实体
 */
//@Document(collection = "softData")
public class SoftData implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 软件编号
     */
    private String softwareNumber;
    /**
     * 阶段
     */
    private String phase;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 子属性
     */
    private String subAttribute;
    /**
     * 度量元
     */
    private String metricElement;
    /**
     * 目标
     */
    private String target;
    /**
     * 度量指标名称
     */
    private String metricIndex;

    /**
     * 度量指标类型
     *
     */
    private String metricType;

    /**
     * 度量指标定义
     *
     */
    private String metricDefinition;
    /**
     * x和y的具体含义
     */
    private String x_y_spec;

    /**
     * 度量指标 x的值
     * 如果类型是定量的，用1-x/y 计算指标可信值
     * 如果类型是定性的，x:1-100；y:100，用x/y 计算指标可信值。
     * 最后计算结果 转换成 1-10 之间的数
     *
     */
    private String x_value;

    /**
     * 度量指标 y的值
     *
     */
    private String y_value;

    private double trustworthyValue;

    public double getTrustworthyValue() {
        return trustworthyValue;
    }

    public void setTrustworthyValue(double trustworthyValue) {
        this.trustworthyValue = trustworthyValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoftwareNumber() {
        return softwareNumber;
    }

    public void setSoftwareNumber(String softwareNumber) {
        this.softwareNumber = softwareNumber;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getSubAttribute() {
        return subAttribute;
    }

    public void setSubAttribute(String subAttribute) {
        this.subAttribute = subAttribute;
    }

    public String getMetricElement() {
        return metricElement;
    }

    public void setMetricElement(String metricElement) {
        this.metricElement = metricElement;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMetricIndex() {
        return metricIndex;
    }

    public void setMetricIndex(String metricIndex) {
        this.metricIndex = metricIndex;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public String getMetricDefinition() {
        return metricDefinition;
    }

    public void setMetricDefinition(String metricDefinition) {
        this.metricDefinition = metricDefinition;
    }

    public String getX_y_spec() {
        return x_y_spec;
    }

    public void setX_y_spec(String x_y_spec) {
        this.x_y_spec = x_y_spec;
    }

    public String getX_value() {
        return x_value;
    }

    public void setX_value(String x_value) {
        this.x_value = x_value;
    }

    public String getY_value() {
        return y_value;
    }

    public void setY_value(String y_value) {
        this.y_value = y_value;
    }

    @Override
    public String toString() {
        return "SoftData{" +
                "id='" + id + '\'' +
                ", softwareNumber='" + softwareNumber + '\'' +
                ", phase='" + phase + '\'' +
                ", attribute='" + attribute + '\'' +
                ", subAttribute='" + subAttribute + '\'' +
                ", metricElement='" + metricElement + '\'' +
                ", target='" + target + '\'' +
                ", metricIndex='" + metricIndex + '\'' +
                ", metricType='" + metricType + '\'' +
                ", metricDefinition='" + metricDefinition + '\'' +
                ", x_y_spec='" + x_y_spec + '\'' +
                ", x_value='" + x_value + '\'' +
                ", y_value='" + y_value + '\'' +
                ", trustworthyValue='" + trustworthyValue + '\'' +
                '}';
    }
}
