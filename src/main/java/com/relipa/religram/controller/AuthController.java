/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.constant.Constant;
import com.relipa.religram.controller.bean.request.ChangePasswordBean;
import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.request.ResetPasswordBean;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.error.ErrorMessage;
import com.relipa.religram.repository.UserRepository;
import com.relipa.religram.service.FacebookService;
import com.relipa.religram.service.UserService;
import com.relipa.religram.util.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    FacebookService facebookService;

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();

            if (username.matches(Constant.EMAIL_PATTERN)) {
                username = this.userRepository.findByEmail(username).orElseThrow(()
                        -> new BadCredentialsException("Invalid username/password supplied")).getUsername();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            Map<Object, Object> model = this.getTokenAndUser(username);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserSignupBean userBean,
                                 @RequestParam(required = false) Boolean viaFacebook) throws Exception {

        if (null != viaFacebook && viaFacebook) {
            UserInfoBean userInfoBean = facebookService.signupFacebook(userBean);
            Map<Object, Object> model = this.getTokenAndUser(userInfoBean.getUsername());

            return ok(model);
        } else {
            User user = User.UserBuilder.builder()
                    .username(userBean.getUsername())
                    .password(this.passwordEncoder.encode(userBean.getPassword()))
                    .roles(Arrays.asList("ROLE_USER"))
                    .email(userBean.getEmail())
                    .fullname(userBean.getFullname())
                    .build();

            this.userService.registerNewUserAccount(user, Locale.ENGLISH);

            Map<Object, Object> model = this.getTokenAndUser(userBean.getUsername());
            return ok(model);
        }
    }

    @PostMapping("/signup/facebook")
    public ResponseEntity getFacebookUserInfo(@RequestBody FacebookAuthInfoBean facebookAuthInfoBean) {

        FacebookInfoBean facebookInfoBean = facebookService.getUserFacebook(facebookAuthInfoBean);
        return ok(facebookInfoBean);
    }

    @PutMapping("/changepassword")
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordBean changePasswordBean,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        this.userService.changePassword(changePasswordBean, userDetails);
        return ok("");
    }

    private Map<Object, Object> getTokenAndUser (String username) {

        User user = this.userRepository.findByUsername(username).get();
        String token = jwtTokenProvider.createToken(username, user.getRoles());

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setId(user.getId());
        userInfoBean.setUsername(user.getUsername());
        userInfoBean.setFullname(user.getFullname());
        userInfoBean.setEmail(user.getEmail());
        userInfoBean.setAvatar(user.getAvatar());

        Map<Object, Object> model = new HashMap<>();
        model.put("user", userInfoBean);
        model.put("token", token);

        return model;
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
