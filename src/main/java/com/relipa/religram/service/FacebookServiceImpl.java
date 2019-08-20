/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.FacebookUser;
import com.relipa.religram.repository.FacebookUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class FacebookServiceImpl extends AbstractServiceImpl<FacebookUser, Long> implements FacebookService {

    @Autowired
    FacebookUserRepository facebookUserRepository;

    @Override
    public UserInfoBean findByFacebookId(String facebookId) {
        FacebookUser facebookUser = facebookUserRepository.findFirstByFacebookId(facebookId);
        if (null != facebookUser) {
            com.relipa.religram.entity.User user = facebookUser.getUser();
            UserInfoBean userInfoBean = new UserInfoBean();
            BeanUtils.copyProperties(user, userInfoBean);
            userInfoBean.setId(user.getId());
            return userInfoBean;
        }
        return null;
    }

    @Override
    public FacebookInfoBean getUserFacebook(FacebookAuthInfoBean facebookAuthInfoBean) {

        Facebook facebook = new FacebookTemplate(facebookAuthInfoBean.getAccessToken());
        String[] fields = {"id", "name", "email"};

        User user = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        URI uri = URIBuilder.fromUri("https://graph.facebook.com/" + user.getId()
                                    + "/picture?type=large&height=120&width=120&redirect=false").build();
        JsonNode response = facebook.restOperations().getForObject(uri, JsonNode.class);
        String avatarUrl = response.get("data").get("url").textValue();

        FacebookInfoBean facebookInfoBean = new FacebookInfoBean();
        facebookInfoBean.setAvatar(avatarUrl);
        facebookInfoBean.setEmail(user.getEmail());
        facebookInfoBean.setFacebookId(user.getId());
        facebookInfoBean.setFullname(user.getName());

        return facebookInfoBean;
    }

}
