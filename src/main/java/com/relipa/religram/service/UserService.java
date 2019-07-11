package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;

import java.util.Locale;

public interface UserService {

    void registerNewUserAccount(User user, Locale locale) throws UserAlreadyExistException;
}
