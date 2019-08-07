/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.UpdateUserBean;
import com.relipa.religram.controller.bean.response.UpdatedUserBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.User;
import com.relipa.religram.exceptionhandler.UserAlreadyExistException;
import com.relipa.religram.repository.UserRepository;
import com.relipa.religram.util.ImageUtils;
import com.relipa.religram.util.security.jwt.JwtTokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserInfoBean findUserByUserName(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("An error occured!"));
        UserInfoBean userInfoBean = new UserInfoBean();
        BeanUtils.copyProperties(user, userInfoBean);
        userInfoBean.setId(user.getId());

        return userInfoBean;
    }

    @Override
    public UserInfoBean findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Not found user"));

        UserInfoBean userInfoBean = new UserInfoBean();
        BeanUtils.copyProperties(user, userInfoBean);
        userInfoBean.setId(user.getId());

        userInfoBean.setPostCount(postService.countPostByUserId(userId.intValue()));

        // TODO: add count of follower and following
        userInfoBean.setFollowerCount(0);
        userInfoBean.setFollowingCount(0);

        return userInfoBean;
    }

    @Override
    @Transactional
    public UpdatedUserBean updateUserInfo(UpdateUserBean userInfoBean, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("An error occured!"));

        // Validate unique Username
        if (userRepository.countByUsernameAndIdNot(userInfoBean.getUsername(), user.getId()) > 0 ) {
            throw new UserAlreadyExistException(messageSource.getMessage("error.username.existed", null, null, Locale.ENGLISH) + user.getUsername());
        }

        // Save image
        String fileOutput = ImageUtils.getImageFileName(userInfoBean.getAvatar());
        ImageUtils.decodeToImage(userInfoBean.getAvatar(), fileOutput);

        user.setUsername(userInfoBean.getUsername());
        user.setFullname(userInfoBean.getFullname());
        user.setAvatar(fileOutput);
        userRepository.save(user);

        UserInfoBean updatedUserInfo = new UserInfoBean();
        BeanUtils.copyProperties(user, updatedUserInfo);
        updatedUserInfo.setId(user.getId());

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        UpdatedUserBean updatedUserBean = new UpdatedUserBean();
        updatedUserBean.setUser(updatedUserInfo);
        updatedUserBean.setToken(token);

        return updatedUserBean;
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
