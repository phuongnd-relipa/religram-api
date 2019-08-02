/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.Like;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {

    Like getLikeByUserIdAndPostId(Long userId, Long postId);

    Integer countByUserIdAndPostId(Long userId, Long postId);
}
