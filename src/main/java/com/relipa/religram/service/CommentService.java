package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.CommentRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.entity.Comment;
import com.relipa.religram.entity.Hashtag;

import java.util.List;
import java.util.Set;

public interface CommentService extends AbstractService<Comment,Long> {

    Integer getTotalPage(Long postId);

    List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer page);

    List<CommentBean> get3Comments(Long postId);

    CommentBean postComment(Long postId, CommentRequestBean commentRequestBean);

    Set<Hashtag> insertHashtags(List<String> hastagNames);
}
