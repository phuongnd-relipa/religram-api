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

@Service
public class CommentServiceImpl extends AbstractServiceImpl<Comment, Long> implements CommentService {

    private static final int COMMENT_PER_PAGE = 10;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentBean> getCommentsByPostIdAndPageNumber(Long postId, Integer page) {
        List<CommentBean> commentBeans = new ArrayList<>();
        List<Comment> comments;

        comments = commentRepository.getCommentsByPostIdAndPageNumber(postId, COMMENT_PER_PAGE, (page - 1) * COMMENT_PER_PAGE);
        return getCommentBeans(commentBeans, comments);
    }


    @Override
    @Transactional
    public Integer getTotalPage(Long postId) {
        long totalPost = commentRepository.countCommentsByPostId(postId);
        return (int) totalPost / COMMENT_PER_PAGE + 1;

    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentBean> get3Comments(Long postId) {

        List<CommentBean> commentBeans = new ArrayList<>();
        List<Comment> comments;

        int countCount = commentRepository.countCommentsByPostId(postId);

        if (countCount <= 3) {
            comments = commentRepository.getTheFirstsCommentByPostId(postId, 3);
        } else {
            comments = this.get3CommentsForPostList(postId);
        }

        return getCommentBeans(commentBeans, comments);
    }

    @Override
    @Transactional
    public CommentBean postComment(Long postId, Long userId, String content) {
        Comment comment = Comment.CommentBuilder.builder()
                .postId(postId)
                .userId(userId)
                .comment(content)
                .build();
        comment = commentRepository.save(comment);
        CommentBean commentBean = new CommentBean();
        this.getCommentBean(commentBean, comment);

        return commentBean;
    }

    private List<Comment> get3CommentsForPostList(Long postId) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> theFirstComment = commentRepository.getTheFirstsCommentByPostId(postId, 1);
        List<Comment> theLastsComment = commentRepository.getTheLastsCommentByPostId(postId, 2);
        Collections.reverse(theLastsComment);

        theFirstComment.forEach(comment -> comments.add(comment));
        theLastsComment.forEach(comment -> comments.add(comment));

        return comments;
    }

    private List<CommentBean> getCommentBeans(List<CommentBean> commentBeans, List<Comment> comments) {
        comments.forEach(comment -> {
            CommentBean commentBean = new CommentBean();
            commentBean = this.getCommentBean(commentBean, comment);

            commentBeans.add(commentBean);
        });

        return commentBeans;
    }

    private CommentBean getCommentBean(CommentBean commentBean, Comment comment) {
        BeanUtils.copyProperties(comment, commentBean);
        commentBean.setId(comment.getId());

        User user = userRepository.findById(comment.getUserId()).orElseThrow(() -> new UsernameNotFoundException("An error occured!"));
        UserInfoBean userInfoBean = new UserInfoBean();
        BeanUtils.copyProperties(user, userInfoBean);
        userInfoBean.setId(user.getId());
        commentBean.setUser(userInfoBean);

        return commentBean;
    }
}
