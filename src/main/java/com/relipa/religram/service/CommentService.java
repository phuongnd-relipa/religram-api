package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.entity.Comment;

import java.util.List;

public interface CommentService extends AbstractService<Comment,Long> {

    List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer limit, Integer offset);

    List<CommentBean> get3Comments(Long postId);
}
