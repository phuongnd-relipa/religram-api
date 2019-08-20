/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.*;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.LoginResponseBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.error.ErrorMessage;
import com.relipa.religram.service.FacebookService;
import com.relipa.religram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {


    @Autowired
    UserService userService;

    @Autowired
    FacebookService facebookService;

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        LoginResponseBean loginResponseBean = userService.login(data);

        return ok(loginResponseBean);
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserSignupBean userBean,
                                 @RequestParam(required = false) Boolean viaFacebook) throws Exception {

        if (null != viaFacebook && viaFacebook) {
            LoginResponseBean loginResponseBean = userService.signupFacebook(userBean);

            return ok(loginResponseBean);
        } else {
            LoginResponseBean loginResponseBean = userService.signup(userBean);

            return ok(loginResponseBean);
        }
    }

    @PostMapping("/facebook")
    public ResponseEntity getFacebookUserInfo(@RequestBody FacebookAuthInfoBean facebookAuthInfoBean) {

        FacebookInfoBean facebookInfoBean = facebookService.getUserFacebook(facebookAuthInfoBean);

        UserInfoBean userInfoBean = facebookService.findByFacebookId(facebookInfoBean.getFacebookId());
        if (null != userInfoBean) {
            LoginResponseBean loginResponseBean = userService.loginFacebook(userInfoBean);
            return ok(loginResponseBean);
        }
        return ok(facebookInfoBean);
    }

    @PutMapping("/changepassword")
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordBean changePasswordBean,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        this.userService.changePassword(changePasswordBean, userDetails);
        return ok("");
    }

    @PostMapping("/resetpassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordBean resetPasswordBean) {
        if (userService.requestResetPassword(resetPasswordBean)) {
            return ok("");
        } else {
            return status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(400, "User/Email not existed."));
        }
    }

    @PostMapping("/resetpassword/verify/{token}")
    public ResponseEntity updatePassword(@PathVariable String token,
                                         @RequestBody ChangePasswordBean changePasswordBean) {
        userService.resetPassword(token, changePasswordBean);
        return ok("");
    }
}
