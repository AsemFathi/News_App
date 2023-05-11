package com.asem.newsapp;

public class ModelComment {
    String cId , comment , image , ptime , uemail , uname;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public ModelComment(String cId, String comment, String image, String ptime, String uemail, String uname) {
        this.cId = cId;
        this.comment = comment;
        this.image = image;
        this.ptime = ptime;
        this.uemail = uemail;
        this.uname = uname;
    }
}
