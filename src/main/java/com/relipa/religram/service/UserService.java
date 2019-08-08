/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.ChangePasswordBean;
import com.relipa.religram.controller.bean.request.ResetPasswordBean;
import com.relipa.religram.controller.bean.request.UpdateUserBean;
import com.relipa.religram.controller.bean.response.UpdatedUserBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;

public interface UserService extends AbstractService<User, Long> {

    UserInfoBean findUserByUserName(String userName);

    UserInfoBean findUserById(Long userId);

    UpdatedUserBean updateUserInfo(UpdateUserBean userInfoBean, UserDetails userDetails);

    boolean changePassword(ChangePasswordBean changePasswordBean, UserDetails userDetails);

    boolean resetPassword(ResetPasswordBean resetPasswordBean);

    void registerNewUserAccount(User user, Locale locale) throws UserAlreadyExistException;
}
