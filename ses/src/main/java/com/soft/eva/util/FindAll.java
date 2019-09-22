package com.soft.eva.util;

import com.soft.eva.domain.SoftData;

import java.util.*;

/**
 * 查找
 * 子属性的度量元
 * 属性的子属性
 * 阶段的属性
 */
public class FindAll {
    /**
     * 查找所有子属性
     * @param softDataList
     * @return
     */
    public List<String> findAllSubAttribute(List<SoftData> softDataList){
        List<String> subAttributeList = new ArrayList<>();
        for(int i = 0; i < softDataList.size(); i++){
            String curSubAttribute = softDataList.get(i).getSubAttribute();
            if(!subAttributeList.contains(curSubAttribute)){
                subAttributeList.add(curSubAttribute);
            }
        }
        return subAttributeList  ;
    }

    /**
     * 查找每个子属性对应的度量元
     * @param softDataList
     * @return
     */
    public Map<String, List<String>> findAllMetricElementToEachSubAttribute(List<SoftData> softDataList){
        List<String> subAttributeList = findAllSubAttribute(softDataList);
        Map<String,List<String>> resMap = new LinkedHashMap<>();
        for(int i = 0; i < subAttributeList.size(); i++){
            String curSubAttribute = subAttributeList.get(i);
            List<String> metricElementList = new ArrayList<>();
            for(int j = 0; j < softDataList.size(); j++){
                if(softDataList.get(j).getSubAttribute().equals(curSubAttribute) && !metricElementList.contains(softDataList.get(j).getMetricElement())){
                    metricElementList.add(softDataList.get(j).getMetricElement());
                }
            }
            resMap.put(curSubAttribute, metricElementList);
        }
        return resMap;
    }

    /**
     * 查找所有属性
     * @param softDataList
     * @return
     */
    public List<String> findAllAttribute(List<SoftData> softDataList){
        List<String> attributeList = new ArrayList<>();
        for(int i = 0; i < softDataList.size(); i++){
            String curAttribute = softDataList.get(i).getAttribute();
            if(!attributeList.contains(curAttribute)){
                attributeList.add(curAttribute);
            }
        }
        return attributeList  ;
    }

    /**
     * 查找每个属性对应的子属性
     * @param softDataList
     * @return
     */
    public Map<String, List<String>> findAllSubAttributeToEachAttribute(List<SoftData> softDataList){
        List<String> attributeList = findAllAttribute(softDataList);
        Map<String,List<String>> resMap = new LinkedHashMap<>();
        for(int i = 0; i < attributeList.size(); i++){
            String curAttribute = attributeList.get(i);
            List<String> subAttributeList = new ArrayList<>();
            for(int j = 0; j < softDataList.size(); j++){
                String curSubAttribute = softDataList.get(j).getSubAttribute();
                if(softDataList.get(j).getAttribute().equals(curAttribute) && !subAttributeList.contains(curSubAttribute)){
                    subAttributeList.add(curSubAttribute);
                }
            }
            resMap.put(curAttribute, subAttributeList);
        }
        return resMap;
    }

    /**
     * 查找阶段
     * @param softDataList
     * @return
     */
    public List<String> findPhase(List<SoftData> softDataList){
        List<String> phaseList = new ArrayList<>();
        for(int i = 0; i < softDataList.size(); i++){
            String curPhase = softDataList.get(i).getPhase();
            if(!phaseList.contains(curPhase)){
                phaseList.add(curPhase);
            }
        }
        return phaseList  ;
    }

    /**
     * 查找阶段对应的属性
     * @param softDataList
     * @return
     */
    public Map<String, List<String>> findAllAttributeToEachPhase(List<SoftData> softDataList){
        List<String> phaseList = findPhase(softDataList);
        Map<String,List<String>> resMap = new LinkedHashMap<>();
        for(int i = 0; i < phaseList.size(); i++){
            String curPhase = phaseList.get(i);
            List<String> attributeList = new ArrayList<>();
            for(int j = 0; j < softDataList.size(); j++){
                String curAttribute = softDataList.get(j).getAttribute();
                if(softDataList.get(j).getPhase().equals(curPhase) && !attributeList.contains(curAttribute)){
                    attributeList.add(curAttribute);
                }
            }
            resMap.put(curPhase, attributeList);
        }
        return resMap;
    }
}
