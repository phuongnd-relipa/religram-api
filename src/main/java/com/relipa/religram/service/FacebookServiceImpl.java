/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class FacebookServiceImpl implements FacebookService {

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
}
