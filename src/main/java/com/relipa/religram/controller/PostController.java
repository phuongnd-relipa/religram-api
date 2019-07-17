package com.relipa.religram.controller;

import com.relipa.religram.entity.Post;
import com.relipa.religram.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping("")
    public ResponseEntity list() {
        Map<Object, Object> model = new HashMap<>();
        model.put("test", "token");
        model.put("status", "OK");

        return ok(model);
    }
}
