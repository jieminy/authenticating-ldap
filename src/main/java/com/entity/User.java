package com.entity;

import com.common.Globals;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/***
 * 用户实体类
 */
public class User {
    private String cn;
    private String sn;
    @NotNull
    private String uid;
    @Max(message = "最大长度不能超过10",value = 10l)
    private String password;
    private Globals.Role role;
    @Email(message = "必须为邮箱格式")
    private String mail;
    private String mobile;
    private String postaladdress;
    private String postofficebox;
    private String street;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getPostofficebox() {
        return postofficebox;
    }

    public void setPostofficebox(String postofficebox) {
        this.postofficebox = postofficebox;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }



    public User(){}

    public User(String uid, String password){
        this.uid = uid;
        this.password = password;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Globals.Role getRole() {
        return role;
    }

    public void setRole(Globals.Role role) {
        this.role = role;
    }
}
