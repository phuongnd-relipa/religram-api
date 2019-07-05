package com.relipa.religram.controller.bean.request;

import com.relipa.religram.validator.ValidEmail;
import com.relipa.religram.validator.ValidUserName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserSignupBean {

    @NotBlank(message = "{error.user.username.notBlank}")
    @Size(max = 32,message = "{error.user.username.size}")
    @ValidUserName(message = "{error.user.username.fomart}")
    private String username;

    @NotBlank
    @Size(max = 32)
    private String password;

    @NotBlank
    @Size(max = 32)
    private String fullname;

    @NotBlank
    @Size(max = 32)
    @ValidEmail
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
