/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.UpdateUserBean;
import com.relipa.religram.controller.bean.response.*;
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
    public ResponseEntity getUserInfoById(@ApiParam(value = "${user-info.get.param.id}", required = true) @PathVariable Integer userId) {
        UserInfoBean userInfoBean = userService.findUserById((long) userId);
        return ok(userInfoBean);
    }

    @GetMapping("/{name}/info")
    @ApiOperation(value = "${user-name.get.value}", notes = "${user-name.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserInfoBean.class)})
    public ResponseEntity getUserInfoByName(@ApiParam(value = "${user-name.get.param.name}", required = true) @PathVariable String name) {
        UserInfoBean userInfoBean = userService.findUserByUserName(name);
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

    @PostMapping("/{followId}/follow")
    @ApiOperation(value = "${user-follow.post.value}", notes = "${user-follow.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = FollowResponseBean.class)})
    public ResponseEntity followUser(@ApiParam(value = "${user-follow.post.param.follow}", required = true) @PathVariable Long followId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        userService.followUser(userDetails, followId);
        return ok(new FollowResponseBean(true));
    }

    @PostMapping("/{followId}/un-follow")
    @ApiOperation(value = "${user-un-follow.post.value}", notes = "${user-un-follow.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = FollowResponseBean.class)})
    public ResponseEntity unFollowUser(@ApiParam(value = "${user-un-follow.post.param.follow}", required = true) @PathVariable Long followId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        userService.unFollowUser(userDetails, followId);
        return ok(new FollowResponseBean(false));
    }

    @GetMapping("/{userId}/followers")
    @ApiOperation(value = "${followers.get.value}", notes = "${followers.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = FollowerUserInfoBean.class)})
    public ResponseEntity getFollowers(@ApiParam(value = "${followers.get.param.userId}", required = true) @PathVariable Long userId) throws Exception {
        List<UserInfoBean> userInfoBeans = userService.getFollowers(userId);
        FollowerUserInfoBean followerUserInfoBean = new FollowerUserInfoBean();
        followerUserInfoBean.setCount(userInfoBeans.size());
        followerUserInfoBean.setUserInfoBeans(userInfoBeans);
        return ok(followerUserInfoBean);
    }

    @GetMapping("/{userId}/followings")
    @ApiOperation(value = "${followings.get.value}", notes = "${followings.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = FollowerUserInfoBean.class)})
    public ResponseEntity getFollowings(@ApiParam(value = "${followings.get.param.userId}", required = true) @PathVariable Long userId) throws Exception {
        List<UserInfoBean> userInfoBeans = userService.getFollowings(userId);
        FollowerUserInfoBean followingUserInfoBean = new FollowerUserInfoBean();
        followingUserInfoBean.setCount(userInfoBeans.size());
        followingUserInfoBean.setUserInfoBeans(userInfoBeans);
        return ok(followingUserInfoBean);
    }
}
