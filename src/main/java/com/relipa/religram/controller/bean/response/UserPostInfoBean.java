package com.relipa.religram.controller.bean.response;

import java.util.List;

public class UserPostInfoBean {
    private Integer totalPage;
    private List<PostBean> posts;

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<PostBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostBean> posts) {
        this.posts = posts;
    }
}
