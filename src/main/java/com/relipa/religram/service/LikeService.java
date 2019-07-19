package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.entity.Like;

public interface LikeService extends AbstractService<Like, Long> {

    LikeBean getLikeByPostIdAndUserId(Long postId, Long userId);
}
