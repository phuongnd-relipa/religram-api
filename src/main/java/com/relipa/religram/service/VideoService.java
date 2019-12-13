package com.relipa.religram.service;

import com.relipa.religram.entity.Video;

import java.util.List;

public interface VideoService extends AbstractService<Video, Long> {
    List<Video> findVideosByPostId(Long postId);
}
