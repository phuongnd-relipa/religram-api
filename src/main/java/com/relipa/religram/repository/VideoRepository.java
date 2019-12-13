package com.relipa.religram.repository;

import com.relipa.religram.entity.Video;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoRepository extends CrudRepository<Video, Long> {
    List<Video> findAllByPostId(Long postId);
}
