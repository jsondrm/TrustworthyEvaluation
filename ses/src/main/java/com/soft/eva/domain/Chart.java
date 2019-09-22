package com.soft.eva.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Chart {
    String[] attributeIndicator;
    String[] subAttributeIndicator;

    Map<String,Double> attributeKeXinDuMapInRequirePhase;
    Map<String,Double> attributeKeXinDuMapInDesignPhase;

    Map<String,Double> attributeKeXinDuMapInCodingPhase;

    Map<String,Double> attributeKeXinDuMapInTestPhase;

    public Chart(){}
    public Map<String, Double> getAttributeKeXinDuMapInRequirePhase() {
        return attributeKeXinDuMapInRequirePhase;
    }

    public void setAttributeKeXinDuMapInRequirePhase(Map<String, Double> attributeKeXinDuMapInRequirePhase) {
        this.attributeKeXinDuMapInRequirePhase = attributeKeXinDuMapInRequirePhase;
    }

    public Map<String, Double> getAttributeKeXinDuMapInDesignPhase() {
        return attributeKeXinDuMapInDesignPhase;
    }

    public void setAttributeKeXinDuMapInDesignPhase(Map<String, Double> attributeKeXinDuMapInDesignPhase) {
        this.attributeKeXinDuMapInDesignPhase = attributeKeXinDuMapInDesignPhase;
    }

    public Map<String, Double> getAttributeKeXinDuMapInCodingPhase() {
        return attributeKeXinDuMapInCodingPhase;
    }

    public void setAttributeKeXinDuMapInCodingPhase(Map<String, Double> attributeKeXinDuMapInCodingPhase) {
        this.attributeKeXinDuMapInCodingPhase = attributeKeXinDuMapInCodingPhase;
    }

    public Map<String, Double> getAttributeKeXinDuMapInTestPhase() {
        return attributeKeXinDuMapInTestPhase;
    }

    public void setAttributeKeXinDuMapInTestPhase(Map<String, Double> attributeKeXinDuMapInTestPhase) {
        this.attributeKeXinDuMapInTestPhase = attributeKeXinDuMapInTestPhase;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "attributeIndicator=" + Arrays.toString(attributeIndicator) +
                ", subAttributeIndicator=" + Arrays.toString(subAttributeIndicator) +
                ", attributeKeXinDuMapInRequirePhase=" + attributeKeXinDuMapInRequirePhase +
                ", attributeKeXinDuMapInDesignPhase=" + attributeKeXinDuMapInDesignPhase +
                ", attributeKeXinDuMapInCodingPhase=" + attributeKeXinDuMapInCodingPhase +
                ", attributeKeXinDuMapInTestPhase=" + attributeKeXinDuMapInTestPhase +
                '}';
    }

//    public Map<String, Double> getAttributeKeXinDuMapRequire() {
//        List<MeasurementData> measurementDataList = me
//        return attributeKeXinDuMapInDesignPhase;
//    }

}
