/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="videos")
public class Video extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "video_uri")
    private String videoUri;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }

    public static final class VideoBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long postId;
        private Long id;
        private String videoUri;

        private VideoBuilder() {
        }

        public static VideoBuilder builder() {
            return new VideoBuilder();
        }

        public VideoBuilder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public VideoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public VideoBuilder videoUri(String videoUri) {
            this.videoUri = videoUri;
            return this;
        }

        public VideoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public VideoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Video build() {
            Video video = new Video();
            video.setId(id);
            video.setCreatedAt(createdAt);
            video.setUpdatedAt(updatedAt);
            video.videoUri = this.videoUri;
            video.postId = this.postId;
            return video;
        }
    }
}
