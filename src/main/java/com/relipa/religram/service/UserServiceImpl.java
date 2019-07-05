package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import com.relipa.religram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerNewUserAccount(User user)
            throws UserAlreadyExistException {

        if (userExist(user.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that user account: " + user.getUsername());
        }
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email adress: " + user.getEmail());
        }

        //save UserAccountNew
        this.userRepository.save(user);

    }

    private boolean userExist(String userName) {

        Optional<User> user = userRepository.findByUsername(userName);
        return user.isPresent();
    }

    private boolean emailExist(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }


}
