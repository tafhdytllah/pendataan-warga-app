package com.tafh.tugasakhir.model;

public class ModelPost {
    //use same name as we given while uploading post
    private String postId, pTitle, pDescr, pImage, pTime, userId,  userName, userEmail, userImage;

    public ModelPost() {
    }

    public ModelPost(String postId, String pTitle, String pDescr, String pImage, String pTime, String userId, String userName, String userEmail, String userImage) {
        this.postId = postId;
        this.pTitle = pTitle;
        this.pDescr = pDescr;
        this.pImage = pImage;
        this.pTime = pTime;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImage = userImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
