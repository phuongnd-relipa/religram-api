/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.service;

import com.relipa.religram.controller.bean.request.LikeBean;
import com.relipa.religram.controller.bean.response.LikeStatusBean;
import com.relipa.religram.entity.Like;

public interface LikeService extends AbstractService<Like, Long> {

    LikeBean getLikeByPostIdAndUserId(Long postId, Long userId);

    LikeStatusBean likePost(Long postId, Long userId);
    LikeStatusBean unlikePost(LikeBean likeBean);
}
