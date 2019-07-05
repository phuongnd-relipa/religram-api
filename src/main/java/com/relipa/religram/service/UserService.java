package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;

public interface UserService {
    // TODO: Viết hàm validate
    void registerNewUserAccount(User user) throws UserAlreadyExistException;

}
