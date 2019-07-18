package com.relipa.religram.repository;

import com.relipa.religram.entity.Photo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long> {

    List<Photo> getPhotosByPostId(Long postId);
}
