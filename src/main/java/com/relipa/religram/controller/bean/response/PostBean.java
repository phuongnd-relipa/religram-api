package com.relipa.religram.controller.bean.response;

import com.relipa.religram.controller.bean.AbstractBean;
import com.relipa.religram.entity.Comment;

import java.util.List;

public class PostBean extends AbstractBean {

    private UserInfoBean user;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private CommentBean [] comments;
    private PhotoBean [] photos;

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public CommentBean[] getComments() {
        return comments;
    }

    public void setComments(CommentBean[] comments) {
        this.comments = comments;
    }

    public PhotoBean[] getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoBean[] photos) {
        this.photos = photos;
    }
}
