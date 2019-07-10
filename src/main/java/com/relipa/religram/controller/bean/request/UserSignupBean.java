package com.relipa.religram.controller.bean.request;

import com.relipa.religram.validator.ValidUserName;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserSignupBean {

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "error.maxlength}")
    @ValidUserName(message = "{error.username.format}")
    private String username;

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "{error.maxlength}")
    private String password;

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "{error.maxlength}")
    private String fullname;

    @NotBlank(message = "{error.notBlank}")
    @Size(max = 32, message = "{error.maxlength}")
    @Email(message = "{error.email}")
    private String email;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
