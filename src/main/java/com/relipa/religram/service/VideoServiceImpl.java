package com.relipa.religram.service;

import com.relipa.religram.entity.Video;
import com.relipa.religram.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
public class VideoServiceImpl extends AbstractServiceImpl<Video, Long> implements VideoService {

    @Inject
    VideoRepository videoRepository;

    @Override
    @Transactional
    public List<Video> findVideosByPostId(Long postId) {
        return videoRepository.findAllByPostId(postId);
    }
}
