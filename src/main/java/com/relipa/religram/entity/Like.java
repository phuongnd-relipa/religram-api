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
@Table(name="likes")
public class Like extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }


    public static final class LikeBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long id;
        private Long userId;
        private Long postId;

        private LikeBuilder() {
        }

        public static LikeBuilder builder() {
            return new LikeBuilder();
        }

        public LikeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LikeBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public LikeBuilder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public LikeBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public LikeBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Like build() {
            Like like = new Like();
            like.setId(id);
            like.setUserId(userId);
            like.setPostId(postId);
            like.setCreatedAt(createdAt);
            like.setUpdatedAt(updatedAt);
            return like;
        }
    }
}
