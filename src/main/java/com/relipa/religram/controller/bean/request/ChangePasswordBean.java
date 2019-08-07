/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller.bean.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordBean {

    private String currentPassword;

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "{error.maxlength}")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
