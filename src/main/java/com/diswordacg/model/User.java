package com.diswordacg.model;

public class User {
    private int u_id;
    private String u_name;
    private String u_password;
    private String u_touxiang;
    private String u_email;
    private String u_authority;

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_touxiang() {
        return u_touxiang;
    }

    public void setU_touxiang(String u_touxiang) {
        this.u_touxiang = u_touxiang;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_authority() {
        return u_authority;
    }

    public void setU_authority(String u_authority) {
        this.u_authority = u_authority;
    }
}