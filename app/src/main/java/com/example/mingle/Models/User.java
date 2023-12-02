package com.example.mingle.Models;

public class User {
    String profile;
    String username;
    String password;
    String mail;
    String userId;
    String lastMsg;

    String about;


    public String getAbout() {
        return about;
    }

    public User(String profile, String username, String password, String mail, String userId, String lastMsg, String about) {
        this.profile = profile;
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.userId = userId;
        this.lastMsg = lastMsg;
        this.about = about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public User(String profile, String mail, String username, String password, String userId, String lastMsg) {
        this.profile = profile;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.lastMsg = lastMsg;
        this.mail = mail;
    }
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public User( String username, String mail, String password) {
       this.mail = mail;
        this.username = username;
        this.password = password;

    }

    public User()
    {

    }
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
}
