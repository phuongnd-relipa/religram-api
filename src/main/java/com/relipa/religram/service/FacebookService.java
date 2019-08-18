/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.FacebookAuthInfoBean;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.FacebookInfoBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;

public interface FacebookService {

    FacebookInfoBean getUserFacebook(FacebookAuthInfoBean facebookAuthInfoBean);

    UserInfoBean signupFacebook(UserSignupBean userSignupBean) throws Exception;
}
