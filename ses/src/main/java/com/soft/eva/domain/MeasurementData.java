package com.soft.eva.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MeasurementData implements Serializable {
    String id;
    //所属软件的编号（是哪个软件对应的值）
    String softwareBelongsTo;
    //名称（如szyg-2软件，需求阶段，功能性，适合性，功能定义充分性等）
    String name;
    //类型（软件，阶段，属性，子属性，度量元)
    String type;
    //所属阶段
    String phaseBelongsTo;
    //可信值
    double keXinDu;
    //权重
    double weight;
    public MeasurementData(){ }
    public MeasurementData(String softwareBelongsTo, String name, String type, String phaseBelongsTo, double keXinDu, double weight) {
        this.softwareBelongsTo = softwareBelongsTo;
        this.name = name;
        this.type = type;
        this.phaseBelongsTo = phaseBelongsTo;
        this.keXinDu = keXinDu;
        this.weight = weight;
    }

    public List<MeasurementData> getMeasurementList(String softwareBelongsTo, String type, String phaseBelongsTo, Map<String,Double> keXinDuMap, Map<String,Double> weightMap){
        List<MeasurementData> measurementDataList = new ArrayList<>();
        for(Map.Entry<String,Double> map : keXinDuMap.entrySet()){
            String name = map.getKey();
            double keXinDu = map.getValue();
            double weight = weightMap.get(name);
            MeasurementData measurementData = new MeasurementData(softwareBelongsTo,name,type,phaseBelongsTo,keXinDu,weight);
            measurementDataList.add(measurementData);
        }
        return measurementDataList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhaseBelongsTo() {
        return phaseBelongsTo;
    }

    public void setPhaseBelongsTo(String phaseBelongsTo) {
        this.phaseBelongsTo = phaseBelongsTo;
    }

    public double getKeXinDu() {
        return keXinDu;
    }

    public void setKeXinDu(double keXinDu) {
        this.keXinDu = keXinDu;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "MeasurementData{" +
                "id='" + id + '\'' +
                ", softwareBelongsTo='" + softwareBelongsTo + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", phaseBelongsTo='" + phaseBelongsTo + '\'' +
                ", keXinDu=" + keXinDu +
                ", weight=" + weight +
                '}';
    }
}
