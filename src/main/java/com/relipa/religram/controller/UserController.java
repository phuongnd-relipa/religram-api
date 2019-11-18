/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.UpdateUserBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UpdatedUserBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.controller.bean.response.UserPostInfoBean;
import com.relipa.religram.error.ErrorMessage;
import com.relipa.religram.service.PostService;
import com.relipa.religram.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/user")
@Api(tags = {"user"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    @ApiOperation(value = "${user-list.get.value}", notes = "${user-list.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserInfoBean.class, responseContainer = "List")})
    public ResponseEntity getUsers() {
        List<UserInfoBean> userInfoBeans = userService.findAllUsers();
        return ok(userInfoBeans);
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "${user-info.get.value}", notes = "${user-info.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserInfoBean.class)})
    public ResponseEntity getUserInfo(@ApiParam(value = "${user-info.get.param.id}", required = true) @PathVariable Integer userId) {
        UserInfoBean userInfoBean = userService.findUserById((long) userId);
        return ok(userInfoBean);
    }

    @PutMapping("")
    @ApiOperation(value = "${user-info.put.value}", notes = "${user-info.put.notes}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "response.message.200", response = UpdatedUserBean.class),
            @ApiResponse(code = 409, message = "response.error.user.exist", response = ErrorMessage.class)
    })
    public ResponseEntity updateUserInfo(@ApiParam(value = "${user-info.put.param.body}") @RequestBody @Valid UpdateUserBean userInfoBean,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        UpdatedUserBean updatedUserBean = userService.updateUserInfo(userInfoBean, userDetails);
        return ok(updatedUserBean);
    }

    @GetMapping("/{userId}/posts")
    @ApiOperation(value = "${user-post.get.value}", notes = "${user-post.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserPostInfoBean.class)})
    public ResponseEntity getUserPostInfo(@ApiParam(value = "${user-post.get.param.userId}", required = true) @PathVariable Integer userId,
                                          @ApiParam(value = "${user-post.get.param.page}", required = true) @RequestParam Integer page) {
        List<PostBean> postBeans = postService.getPostByUserid(userId, page);
        Integer totalPage = postService.getTotalPageByUserId(userId);

        /*Map<Object, Object> model = new HashMap<>();
        model.put("posts", postBeans);
        model.put("totalPage", totalPage);*/

        UserPostInfoBean model = new UserPostInfoBean();
        model.setPosts(postBeans);
        model.setTotalPage(totalPage);

        return ok(model);
    }

}
