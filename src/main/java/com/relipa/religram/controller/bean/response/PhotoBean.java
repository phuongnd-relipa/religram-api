package com.relipa.religram.controller.bean.response;

import com.relipa.religram.controller.bean.AbstractBean;

public class PhotoBean extends AbstractBean {

    private String photoUri;

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
