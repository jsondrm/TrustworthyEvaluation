package com.soft.eva.domain;

import java.io.Serializable;

public class SoftwareProduct implements Serializable {
    //Long id;
    private String number;//项目编号
    private String softwareName;//待评估软件名称
    private Double keXinDu = Double.valueOf(0.0);//软件可信度
    private Integer status = 0;//状态，0：表示待评测；1：表示评测结束
    private String leader;//项目组长
    private String developManager;//开发负责人
    private String testManager;//测试负责人
    private String QAManager;//质量管理负责人
    private String securityLevel;//保密等级
    private String description;//描述

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public Double getKeXinDu() {
        return keXinDu;
    }

    public void setKeXinDu(Double keXinDu) {
        this.keXinDu = keXinDu;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getDevelopManager() {
        return developManager;
    }

    public void setDevelopManager(String developManager) {
        this.developManager = developManager;
    }

    public String getTestManager() {
        return testManager;
    }

    public void setTestManager(String testManager) {
        this.testManager = testManager;
    }

    public String getQAManager() {
        return QAManager;
    }

    public void setQAManager(String QAManager) {
        this.QAManager = QAManager;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SoftwareProduct{" +
                "number='" + number + '\'' +
                ", softwareName='" + softwareName + '\'' +
                ", keXinDu='" + keXinDu + '\'' +
                ", status=" + status +
                ", leader='" + leader + '\'' +
                ", developManager='" + developManager + '\'' +
                ", testManager='" + testManager + '\'' +
                ", QAManager='" + QAManager + '\'' +
                ", securityLevel='" + securityLevel + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
