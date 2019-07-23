package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.entity.Comment;

import java.util.List;

public interface CommentService extends AbstractService<Comment,Long> {

    Integer getTotalPage(Long postId);

    List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer page);

    List<CommentBean> get3Comments(Long postId);

    CommentBean postComment(Long postId, Long userId, String comment);
}
