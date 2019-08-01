/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;

import java.util.Locale;

public interface UserService extends AbstractService<User, Long> {

    UserInfoBean findUserByUserName(String userName);

    UserInfoBean findUserById(Long userId);

    void registerNewUserAccount(User user, Locale locale) throws UserAlreadyExistException;
}
