/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/hashtag")
public class HashtagController {

    @Autowired
    PostService postService;

    @GetMapping("/{hashtag}")
    public ResponseEntity comment(@PathVariable String hashtag, @RequestParam(required = false, defaultValue = "1") Integer page) {
        List<PostBean> postBeans = postService.getAllPostByHashtag(page, hashtag);
        Integer totalPage = postService.countPostByHashtag(hashtag);

        Map<Object, Object> model = new HashMap<>();
        model.put("posts", postBeans);
        model.put("totalPage", totalPage);

        return ok(model);
    }
}
