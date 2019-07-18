package com.relipa.religram.controller.bean.response;

import com.relipa.religram.controller.bean.AbstractBean;

public class CommentBean extends AbstractBean {

    private UserInfoBean user;
    private String comment;

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
