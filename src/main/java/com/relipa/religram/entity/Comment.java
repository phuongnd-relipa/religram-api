package com.relipa.religram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="comments")
public class Comment extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_id")
    private Long postId;

    @Column
    private String comment;

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


    public static final class CommentBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long id;
        private Long userId;
        private Long postId;
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
            comment.setUserId(userId);
            comment.setPostId(postId);
            comment.setComment(this.comment);
            comment.setCreatedAt(createdAt);
            comment.setUpdatedAt(updatedAt);
            return comment;
        }
    }
}
