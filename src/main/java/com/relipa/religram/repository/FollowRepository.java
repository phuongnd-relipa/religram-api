/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.Follow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends CrudRepository<Follow, Long> {

    Boolean existsByUserIdAndFollowingId(Long userId, Long followingId);

    Optional<Follow> findByUserIdAndFollowingId(Long userId, Long followingId);

    @Query(value = "select * from follows where following_id = :followingId", nativeQuery = true)
    List<Follow> findAllFollower(@Param("followingId") Long followingId);

    @Query(value = "select * from follows where user_id = :userId", nativeQuery = true)
    List<Follow> findAllFollowing(@Param("userId") Long userId);

    @Query(value = "select count(*) from follows where following_id = :followingId", nativeQuery = true)
    Integer countTotalFollower(@Param("followingId") Long followingId);

    @Query(value = "select count(*) from follows where user_id = :userId", nativeQuery = true)
    Integer countTotalFollowing(@Param("userId") Long userId);
}
