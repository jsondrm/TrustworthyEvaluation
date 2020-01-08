package com.soft.eva.domain;

import java.io.Serializable;

public class User implements Serializable {
   //f, private static final long serialVersionUID = 1L;

    private String userId;//工号
    private String username;//姓名（用户名）
    private String password;//登录密码
    private String dptName;//所属部门
    private String group;//分组
    private String email;//邮箱
    private String mobile;//手机号
    private Integer status = 1;//状态：0：离职；1：正常

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dptName='" + dptName + '\'' +
                ", group='" + group + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                '}';
    }
}
