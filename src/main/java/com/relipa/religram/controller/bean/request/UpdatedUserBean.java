/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller.bean.request;

import com.relipa.religram.validator.ValidImage;
import com.relipa.religram.validator.ValidUserName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatedUserBean {

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "error.maxlength}")
    @ValidUserName(message = "{error.username.format}")
    private String username;

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "{error.maxlength}")
    private String fullname;

    @ValidImage
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
