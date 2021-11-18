package com.geektech.android3lesson2.data.models;

import java.io.Serializable;

public class Post implements Serializable {

    private int id;
    private String title;
    private String content;
    private int user;
    private int group;

    public Post(String title, String content, int user, int group) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }


}
