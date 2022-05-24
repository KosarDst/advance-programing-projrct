package com.example.todolistapp;

import java.io.Serializable;

public class Task implements Serializable {
    private String Title;
    private String Description;
    private String Expire;
    private Boolean is_done = false;

    Task(String T, String D, String E) {
        this.Title = T;
        this.Description = D;
        this.Expire = E;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setExpire(String expire) {
        Expire = expire;
    }

    public void setIs_done(Boolean is_done) {
        this.is_done = is_done;
    }

    public String getDescription() {
        return Description;
    }

    public String getExpire() {
        return Expire;
    }

    public Boolean getIs_done() {
        return is_done;
    }

    public String getTitle() {
        return Title;
    }
}
