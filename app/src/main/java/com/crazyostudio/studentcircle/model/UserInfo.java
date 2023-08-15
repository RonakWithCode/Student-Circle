package com.crazyostudio.studentcircle.model;


public class UserInfo {
    private String name,bio,userImage,userid,mail,number;

    public UserInfo(String name,String userImage,String mail,String Number) {
        this.name = name;
        this.userImage = userImage;
        this.mail = mail;
        this.number =Number;
    }

    public UserInfo(){}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

}


