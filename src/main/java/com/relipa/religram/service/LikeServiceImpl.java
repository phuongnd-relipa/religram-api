/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.constant.Constant;
import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.response.LikeStatusBean;
import com.relipa.religram.entity.Like;
import com.relipa.religram.entity.Post;
import com.relipa.religram.repository.LikeRepository;
import com.relipa.religram.repository.PostRepository;
import com.relipa.religram.util.ActivityFeedUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

@Service
public class LikeServiceImpl extends AbstractServiceImpl<Like, Long> implements LikeService {

    @Inject
    private LikeRepository likeRepository;

    @Inject
    private PostRepository postRepository;

    @Inject
    private ActivityFeedService activityFeedService;

    @Override
    @Transactional(readOnly = true)
    public LikeBean getLikeByPostIdAndUserId(Long postId, Long userId) {
        LikeBean likeBean = new LikeBean();
        Like like = likeRepository.getLikeByUserIdAndPostId(userId, postId);
        if (like != null) {
            BeanUtils.copyProperties(like, likeBean);
            likeBean.setId(like.getId());
            return likeBean;
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countLikeByUserIdAndPostId(Long userId, Long postId) {
        return likeRepository.countByUserIdAndPostId(userId, postId);
    }

    @Override
    @Transactional
    public LikeStatusBean likePost(Long postId, Long userId) {
        Like like = Like.LikeBuilder.builder()
                .postId(postId)
                .userId(userId)
                .build();
        likeRepository.save(like);

        LikeStatusBean likeStatusBean = new LikeStatusBean();
        likeStatusBean.setIsLiked(true);

        // Update comment count of Post
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Not found the Post"));
        post.setLikeCount(post.getLikeCount() + 1);

        // Create new activity feed
        activityFeedService.createNewFeed(userId, ActivityFeedUtils.TYPE.LIKE, postId, Constant.POST, (long) post.getUserId());

        return likeStatusBean;
    }

    @Override
    @Transactional
    public LikeStatusBean unlikePost(LikeBean likeBean) {
        Like like = new Like();
        BeanUtils.copyProperties(likeBean, like);
        like.setId(likeBean.getId());

        likeRepository.delete(like);

        LikeStatusBean likeStatusBean = new LikeStatusBean();
        likeStatusBean.setIsLiked(false);

        // Update like count of Post
        Post post = postRepository.findById(likeBean.getPostId()).orElseThrow(() -> new EntityNotFoundException("Not found the Post"));
        post.setLikeCount(post.getLikeCount() - 1);

        return likeStatusBean;
    }
}
