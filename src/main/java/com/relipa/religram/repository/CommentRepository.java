/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.repository;

import com.relipa.religram.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query(value = "select * from comments where post_id = :post_id order by created_at desc limit :limit", nativeQuery = true)
    List<Comment> getTheFirstsCommentByPostId(@Param("post_id") Long postId, @Param("limit") Integer limit);

    @Query(value = "select * from comments where post_id = :post_id order by created_at asc limit :limit", nativeQuery = true)
    List<Comment> getTheLastsCommentByPostId(@Param("post_id") Long postId, @Param("limit") Integer limit);

    @Query(value = "select * from comments where post_id = :post_id order by created_at desc limit :limit offset :offset", nativeQuery = true)
    List<Comment> getCommentsByPostIdAndPageNumber(@Param("post_id") Long postId, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Query(value = "select count(*) from comments where post_id = :post_id", nativeQuery = true)
    Integer countCommentsByPostId(@Param("post_id") Long postId);

}
