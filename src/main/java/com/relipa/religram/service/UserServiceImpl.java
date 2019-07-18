package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import com.relipa.religram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerNewUserAccount(User user, Locale locale) throws UserAlreadyExistException {
        if (userExist(user.getUsername())) {
            throw new UserAlreadyExistException(messageSource.getMessage("error.username.existed", null, null, Locale.ENGLISH) + user.getUsername());
        }
        if (emailExist(user.getEmail())) {
            throw new UserAlreadyExistException(messageSource.getMessage("error.email.existed", null, null, Locale.ENGLISH) + user.getEmail());
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
