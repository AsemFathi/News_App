package com.asem.newsapp;

import android.widget.ImageView;

public class Posts {
    String date , title , desc , url , likes , comments , author , authorImage;

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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public Posts(String date, String title, String desc, String url, String likes, String comments, String author, String authorImage) {
        this.date = date;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.likes = likes;
        this.comments = comments;
        this.author = author;
        this.authorImage = authorImage;
    }
}
