package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.repository.PostRepository;
import com.relipa.religram.repository.UserRepository;
import com.relipa.religram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/post")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity list(@RequestParam Integer page, @AuthenticationPrincipal UserDetails userDetails) {

        List<PostBean> postList = postService.getAllPostByPage(page, userDetails);
        int totalPage = postService.getTotalPage();

        Map<Object, Object> model = new HashMap<>();
        model.put("posts", postList.toArray());
        model.put("totalPage", totalPage);

        return ok(model);
    }
}
