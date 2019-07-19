package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
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
    @Transactional
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
}
