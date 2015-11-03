package com.toocomplicated.mademesmile;

/**
 * Created by User on 22/10/2558.
 */
public class Comment {
    private String detail;
    private User user;
    private String time;

    public Comment(String detail) {
        this.detail = detail;
        this.time = null;
        this.user = null;
    }

    public Comment(String detail, String time, User user) {
        this.detail = detail;
        this.time = time;
        this.user = user;
    }

    public String getDetail() {
        return detail;
    }
    public User getUser(){
        return user;
    }

    public String getTime() {
        return time;
    }
}