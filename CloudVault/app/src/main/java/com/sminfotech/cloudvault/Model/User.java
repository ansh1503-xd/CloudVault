package com.sminfotech.cloudvault.Model;

import java.util.List;

public class User {
    String uid;
    List<String> imageList;
    List<String> videoList;
    String inAppPassword;
    String email;
    String name;
    Boolean panicSwitch;
    List<String> audioList;
    List<String> documentsList;
    List<String> notesList;
    long totalDataQuota;
    long usedDataQuota;

    public User() {
    }

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

    public Boolean getPanicSwitch() {
        return panicSwitch;
    }

    public void setPanicSwitch(Boolean panicSwitch) {
        this.panicSwitch = panicSwitch;
    }

    public List<String> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<String> audioList) {
        this.audioList = audioList;
    }

    public List<String> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<String> notesList) {
        this.notesList = notesList;
    }

    public List<String> getDocumentsList() {
        return documentsList;
    }

    public void setDocumentsList(List<String> documentsList) {
        this.documentsList = documentsList;
    }

    public long getTotalDataQuota() {
        return totalDataQuota;
    }

    public void setTotalDataQuota(long totalDataQuota) {
        this.totalDataQuota = totalDataQuota;
    }

    public long getUsedDataQuota() {
        return usedDataQuota;
    }

    public void setUsedDataQuota(long usedDataQuota) {
        this.usedDataQuota = usedDataQuota;
    }
}
