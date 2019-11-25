/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="comments")
public class Comment extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column(length = 3000)
    private String comment;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hashtag_comment", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public static final class CommentBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long id;
        private Long userId;
        private Long postId;
        private Long parentId;
        private String comment;

        private CommentBuilder() {
        }

        public static CommentBuilder builder() {
            return new CommentBuilder();
        }

        public CommentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CommentBuilder parentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public CommentBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public CommentBuilder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public CommentBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public CommentBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CommentBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();
            comment.setId(id);
            comment.setParentId(parentId);
            comment.setUserId(userId);
            comment.setPostId(postId);
            comment.setComment(this.comment);
            comment.setCreatedAt(createdAt);
            comment.setUpdatedAt(updatedAt);
            return comment;
        }
    }
}
