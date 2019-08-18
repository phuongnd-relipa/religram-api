/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.relipa.religram.constant.Constant;
import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.FacebookUser;
import com.relipa.religram.repository.FacebookUserRepository;
import com.relipa.religram.util.ImageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Arrays;
import java.util.Locale;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    FacebookUserRepository facebookUserRepository;

    @Override
    public FacebookInfoBean getUserFacebook(FacebookAuthInfoBean facebookAuthInfoBean) {

        Facebook facebook = new FacebookTemplate(facebookAuthInfoBean.getAccessToken());
        String[] fields = {"id", "name", "email"};

        User user = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        URI uri = URIBuilder.fromUri("https://graph.facebook.com/" + user.getId()
                                    + "/picture?type=large&height=300&width=300&redirect=false").build();
        JsonNode response = facebook.restOperations().getForObject(uri, JsonNode.class);
        String avatarUrl = response.get("data").get("url").textValue();

        FacebookInfoBean facebookInfoBean = new FacebookInfoBean();
        facebookInfoBean.setAvatar(avatarUrl);
        facebookInfoBean.setEmail(user.getEmail());
        facebookInfoBean.setFacebookId(user.getId());
        facebookInfoBean.setFullname(user.getName());

        return facebookInfoBean;
    }

    @Override
    @Transactional
    public UserInfoBean signupFacebook(UserSignupBean userSignupBean) throws Exception {

        if (null == userSignupBean.getFacebookId()) {
            throw new Exception("Not success");
        }

        com.relipa.religram.entity.User user = com.relipa.religram.entity.User.UserBuilder.builder()
                .username(userSignupBean.getUsername())
                .password(this.passwordEncoder.encode(userSignupBean.getPassword()))
                .roles(Arrays.asList("ROLE_USER"))
                .email(userSignupBean.getEmail())
                .fullname(userSignupBean.getFullname())
                .build();

        if (null != userSignupBean.getAvatar()) {
            String avatarFile = ImageUtils.getImageFileName(Constant.DEFAULT_IMAGE_EXT);
            ImageUtils.saveImage(userSignupBean.getAvatar(), avatarFile);
            user.setAvatar(avatarFile);
        }

        this.userService.registerNewUserAccount(user, Locale.ENGLISH);

        FacebookUser facebookUser = new FacebookUser();
        facebookUser.setFacebookId(userSignupBean.getFacebookId());
        facebookUser.setUser(user);
        facebookUserRepository.save(facebookUser);

        UserInfoBean userInfoBean = new UserInfoBean();
        BeanUtils.copyProperties(user, userInfoBean);
        userInfoBean.setId(user.getId());

        return userInfoBean;
    }
}
