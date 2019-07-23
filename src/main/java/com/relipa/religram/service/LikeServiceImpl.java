package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.response.LikeStatusBean;
import com.relipa.religram.entity.Like;
import com.relipa.religram.repository.LikeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class LikeServiceImpl extends AbstractServiceImpl<Like, Long> implements LikeService {

    @Inject
    private LikeRepository likeRepository;

    @Override
    @Transactional(readOnly = true)
    public LikeBean getLikeByPostIdAndUserId(Long postId, Long userId) {
        LikeBean likeBean = new LikeBean();
        Like like = likeRepository.getLikeByUserIdAndPostId(userId,postId);
        if (like != null) {
            BeanUtils.copyProperties(like, likeBean);
            likeBean.setId(like.getId());
            return likeBean;
        }

        return null;
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
        return likeStatusBean;
    }

    @Override

    public LikeStatusBean unlikePost(LikeBean likeBean) {
        Like like = new Like();
        BeanUtils.copyProperties(like, likeBean);
        like.setId(likeBean.getId());

        likeRepository.delete(like);

        LikeStatusBean likeStatusBean = new LikeStatusBean();
        likeStatusBean.setIsLiked(false);
        return likeStatusBean;
    }
}
