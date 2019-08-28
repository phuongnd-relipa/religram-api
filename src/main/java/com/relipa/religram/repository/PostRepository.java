/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "select * from posts where user_id = :id order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<Post> getPostsByUserId(@Param("id") Long id, @Param("limit") Integer limit, @Param("offset") Integer offset);

    Integer countByUserId(Integer userId);

    @Query(value = "select * from posts order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<Post> getPagePost(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "select posts.* from posts, hashtag_post, hashtags where hashtags.hashtag like %:hashtag% " +
            "and posts.id = hashtag_post.post_id " +
            "and hashtags.id = hashtag_post.hashtag_id " +
            "order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<Post> searchByHashtag(@Param("hashtag") String hashtag, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "select count(*) from posts, hashtag_post, hashtags where hashtags.hashtag like %:hashtag% " +
            "and posts.id = hashtag_post.post_id " +
            "and hashtags.id = hashtag_post.hashtag_id ", nativeQuery = true)
    Integer countAllByHashtag(@Param("hashtag") String hashtag);
}
