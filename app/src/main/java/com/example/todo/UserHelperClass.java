package com.example.todo;

public class UserHelperClass {
    String username,password, mobile,fullname;

    public UserHelperClass(String username, String password, String mobile, String fullname) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.fullname = fullname;
    }

    public UserHelperClass() {
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
