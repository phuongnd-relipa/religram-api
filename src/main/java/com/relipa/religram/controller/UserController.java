/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity getUserInfo(@PathVariable Integer userId) {

        UserInfoBean userInfoBean = userService.findUserById((long) userId);

        return  ok(userInfoBean);
    }

    @PutMapping("")
    public ResponseEntity updateUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return  ok("Successfully");
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity getUserInfo(@PathVariable Integer userId, @RequestParam Integer page) {
        return  ok("Successfully");
    }

}
