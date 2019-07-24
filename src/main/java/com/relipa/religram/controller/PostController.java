package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.LikeStatusBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.service.CommentService;
import com.relipa.religram.service.LikeService;
import com.relipa.religram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @GetMapping("")
    public ResponseEntity list(@RequestParam Integer page, @AuthenticationPrincipal UserDetails userDetails) {

        List<PostBean> postList = postService.getAllPostByPage(page, userDetails);
        return getResponseEntity(postService.getTotalPage(), postList.toArray(), "posts");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity like(@PathVariable Integer postId, @RequestBody LikeBean likeBean) {

        LikeBean like = likeService.getLikeByPostIdAndUserId((long) postId, likeBean.getUserId());
        LikeStatusBean likeStatusBean;

        if (like != null) {
            likeBean.setId(like.getId());
            likeBean.setPostId((long) postId);
            likeStatusBean = likeService.unlikePost(likeBean);
        } else {
            likeStatusBean = likeService.likePost((long) postId, likeBean.getUserId());
        }

        return ok(likeStatusBean);
    }

    @GetMapping("{postId}/comment")
    public ResponseEntity comment(@PathVariable Integer postId, @RequestParam Integer page) {
        List<CommentBean> commentBeans = commentService.getCommentsByPostIdAndPageNumber((long) postId, page);
        return getResponseEntity(commentService.getTotalPage((long) postId), commentBeans.toArray(), "comments");
    }

    private ResponseEntity getResponseEntity(Integer totalPage, Object[] objects, String listObjName) {

        Map<Object, Object> model = new HashMap<>();
        model.put(listObjName, objects);
        model.put("totalPage", totalPage);

        return ok(model);
    }
}
