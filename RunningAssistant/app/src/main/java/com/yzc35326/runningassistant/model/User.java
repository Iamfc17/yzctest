package com.yzc35326.runningassistant.model;

import java.io.Serializable;

public class User implements Serializable {
    private Integer userId;
    private String userPhone;
    private String userName;
    private String userPassword;
    private Integer userAdmin;
    private String userQq;
    private String userPic;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userPhone='" + userPhone + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAdmin=" + userAdmin +
                ", userQq='" + userQq + '\'' +
                ", userPic='" + userPic + '\'' +
                '}';
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(Integer userAdmin) {
        this.userAdmin = userAdmin;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
