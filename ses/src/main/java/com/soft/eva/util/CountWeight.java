package com.soft.eva.util;

import com.soft.eva.domain.SoftData;

import java.util.*;

public class CountWeight {

    public Map<String,Double> countWeight_MetricElement(List<String> list,List<SoftData> softDataList){
        List<String> subAttributeList = new FindAll().findAllSubAttribute(softDataList);//所有子属性
        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataList);//String:子属性名称，List:该子属性对应的度量元
        Map<String,Double> resMap  = countWeight(list, subAttributeList, subAttributeMap);
        return resMap;
        /*Map<String,Double> resMap = new HashMap<>();//String: 度量元名称，Double:该度量元对应的权重
        //将接收到的前端list中的string 类型转换成double类型，并保留两位小数，存到数组中
        double[] array = new double[list.size()];
        for(int i = 0; i < list.size(); i++){
            String curStr = list.get(i);
            if(curStr.contains("/")){
                String[] strArr = curStr.split("/");
                double temp = (Double.valueOf(strArr[0]))/(Double.valueOf(strArr[1]));
                array[i] = (double)Math.round(temp*100)/100;
            }else{
                array[i] = (double)Math.round(Double.valueOf(list.get(i))*100)/100;
            }
        }
        List<String> subAttributeList = new FindAll().findAllSubAttribute(softDataList);//所有子属性
        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataList);//String:子属性名称，List:该子属性对应的度量元
        int countOfSubAttribute = subAttributeMap.size();//子属性的个数
        int index = 0;//array 数组的下标
        List<Double> CRList = new ArrayList<>();//存放所有的CR
        for(int i = 0; i < countOfSubAttribute; i++){
            String curSubAttribute = subAttributeList.get(i);//当前子属性
            List<String> curMetricElementList = subAttributeMap.get(curSubAttribute);//当前子属性对应的度量元
            int n = curMetricElementList.size();//子属性对应的度量元个数
            double[][] matrix = new double[n][n];//正互反矩阵，个数与子属性个数一致
            for(int p = 0; p < n; p++){
                for(int q = 0; q < n; q++){
                    matrix[p][q] = array[index++];
                }
            }
            Map<String,Double> weightMap = new AHP().countWeightOneMatrix(matrix, n);//计算权重之后返回的结果，“CR":XX,"1":XXX,"2":XXX
            CRList.add(weightMap.get("CR"));
            for(int j = 0; j < n; j++){
                String curMetricElement = curMetricElementList.get(j);
                double curWeight = weightMap.get(String.valueOf(j));
                resMap.put(curMetricElement,curWeight);
            }
        }
        for(double cr : CRList){
            if(cr > 0.1){
                return null;
            }
        }
        return resMap;*/
    }

    public Map<String,Double> countWeight_SubAttribute(List<String> list,List<SoftData> softDataList) {
        List<String> attributeList = new FindAll().findAllAttribute(softDataList);//所有属性
        Map<String, List<String>> attributeMap = new FindAll().findAllSubAttributeToEachAttribute(softDataList);//String:属性名称，List:该属性对应的子属性
        Map<String, Double> resMap = countWeight(list, attributeList, attributeMap);
        return resMap;
    }

    public Map<String,Double> countWeight_Attribute(List<String> list,List<SoftData> softDataList) {
        List<String> phaseList = new FindAll().findPhase(softDataList);//所有子属性
        Map<String, List<String>> phaseMap = new FindAll().findAllAttributeToEachPhase(softDataList);//String:子属性名称，List:该子属性对应的度量元
        Map<String, Double> resMap = countWeight(list, phaseList, phaseMap);
        return resMap;
    }

    /**
     *
     * @param arrayList 前端输入框中的数组元素
     * @param list 要计算权重的父级元素
     * @param map  key: 父级元素，value: 父子元素对应的子元素
     * @return
     */
    public Map<String,Double> countWeight(List<String> arrayList,List<String> list,Map<String,List<String>> map){
        Map<String,Double> resMap = new LinkedHashMap<>();//String: 度量元名称，Double:该度量元对应的权重
        //将接收到的前端list中的string 类型转换成double类型，并保留两位小数，存到数组中
        double[] array = new double[arrayList.size()];
        for(int i = 0; i < arrayList.size(); i++){
            String curStr = arrayList.get(i);
            if(curStr.contains("/")){
                String[] strArr = curStr.split("/");
                if(strArr[0].equals("") || strArr[1].equals("")){//判断分数的格式是否正确，分子和分母都不能是空格
                    return null;
                }
                double temp = (Double.valueOf(strArr[0]))/(Double.valueOf(strArr[1]));
                array[i] = (double)Math.round(temp*100)/100;
            }else{
                array[i] = (double)Math.round(Double.valueOf(arrayList.get(i))*100)/100;
            }
        }
//        List<String> subAttributeList = new FindAll().findAllSubAttribute(softDataList);//所有子属性
//        Map<String,List<String>> subAttributeMap = new FindAll().findAllMetricElementToEachSubAttribute(softDataList);//String:子属性名称，List:该子属性对应的度量元
        int countOfParent = map.size();//子属性的个数
        int index = 0;//array 数组的下标
        List<Double> CRList = new ArrayList<>();//存放所有的CR
        for(int i = 0; i < countOfParent; i++){
            String curParent = list.get(i);//当前子属性
            List<String> curChildrenList = map.get(curParent);//当前子属性对应的度量元
            int n = curChildrenList.size();//子属性对应的度量元个数
            double[][] matrix = new double[n][n];//正互反矩阵，个数与子属性个数一致
            for(int p = 0; p < n; p++){
                for(int q = 0; q < n; q++){
                    matrix[p][q] = array[index++];
                }
            }
            Map<String,Double> weightMap = new AHP().countWeightOneMatrix(matrix, n);//计算权重之后返回的结果，“CR":XX,"1":XXX,"2":XXX
//            CRList.add(weightMap.get("CR"));
            int cmp = weightMap.get("CR").compareTo(Double.valueOf(0.1));
            if(cmp > 0){
                resMap.put("CR", 1.0);//如果 CR的值大于0.1，将CR放进map，之后根据map中key为"CR"的value是否存在判断 是否满足一致性
                return resMap;
            }
            for(int j = 0; j < n; j++){
                String curElement = curChildrenList.get(j);
                double curWeight = weightMap.get(String.valueOf(j));
                resMap.put(curElement,curWeight);
            }
        }
//        for(double cr : CRList){
//            if(cr > 0.1){
//                return null;
//            }
//        }
        return resMap;
    }
}
