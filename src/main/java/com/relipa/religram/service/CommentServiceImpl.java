package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.Comment;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.CommentRepository;
import com.relipa.religram.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl extends AbstractServiceImpl<Comment, Long> implements CommentService {

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    public List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer limit, Integer offset) {
        return null;
    }

    @Override
    @Transactional
    public List<CommentBean> get3Comments(Long postId) {

        List<CommentBean> commentBeans = new ArrayList<>();
        List<Comment> comments;

        int countCount = commentRepository.countCommentsByPostId(postId);

        if (countCount <= 3) {
            comments = commentRepository.getTheFirstsCommentByPostId(postId, 3);
        } else {
            comments = this.get3CommentsForPostList(postId);
        }

        comments.forEach(comment -> {
            CommentBean commentBean = new CommentBean();
            BeanUtils.copyProperties(comment, commentBean);

            User user = userRepository.findById(comment.getUserId()).orElseThrow(() -> new UsernameNotFoundException("An error occured!"));
            UserInfoBean userInfoBean = new UserInfoBean();
            BeanUtils.copyProperties(user, userInfoBean);
            commentBean.setUser(userInfoBean);

            commentBeans.add(commentBean);
        });

        return commentBeans;
    }

    private List<Comment> get3CommentsForPostList(Long postId) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> theFirstComment = commentRepository.getTheFirstsCommentByPostId(postId, 1);
        List<Comment> theLastsComment = commentRepository.getTheLastsCommentByPostId(postId, 2);
        Collections.reverse(theLastsComment);

        theFirstComment.forEach(comment -> {
            comments.add(comment);
        });
        theLastsComment.forEach(comment -> {
            comments.add(comment);
        });

        return comments;
    }

}
