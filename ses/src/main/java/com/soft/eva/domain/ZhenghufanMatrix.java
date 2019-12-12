package com.soft.eva.domain;

/**
 * 正互反矩阵
 *
 */
public class ZhenghufanMatrix {
    String metricElementMatrixArray;//转换成json字符串之后的数组
    String subAttributeMatrixArray;
    String attributeMatrixArray;

    public ZhenghufanMatrix(){

    }

    public ZhenghufanMatrix(String metricElementMatrixArray, String subAttributeMatrixArray, String attributeMatrixArray) {
        this.metricElementMatrixArray = metricElementMatrixArray;
        this.subAttributeMatrixArray = subAttributeMatrixArray;
        this.attributeMatrixArray = attributeMatrixArray;
    }

    public String getMetricElementMatrixArray() {
        return metricElementMatrixArray;
    }

    public void setMetricElementMatrixArray(String metricElementMatrixArray) {
        this.metricElementMatrixArray = metricElementMatrixArray;
    }

    public String getSubAttributeMatrixArray() {
        return subAttributeMatrixArray;
    }

    public void setSubAttributeMatrixArray(String subAttributeMatrixArray) {
        this.subAttributeMatrixArray = subAttributeMatrixArray;
    }

    public String getAttributeMatrixArray() {
        return attributeMatrixArray;
    }

    public void setAttributeMatrixArray(String attributeMatrixArray) {
        this.attributeMatrixArray = attributeMatrixArray;
    }
}
