package com.asem.newsapp;

import android.widget.ImageView;

public class Posts {
    String date , title , desc , url;

    public Posts(String date, String title, String desc, String url) {
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
