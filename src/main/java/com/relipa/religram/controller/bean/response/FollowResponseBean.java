package com.relipa.religram.controller.bean.response;

public class FollowResponseBean {
    private Boolean isFollowed;

    public FollowResponseBean(Boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public Boolean getFollowed() {
        return isFollowed;
    }

    public void setFollowed(Boolean followed) {
        isFollowed = followed;
    }
}
