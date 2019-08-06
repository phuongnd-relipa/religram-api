/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.UpdatedUserBean;
import com.relipa.religram.controller.bean.request.UserSignupBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.service.PostService;
import com.relipa.religram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/{userId}")
    public ResponseEntity getUserInfo(@PathVariable Integer userId) {

        UserInfoBean userInfoBean = userService.findUserById((long) userId);

        return  ok(userInfoBean);
    }

    @PutMapping("")
    public ResponseEntity updateUserInfo(@RequestBody @Valid UpdatedUserBean userInfoBean, @AuthenticationPrincipal UserDetails userDetails) {
        userService.updateUserInfo(userInfoBean, userDetails);
        return  ok("Successfully");
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity getUserInfo(@PathVariable Integer userId, @RequestParam Integer page) {
        List<PostBean> postBeans = postService.getPostByUserid(userId, page);
        Integer totalPage = postService.getTotalPageByUserId(userId);

        Map<Object, Object> model = new HashMap<>();
        model.put("posts", postBeans);
        model.put("totalPage", totalPage);

        return ok(model);
    }

}
