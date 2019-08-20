/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.FacebookUser;

public interface FacebookService extends AbstractService<FacebookUser, Long> {

    UserInfoBean findByFacebookId(String facebookId);

    FacebookInfoBean getUserFacebook(FacebookAuthInfoBean facebookAuthInfoBean);

}
