package com.soft.eva.util;

import com.soft.eva.domain.SoftData;

import java.math.BigDecimal;
import java.util.*;

public class CountKeXinDu {
    /**
     * 计算度量元的可信度
     * 度量元的可信度和度量指标的可信度一样
     * 定量 = 1-x/y
     * 定性 = x/y
     */
    public Map<String, Double> countMetricElementKeXinDu(List<SoftData> softDataList) {
        Map<String, Double> resMap = new LinkedHashMap<>();
        for (int i = 0; i < softDataList.size(); i++) {
            double x = Double.valueOf(softDataList.get(i).getX_value());
            double y = Double.valueOf(softDataList.get(i).getY_value());
            String type = softDataList.get(i).getMetricType();
            double r;
            if (type.equals("定量")) {
                r = new Round().round(1 - x / y, 3);
            } else {
                r = x / y;
            }
            BigDecimal b   =   new   BigDecimal(r);
            double   value   =   b.setScale(3,   BigDecimal.ROUND_HALF_UP).doubleValue();
            value = value * 10;
            String curMetricElement = softDataList.get(i).getMetricElement();
            resMap.put(curMetricElement, value);
        }
        return resMap;
    }

    /**
     * 计算子属性可信度
     *
     * @param subAttributeMap
     * @param metricElementKeXinDuMap
     * @param metricElementWeightMap
     * @return
     */
    public Map<String, Double> countSubAttributeKeXinDu(Map<String, List<String>> subAttributeMap, Map<String, Double> metricElementKeXinDuMap, Map<String, Double> metricElementWeightMap) {
        Map<String, Double> resMap = new HashMap<>();

        double curValue = 0;
        for (Map.Entry<String, List<String>> entry : subAttributeMap.entrySet()) {
            String curSubAttribute = entry.getKey();
            List<String> curMetricElementList = entry.getValue();
            double curSubAttributeKeXinDu = 0;
            for (int i = 0; i < curMetricElementList.size(); i++) {
                String curMetricElement = curMetricElementList.get(i);
                double curKeXinDu = metricElementKeXinDuMap.get(curMetricElement);
                double curWeight = metricElementWeightMap.get(curMetricElement);
                curValue = curKeXinDu * curWeight;
                BigDecimal b   =   new   BigDecimal(curValue);
                double   value   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                curSubAttributeKeXinDu += value;
            }
            BigDecimal b   =   new   BigDecimal(curSubAttributeKeXinDu);
            double   curSubAttributeKeXinDuNew   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
            resMap.put(curSubAttribute, curSubAttributeKeXinDuNew);
        }
        return resMap;
    }


    public Map<String, Double> countParentKeXinDu(Map<String, List<String>> parentMap, Map<String, Double> childrenKeXinDuMap, Map<String, Double> childrenWeightMap) {
        Map<String, Double> resMap = new LinkedHashMap<>();
        double curParentKeXinDu;
        double curValue;
        for (Map.Entry<String, List<String>> entry : parentMap.entrySet()) {
            curParentKeXinDu = 1;
            String curParent = entry.getKey();
            List<String> curChildrenList = entry.getValue();
            for (int i = 0; i < curChildrenList.size(); i++) {
                String curChildren = curChildrenList.get(i);
                double curChildrenKeXinDu = childrenKeXinDuMap.get(curChildren);
                double curChildrenWeight = childrenWeightMap.get(curChildren);
                curValue = Math.pow(curChildrenKeXinDu, curChildrenWeight);

                BigDecimal b   =   new   BigDecimal(curValue);
                double   value   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                curParentKeXinDu = new Round().round(curParentKeXinDu*value, 2) ;
            }
            resMap.put(curParent, curParentKeXinDu);
        }
        return resMap;
    }


    /*public Map<String, Double> countParentKeXinDu(Map<String, List<String>> parentMap, Map<String, Double> childrenKeXinDuMap, Map<String, Double> childrenWeightMap) {
        Map<String, Double> resMap = new LinkedHashMap<>();
        double curParentKeXinDu;
        double curValue;
        for (Map.Entry<String, List<String>> entry : parentMap.entrySet()) {
            curParentKeXinDu = 1;
            String curParent = entry.getKey();
            List<String> curChildrenList = entry.getValue();
            for (int i = 0; i < curChildrenList.size(); i++) {
                String curChildren = curChildrenList.get(i);
                double curChildrenKeXinDu = childrenKeXinDuMap.get(curChildren);
                double curChildrenWeight = childrenWeightMap.get(curChildren);
                curValue = new Round().round(Math.pow(curChildrenKeXinDu, curChildrenWeight), 3);
                curParentKeXinDu = new Round().round(curParentKeXinDu*curValue, 3) ;
            }
            resMap.put(curParent, curParentKeXinDu);
        }
        return resMap;
    }*/
}
