package com.relipa.religram.controller.bean.response;

import java.util.List;

public class FollowerUserInfoBean {
    private int count;
    private List<UserInfoBean> userInfoBeans;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<UserInfoBean> getUserInfoBeans() {
        return userInfoBeans;
    }

    public void setUserInfoBeans(List<UserInfoBean> userInfoBeans) {
        this.userInfoBeans = userInfoBeans;
    }
}
