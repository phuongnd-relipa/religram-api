/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.request.CommentRequestBean;
import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.request.PostRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.LikeStatusBean;
import com.relipa.religram.controller.bean.response.PostBean;
import com.relipa.religram.controller.bean.response.SwaggerEmptyModel;
import com.relipa.religram.service.CommentService;
import com.relipa.religram.service.LikeService;
import com.relipa.religram.service.PostService;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/post")
@Api(tags = {"post"})
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @GetMapping("")
    @ApiOperation(value = "${post-list.get.value}", notes = "${post-list.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = PostBean.class, responseContainer = "List")})
    public ResponseEntity list(@ApiParam(value = "${post-list.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page,
                               @AuthenticationPrincipal UserDetails userDetails) {

        List<PostBean> postList = postService.getAllPostByPage(page, userDetails);
        return getResponseEntity(postService.getTotalPage(), postList.toArray(), "posts");
    }

    @PostMapping("")
    @ApiOperation(value = "${posts.post.value}", notes = "${posts.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = SwaggerEmptyModel.class)})
    public ResponseEntity post(@ApiParam(value = "${posts.post.param.body}", required = true) @RequestBody @Valid PostRequestBean postRequestBean) {
        if (postService.createPost(postRequestBean)) {
            return ok("Successfully");
        } else {
            return badRequest().build();
        }

    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "${post-detail.get.value}", notes = "${post-detail.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = PostBean.class)})
    public ResponseEntity getPostDetail(@ApiParam(value = "${post-detail.get.param.postId}", required = true) @PathVariable Integer postId,
                                        @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {
        PostBean postBean = postService.getPostDetail(postId, userDetails);
        return ok(postBean);
    }

    @PostMapping("/{postId}/like")

    @ApiOperation(value = "${post-like.post.value}", notes = "${post-like.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = LikeStatusBean.class)})
    public ResponseEntity like(@ApiParam(value = "${post-like.post.param.postId}", required = true) @PathVariable Integer postId,
                               @RequestBody LikeBean likeBean) {

        LikeBean like = likeService.getLikeByPostIdAndUserId((long) postId, likeBean.getUserId());
        Integer likeCount = likeService.countLikeByUserIdAndPostId(likeBean.getUserId(), (long) postId);
        LikeStatusBean likeStatusBean;

        if (likeCount > 0) {
            likeBean.setId(like.getId());
            likeBean.setPostId((long) postId);
            likeStatusBean = likeService.unlikePost(likeBean);
        } else {
            likeStatusBean = likeService.likePost((long) postId, likeBean.getUserId());
        }

        return ok(likeStatusBean);
    }

    /*@GetMapping("{postId}/comment")
    @ApiOperation(value = "${post-comment.get.value}", notes = "${post-comment.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = CommentBean.class, responseContainer = "List")})
    public ResponseEntity comment(@ApiParam(value = "${post-comment.get.param.postId}", required = true) @PathVariable Integer postId,
                                  @ApiParam(value = "${post-comment.get.param.page}", required = true) @RequestParam Integer page) {
        List<CommentBean> commentBeans = commentService.getCommentsByPostIdAndPageNumber((long) postId, page);
        return getResponseEntity(commentService.getTotalPage((long) postId), commentBeans.toArray(), "comments");
    }*/

    @GetMapping("{postId}/comment")
    @ApiOperation(value = "${post-parent-comment.get.value}", notes = "${post-parent-comment.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = CommentBean.class, responseContainer = "List")})
    public ResponseEntity parentComment(@ApiParam(value = "${post-comment.get.param.postId}", required = true) @PathVariable Integer postId,
                                        @ApiParam(value = "${post-comment.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page) {
        List<CommentBean> commentBeans = commentService.getParentCommentsByPostIdAndPageNumber((long) postId, page);
        return getResponseEntity(commentService.getParentCommentTotalPage((long) postId), commentBeans.toArray(), "comments");
    }

    @GetMapping("{postId}/comment/{commentId}")
    @ApiOperation(value = "${post-sub-comment.get.value}", notes = "${post-sub-comment.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = CommentBean.class, responseContainer = "List")})
    public ResponseEntity subComment(@ApiParam(value = "${post-comment.get.param.postId}", required = true) @PathVariable Integer postId,
                                     @ApiParam(value = "${post-sub-comment.get.param.id}", required = true) @PathVariable Integer commentId,
                                     @ApiParam(value = "${post-comment.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page) {
        List<CommentBean> commentBeans = commentService.getSubComments((long) postId, (long) commentId, page);
        return getResponseEntity(commentService.getSubCommentTotalPage((long) postId, (long) commentId), commentBeans.toArray(), "comments");
    }

    private ResponseEntity getResponseEntity(Integer totalPage, Object[] objects, String listObjName) {

        Map<Object, Object> model = new HashMap<>();
        model.put(listObjName, objects);
        model.put("totalPage", totalPage);

        return ok(model);
    }

    @PostMapping("{postId}/comment")
    @ApiOperation(value = "${post-comment.post.value}", notes = "${post-comment.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = CommentBean.class)})
    public ResponseEntity postComment(@ApiParam(value = "${post-comment.post.param.postId}", required = true) @PathVariable Integer postId,
                                      @ApiParam(value = "${post-comment.post.param.body}", required = true) @RequestBody CommentRequestBean commentRequest) {
        CommentBean commentBean = commentService.postComment((long) postId, commentRequest);

        return ok(commentBean);
    }

    @PostMapping("{postId}/comment/{commentId}")
    @ApiOperation(value = "${post-sub-comment.post.value}", notes = "${post-sub-comment.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = CommentBean.class)})
    public ResponseEntity postSubComment(@ApiParam(value = "${post-comment.post.param.postId}", required = true) @PathVariable Integer postId,
                                         @ApiParam(value = "${post-comment.post.param.id}", required = true) @PathVariable Integer commentId,
                                         @ApiParam(value = "${post-comment.post.param.body}", required = true) @RequestBody CommentRequestBean commentRequest) {
        CommentBean commentBean = commentService.postSubComment((long) postId, (long) commentId, commentRequest);

        return ok(commentBean);
    }
}
