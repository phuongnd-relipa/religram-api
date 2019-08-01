/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.util.security.jwt.JwtTokenProvider;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.UserRepository;
import com.relipa.religram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private static final String EMAIL_PATTERN = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,6}))?$";

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

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();

            if (username.matches(EMAIL_PATTERN)) {
                username = this.userRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("Invalid username/password supplied")).getUsername();
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));

            Map<Object, Object> model = this.getTokenAndUser(username);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid UserSignupBean userBean) {
        User user = User.UserBuilder.builder()
                .username(userBean.getUsername())
                .password(this.passwordEncoder.encode(userBean.getPassword()))
                .roles(Arrays.asList("ROLE_USER"))
                .email(userBean.getEmail())
                .fullName(userBean.getFullname())
                .build();

        this.userService.registerNewUserAccount(user, Locale.ENGLISH);

        Map<Object, Object> model = this.getTokenAndUser(userBean.getUsername());
        return ok(model);
    }

    private Map<Object, Object> getTokenAndUser (String username) {

        User user = this.userRepository.findByUsername(username).get();
        String token = jwtTokenProvider.createToken(username, user.getRoles());

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setId(user.getId());
        userInfoBean.setUsername(user.getUsername());
        userInfoBean.setFullname(user.getFullName());
        userInfoBean.setEmail(user.getEmail());
        userInfoBean.setAvatar(user.getAvatar());

        Map<Object, Object> model = new HashMap<>();
        model.put("user", userInfoBean);
        model.put("token", token);

        return model;
    }
}
