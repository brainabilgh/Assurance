package com.journaldev.customlistview;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class PostUser {
    public String username;
    public String password;
    public String role;

    public PostUser(){

    }

    public PostUser(String username, String password, String role) {
        this.role = role;
        this.username = username;
        this.password = password;
    }
}