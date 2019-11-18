/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.*;
import com.relipa.religram.controller.bean.response.LoginResponseBean;
import com.relipa.religram.controller.bean.response.UpdatedUserBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Locale;

public interface UserService extends AbstractService<User, Long> {

    LoginResponseBean login(AuthenticationRequest data);

    LoginResponseBean signup(UserSignupBean userBean);

    UserInfoBean findUserByUserName(String userName);

    UserInfoBean findUserById(Long userId);

    List<UserInfoBean> findAllUsers();

    List<UserInfoBean> searchUsersByName(String userName, Integer page);

    UpdatedUserBean updateUserInfo(UpdateUserBean userInfoBean, UserDetails userDetails);

    boolean changePassword(ChangePasswordBean changePasswordBean, UserDetails userDetails);

    boolean requestResetPassword(ResetPasswordBean resetPasswordBean);

    boolean resetPassword(String token, ChangePasswordBean changePasswordBean);

    void registerNewUserAccount(User user, Locale locale) throws UserAlreadyExistException;

    LoginResponseBean loginFacebook(UserInfoBean UserInfoBean);

    LoginResponseBean signupFacebook(UserSignupBean userSignupBean) throws Exception;

}
