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
@RequestMapping("/auth")
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

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            User user = this.userRepository.findByUsername(username).get();

            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUsername(user.getUsername());
            userInfoBean.setFullname(user.getFullName());
            userInfoBean.setEmail(user.getEmail());
            userInfoBean.setAvatar(user.getAvatar());


            Map<Object, Object> model = new HashMap<>();
            model.put("user", userInfoBean);
            model.put("token", token);
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

        String token = jwtTokenProvider.createToken(userBean.getUsername(), this.userRepository.findByUsername(userBean.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username " + userBean.getUsername() + "not found")).getRoles());

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUsername(userBean.getUsername());
        userInfoBean.setFullname(userBean.getFullname());
        userInfoBean.setEmail(userBean.getEmail());
        userInfoBean.setAvatar("");

        Map<Object, Object> model = new HashMap<>();
        model.put("user", userInfoBean);
        model.put("token", token);
        return ok(model);
    }
}
