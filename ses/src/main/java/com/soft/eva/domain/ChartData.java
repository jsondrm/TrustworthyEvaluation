package com.soft.eva.domain;

import java.util.Map;

public class ChartData {
    String phase;
    Double functionalityVal;
    Double reliabilityVal;
    Double safetyVal;
    Double maintainabilityVal;
    Double shiHeXingVal;
    Double zhunQueXingVal;
    Double huCaoZuoXingVal;
    Double chengShuXingVal;
    Double rongCuoXingVal;
    Double anQuanWanShanXingVal;
    Double fangWeiXingVal;
    Double yiFenXiXingVal;
    Double yiCeShiXingVal;

    public ChartData(String phase, Double functionalityVal, Double reliabilityVal, Double safetyVal, Double maintainabilityVal) {
        this.phase = phase;
        this.functionalityVal = functionalityVal;
        this.reliabilityVal = reliabilityVal;
        this.safetyVal = safetyVal;
        this.maintainabilityVal = maintainabilityVal;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Double getFunctionalityVal() {
        return functionalityVal;
    }

    public void setFunctionalityVal(Double functionalityVal) {
        this.functionalityVal = functionalityVal;
    }

    public Double getReliabilityVal() {
        return reliabilityVal;
    }

    public void setReliabilityVal(Double reliabilityVal) {
        this.reliabilityVal = reliabilityVal;
    }

    public Double getSafetyVal() {
        return safetyVal;
    }

    public void setSafetyVal(Double safetyVal) {
        this.safetyVal = safetyVal;
    }

    public Double getMaintainabilityVal() {
        return maintainabilityVal;
    }

    public void setMaintainabilityVal(Double maintainabilityVal) {
        this.maintainabilityVal = maintainabilityVal;
    }

    public ChartData(Map<String,Double> attributeKeXinDuMap){
        this.functionalityVal = attributeKeXinDuMap.get("功能性");
        this.reliabilityVal = attributeKeXinDuMap.get("可靠性");
        this.safetyVal = attributeKeXinDuMap.get("安全性");
        this.maintainabilityVal = attributeKeXinDuMap.get("可维护性");
    }
}
