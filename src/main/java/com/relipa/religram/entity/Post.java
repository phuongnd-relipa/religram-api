package com.relipa.religram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column
    private String content;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "comment_count")
    private Integer commentCount;

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
