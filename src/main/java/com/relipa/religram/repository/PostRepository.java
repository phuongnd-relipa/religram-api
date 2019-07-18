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

    @Query(value = "select * from posts where user_id = :id", nativeQuery = true)
    List<Post> getPostsByUserId(@Param("id") Long id);

    @Query(value = "select * from posts limit :limit offset :offset", nativeQuery = true)
    List<Post> getPagePost(@Param("limit") Integer limit, @Param("offset") Integer offset);
}
