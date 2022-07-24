package com.springboot.jwt_study.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class User {
    private long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN

    public List<String> getRoleList() {
        if(this.roles.length()>0)
            return Arrays.asList(this.roles.split(","));
        else
            return new ArrayList<>();
    }
}
