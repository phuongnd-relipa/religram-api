package com.relipa.religram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "following_id")
    private Long followingId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }


    public static final class FollowBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long id;
        private Long userId;
        private Long followingId;

        private FollowBuilder() {
        }

        public static FollowBuilder builder() {
            return new FollowBuilder();
        }

        public FollowBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FollowBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public FollowBuilder followingId(Long followingId) {
            this.followingId = followingId;
            return this;
        }

        public FollowBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FollowBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Follow build() {
            Follow follow = new Follow();
            follow.setId(id);
            follow.setUserId(userId);
            follow.setFollowingId(followingId);
            follow.setCreatedAt(createdAt);
            follow.setUpdatedAt(updatedAt);
            return follow;
        }
    }
}
