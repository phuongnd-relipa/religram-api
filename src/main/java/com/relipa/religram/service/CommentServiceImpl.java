package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.CommentRequestBean;
import com.relipa.religram.controller.bean.response.CommentBean;
import com.relipa.religram.controller.bean.response.UserInfoBean;
import com.relipa.religram.entity.Comment;
import com.relipa.religram.entity.Hashtag;
import com.relipa.religram.entity.Post;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.CommentRepository;
import com.relipa.religram.repository.PostRepository;
import com.relipa.religram.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class CommentServiceImpl extends AbstractServiceImpl<Comment, Long> implements CommentService {

    private static final int COMMENT_PER_PAGE = 10;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;

    @Inject HashtagService hashtagService;

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
    public CommentBean postComment(Long postId, CommentRequestBean commentRequestBean) {

        Set<Hashtag> hashtagSet = insertHashtags(commentRequestBean.getHashtags());

        Comment comment = Comment.CommentBuilder.builder()
                .postId(postId)
                .userId((long) commentRequestBean.getUserId())
                .comment(commentRequestBean.getComment())
                .build();
        comment.setHashtags(hashtagSet);
        comment = commentRepository.save(comment);

        // Update comment count of Post
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Not found th Post"));
        if (post != null) {
            post.setCommentCount(post.getCommentCount() + 1);
        }

        CommentBean commentBean = new CommentBean();
        this.getCommentBean(commentBean, comment);

        return commentBean;
    }

    @Override
    @Transactional
    public Set<Hashtag> insertHashtags(List<String> hashtagNames) {

        Set<Hashtag> hashtagSet = new HashSet<>();
        hashtagNames.forEach(hashtagName -> {
            if (hashtagService.existHashTagByName(hashtagName)) {
                Hashtag tag = hashtagService.findByHashtag(hashtagName);
                hashtagSet.add(tag);

            } else {
                Hashtag tag = new Hashtag();
                tag.setHashtag(hashtagName);

                hashtagService.save(tag);
                hashtagSet.add(tag);
            }
        });

        return hashtagSet;
    }

    private List<Comment> get3CommentsForPostList(Long postId) {
        List<Comment> comments = new ArrayList<>();
        List<Comment> theFirstComment = commentRepository.getTheFirstsCommentByPostId(postId, 1);
        List<Comment> theLastsComment = commentRepository.getTheLastsCommentByPostId(postId, 2);
        Collections.reverse(theLastsComment);

        comments.addAll(theFirstComment);
        comments.addAll(theLastsComment);

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
