/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="posts")
public class Post extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(length = 3000)
    private String content;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hashtag_post", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Set<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public static final class PostBuilder {
        private Post post;

        private PostBuilder() {
            post = new Post();
        }

        public static PostBuilder builder() {
            return new PostBuilder();
        }

        public PostBuilder id(Long id) {
            post.setId(id);
            return this;
        }

        public PostBuilder userId(Integer userId) {
            post.setUserId(userId);
            return this;
        }

        public PostBuilder content(String content) {
            post.setContent(content);
            return this;
        }

        public PostBuilder createdAt(LocalDateTime createdAt) {
            post.setCreatedAt(createdAt);
            return this;
        }

        public PostBuilder updatedAt(LocalDateTime updatedAt) {
            post.setUpdatedAt(updatedAt);
            return this;
        }

        public PostBuilder likeCount(Integer likeCount) {
            post.setLikeCount(likeCount);
            return this;
        }

        public PostBuilder commentCount(Integer commentCount) {
            post.setCommentCount(commentCount);
            return this;
        }

        public Post build() {
            return post;
        }
    }
}
