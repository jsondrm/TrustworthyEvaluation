package com.soft.eva.domain;

import java.io.Serializable;

public class OperateRecord implements Serializable {
    String id;//主键
    String operator;//操作人
    String operateTime;//操作时间
    String operateTarget;//操作对象
    String description;//情况说明

    public OperateRecord(String operator, String operateTime, String operateTarget, String description) {
        this.operator = operator;
        this.operateTime = operateTime;
        this.operateTarget = operateTarget;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateTarget() {
        return operateTarget;
    }

    public void setOperateTarget(String operateTarget) {
        this.operateTarget = operateTarget;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OperateRecord{" +
                "id='" + id + '\'' +
                ", operator='" + operator + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", operateTarget='" + operateTarget + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
