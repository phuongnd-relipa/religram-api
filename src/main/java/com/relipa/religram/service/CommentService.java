/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.CommentRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.entity.Comment;
import com.relipa.religram.entity.Hashtag;

import java.util.List;
import java.util.Set;

public interface CommentService extends AbstractService<Comment, Long> {

    Integer getTotalPage(Long postId);

    Integer getParentCommentTotalPage(Long postId);

    Integer getSubCommentTotalPage(Long postId, Long commentId);

    List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer page);

    List<CommentBean> getParentCommentsByPostIdAndPageNumber(Long postId, Integer page);

    List<CommentBean> getSubComments(Long postId, Long commentId, Integer page);

    List<CommentBean> get3Comments(Long postId);

    CommentBean postComment(Long postId, CommentRequestBean commentRequestBean);

    CommentBean postSubComment(Long postId, Long commentId, CommentRequestBean commentRequestBean);

    Set<Hashtag> insertHashtags(List<String> hastagNames);
}
