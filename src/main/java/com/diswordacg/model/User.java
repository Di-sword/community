package com.diswordacg.model;

import lombok.Data;

@Data
public class User {
    private int u_id;
    private String u_name;
    private String u_password;
    private String u_touxiang;
    private String u_email;
    private Integer u_authority;

}
