/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.*;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.LoginResponseBean;
import com.relipa.religram.controller.bean.response.SwaggerEmptyModel;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.error.ErrorMessage;
import com.relipa.religram.exceptionhandler.FacebookNotFoundException;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import com.relipa.religram.service.FacebookService;
import com.relipa.religram.service.UserService;
import io.swagger.annotations.*;
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
@Api(tags = {"auth"})
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    FacebookService facebookService;

    @PostMapping("/login")
    @ApiOperation(value = "${auth.login.value}", notes = "${auth.login.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "response.message.200", response = LoginResponseBean.class),
            @ApiResponse(code = 401, message = "response.error.auth")
    })
    public ResponseEntity signin(@ApiParam(value = "${auth.login.param.data}", required = true) @RequestBody AuthenticationRequest data) {
        LoginResponseBean loginResponseBean = userService.login(data);
        return ok(loginResponseBean);
    }

    @PostMapping("/login/facebook")
    @ApiOperation(value = "${auth.loginFacebook.value}", notes = "${auth.loginFacebook.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "response.message.200", response = LoginResponseBean.class),
            @ApiResponse(code = 301, message = "response.info.redirect.facebook",
                    responseHeaders = @ResponseHeader(name = "Location", description = "Link redirect sang trang đăng ký user bằng Facebook", response = String.class)),
            @ApiResponse(code = 409, message = "response.error.user.exist", response = ErrorMessage.class)

    })
    public ResponseEntity signinFacebook(@ApiParam(value = "${auth.loginFacebook.param.token}", required = true) @RequestBody FacebookAuthInfoBean facebookAuthInfoBean) {
        FacebookInfoBean facebookInfoBean = facebookService.getUserFacebook(facebookAuthInfoBean);
        if (facebookInfoBean != null && facebookInfoBean.getFacebookId() != null) {
            UserInfoBean userInfoBean = facebookService.findByFacebookId(facebookInfoBean.getFacebookId());
            if (userInfoBean != null) {
                LoginResponseBean loginResponseBean = userService.loginFacebook(userInfoBean);
                return ok(loginResponseBean);
            } else {
                throw new FacebookNotFoundException("This account is not registed!");
            }
        } else {
            throw new FacebookNotFoundException("Can not found this account!");
        }
    }

    /*@PostMapping("/signup")
    @ApiOperation(value = "${auth.signup.value}", notes = "${auth.signup.notes}")
    public ResponseEntity signup(@ApiParam(value = "${auth.signup.param.data}") @RequestBody @Valid UserSignupBean userBean,
                                 @ApiParam(value = "${auth.signup.param.viaFacebook}") @RequestParam(required = false) Boolean viaFacebook) throws Exception {

        if (null != viaFacebook && viaFacebook) {
            LoginResponseBean loginResponseBean = userService.signupFacebook(userBean);

            return ok(loginResponseBean);
        } else {
            LoginResponseBean loginResponseBean = userService.signup(userBean);

            return ok(loginResponseBean);
        }
    }*/

    @PostMapping("/signup")
    @ApiOperation(value = "${auth.signup.value}", notes = "${auth.signup.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "response.message.200", response = LoginResponseBean.class),
            @ApiResponse(code = 400, message = "response.error.validate.param", response = ErrorMessage.class),
            @ApiResponse(code = 409, message = "response.error.user.exist", response = ErrorMessage.class)
    })
    public ResponseEntity signup(@ApiParam(value = "${auth.signup.param.data}", required = true) @RequestBody @Valid UserSignupBean userBean) {
        LoginResponseBean loginResponseBean = userService.signup(userBean);
        return ok(loginResponseBean);
    }

    @PostMapping("/signup/facebook")
    @ApiOperation(value = "${auth.signupFacebook.value}", notes = "${auth.signupFacebook.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = LoginResponseBean.class)})
    public ResponseEntity signUpFacebook(@ApiParam(value = "${auth.signupFacebook.param.data}", required = true) @RequestBody FacebookAuthInfoBean facebookAuthInfoBean) throws Exception {
        FacebookInfoBean facebookInfoBean = facebookService.getUserFacebook(facebookAuthInfoBean);
        if (facebookInfoBean != null && facebookInfoBean.getFacebookId() != null) {
            UserInfoBean userInfoBean = facebookService.findByFacebookId(facebookInfoBean.getFacebookId());
            if (userInfoBean != null) {
                throw new UserAlreadyExistException("This account with facebook is in used!");
            }

            UserSignupBean userSignupBean = new UserSignupBean();
            userSignupBean.setUsername(facebookInfoBean.getFullname());
            userSignupBean.setEmail(facebookInfoBean.getEmail());
            userSignupBean.setFullname(facebookInfoBean.getFullname());
            userSignupBean.setAvatar(facebookInfoBean.getAvatar());
            userSignupBean.setPassword(facebookInfoBean.getFacebookId());
            userSignupBean.setFacebookId(facebookInfoBean.getFacebookId());

            LoginResponseBean loginResponseBean = userService.signupFacebook(userSignupBean);
            return ok(loginResponseBean);
        } else {
            throw new FacebookNotFoundException("Can not register user with facebook!");
        }
    }

    @PostMapping("/facebook")
    @ApiOperation(value = "${auth.facebook.value}", notes = "${auth.facebook.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = FacebookInfoBean.class)})
    public ResponseEntity getFacebookUserInfo(@ApiParam(value = "${auth.facebook.param.data}") @RequestBody FacebookAuthInfoBean facebookAuthInfoBean) {

        FacebookInfoBean facebookInfoBean = facebookService.getUserFacebook(facebookAuthInfoBean);

        /*UserInfoBean userInfoBean = facebookService.findByFacebookId(facebookInfoBean.getFacebookId());
        if (null != userInfoBean) {
            LoginResponseBean loginResponseBean = userService.loginFacebook(userInfoBean);
            return ok(loginResponseBean);
        }*/
        return ok(facebookInfoBean);
    }

    @PutMapping("/changepassword")
    @ApiOperation(value = "${auth.changePassword.value}", notes = "${auth.changePassword.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "response.message.200", response = SwaggerEmptyModel.class),
            @ApiResponse(code = 400, message = "response.error.validate.password", response = ErrorMessage.class)
    })
    public ResponseEntity changePassword(@ApiParam(value = "${auth.changePassword.param.body}") @RequestBody @Valid ChangePasswordBean changePasswordBean,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        this.userService.changePassword(changePasswordBean, userDetails);
        return ok("");
    }

    @PostMapping("/resetpassword")
    @ApiOperation(value = "${auth.resetPassword.value}", notes = "${auth.resetPassword.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = SwaggerEmptyModel.class)})
    public ResponseEntity resetPassword(@ApiParam(value = "${auth.resetPassword.param.body}") @RequestBody ResetPasswordBean resetPasswordBean) {
        if (userService.requestResetPassword(resetPasswordBean)) {
            return ok("");
        } else {
            return status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorMessage(400, "User/Email not existed."));
        }
    }

    @PostMapping("/resetpassword/verify/{token}")
    @ApiOperation(value = "${auth.resetPasswordVerify.value}", notes = "${auth.resetPasswordVerify.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = SwaggerEmptyModel.class)})
    public ResponseEntity updatePassword(@ApiParam(value = "${auth.resetPasswordVerify.param.token}") @PathVariable String token,
                                         @ApiParam(value = "${auth.resetPasswordVerify.param.body}") @RequestBody ChangePasswordBean changePasswordBean) {
        userService.resetPassword(token, changePasswordBean);
        return ok("");
    }
}
