package com.relipa.religram.repository;

import com.relipa.religram.entity.Like;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {

    Like getLikeByUserIdAndPostId(Long userId, Long postId);
}
