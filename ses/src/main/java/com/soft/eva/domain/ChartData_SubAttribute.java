package com.soft.eva.domain;

import java.util.Map;

public class ChartData_SubAttribute {
    String phase;
    Double shiHeXingVal;
    Double zhunQueXingVal;
    Double huCaoZuoXingVal;
    Double chengShuXingVal;
    Double rongCuoXingVal;
    Double anQuanWanShanXingVal;
    Double fangWeiXingVal;
    Double yiFenXiXingVal;
    Double yiCeShiXingVal;

    public ChartData_SubAttribute(String phase, Double shiHeXingVal, Double zhunQueXingVal, Double huCaoZuoXingVal, Double chengShuXingVal, Double rongCuoXingVal, Double anQuanWanShanXingVal, Double fangWeiXingVal, Double yiFenXiXingVal, Double yiCeShiXingVal) {
        this.phase = phase;
        this.shiHeXingVal = shiHeXingVal;
        this.zhunQueXingVal = zhunQueXingVal;
        this.huCaoZuoXingVal = huCaoZuoXingVal;
        this.chengShuXingVal = chengShuXingVal;
        this.rongCuoXingVal = rongCuoXingVal;
        this.anQuanWanShanXingVal = anQuanWanShanXingVal;
        this.fangWeiXingVal = fangWeiXingVal;
        this.yiFenXiXingVal = yiFenXiXingVal;
        this.yiCeShiXingVal = yiCeShiXingVal;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }

    public Double getShiHeXingVal() {
        return shiHeXingVal;
    }

    public void setShiHeXingVal(Double shiHeXingVal) {
        this.shiHeXingVal = shiHeXingVal;
    }

    public Double getZhunQueXingVal() {
        return zhunQueXingVal;
    }

    public void setZhunQueXingVal(Double zhunQueXingVal) {
        this.zhunQueXingVal = zhunQueXingVal;
    }

    public Double getHuCaoZuoXingVal() {
        return huCaoZuoXingVal;
    }

    public void setHuCaoZuoXingVal(Double huCaoZuoXingVal) {
        this.huCaoZuoXingVal = huCaoZuoXingVal;
    }

    public Double getChengShuXingVal() {
        return chengShuXingVal;
    }

    public void setChengShuXingVal(Double chengShuXingVal) {
        this.chengShuXingVal = chengShuXingVal;
    }

    public Double getRongCuoXingVal() {
        return rongCuoXingVal;
    }

    public void setRongCuoXingVal(Double rongCuoXingVal) {
        this.rongCuoXingVal = rongCuoXingVal;
    }

    public Double getAnQuanWanShanXingVal() {
        return anQuanWanShanXingVal;
    }

    public void setAnQuanWanShanXingVal(Double anQuanWanShanXingVal) {
        this.anQuanWanShanXingVal = anQuanWanShanXingVal;
    }

    public Double getFangWeiXingVal() {
        return fangWeiXingVal;
    }

    public void setFangWeiXingVal(Double fangWeiXingVal) {
        this.fangWeiXingVal = fangWeiXingVal;
    }

    public Double getYiFenXiXingVal() {
        return yiFenXiXingVal;
    }

    public void setYiFenXiXingVal(Double yiFenXiXingVal) {
        this.yiFenXiXingVal = yiFenXiXingVal;
    }

    public Double getYiCeShiXingVal() {
        return yiCeShiXingVal;
    }

    public void setYiCeShiXingVal(Double yiCeShiXingVal) {
        this.yiCeShiXingVal = yiCeShiXingVal;
    }

    public ChartData_SubAttribute(Map<String,Double> subAttributeKeXinDuMap){
        this.shiHeXingVal = subAttributeKeXinDuMap.get("适合性");
        this.zhunQueXingVal = subAttributeKeXinDuMap.get("准确性");
        this.huCaoZuoXingVal = subAttributeKeXinDuMap.get("互操作性");
        this.chengShuXingVal = subAttributeKeXinDuMap.get("成熟性");
        this.rongCuoXingVal = subAttributeKeXinDuMap.get("容错性");
        this.anQuanWanShanXingVal = subAttributeKeXinDuMap.get("安全完善性");
        this.fangWeiXingVal = subAttributeKeXinDuMap.get("防危性");
        this.yiFenXiXingVal = subAttributeKeXinDuMap.get("易分析性");
        this.yiCeShiXingVal = subAttributeKeXinDuMap.get("易测试性");
    }
}
