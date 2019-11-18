/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.UserPostInfoBean;
import com.relipa.religram.service.PostService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/hashtag")
@Api(tags = {"hashTag"})
public class HashtagController {

    @Autowired
    PostService postService;

    @GetMapping("/{hashtag}")
    @ApiOperation(value = "${hashTag-comment.get.value}", notes = "${hashTag-comment.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UserPostInfoBean.class)})
    public ResponseEntity comment(@ApiParam(value = "${hashTag-comment.get.param.hashtag}") @PathVariable String hashtag,
                                  @ApiParam(value = "${hashTag-comment.get.param.page}") @RequestParam(required = false, defaultValue = "1") Integer page) {
        List<PostBean> postBeans = postService.getAllPostByHashtag(page, hashtag);
        Integer totalPage = postService.countPostByHashtag(hashtag);

        Map<Object, Object> model = new HashMap<>();
        model.put("posts", postBeans);
        model.put("totalPage", totalPage);

        return ok(model);
    }
}
