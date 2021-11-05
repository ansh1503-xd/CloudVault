package com.sminfotech.cloudvault.Model;

public class UserDocuments {

    String name;
    String uid;
    String docId;
    String docLink;

    public String getDocLink() {
        return docLink;
    }

    public void setDocLink(String docLink) {
        this.docLink = docLink;
    }

    public UserDocuments() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
