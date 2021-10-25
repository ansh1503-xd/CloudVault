package com.sminfotech.cloudvault.Model;

import java.util.List;

public class User {
    String uid;
    List<String> imageList;
    List<String> videoList;
    String inAppPassword;
    List<String> notesList;
    String email;
    String name;

    public User() { }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<String> videoList) {
        this.videoList = videoList;
    }

    public String getInAppPassword() {
        return inAppPassword;
    }

    public void setInAppPassword(String inAppPassword) {
        this.inAppPassword = inAppPassword;
    }

    public List<String> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
