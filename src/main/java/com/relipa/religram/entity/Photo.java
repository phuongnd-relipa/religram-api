package com.relipa.religram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="photos")
public class Photo extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "photo_uri")
    private String photoUri;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public static final class PhotoBuilder {
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        private Long postId;
        private Long id;
        private String photoUri;

        private PhotoBuilder() {
        }

        public static PhotoBuilder builder() {
            return new PhotoBuilder();
        }

        public PhotoBuilder postId(Long postId) {
            this.postId = postId;
            return this;
        }

        public PhotoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PhotoBuilder photoUri(String photoUri) {
            this.photoUri = photoUri;
            return this;
        }

        public PhotoBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PhotoBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Photo build() {
            Photo photo = new Photo();
            photo.setId(id);
            photo.setCreatedAt(createdAt);
            photo.setUpdatedAt(updatedAt);
            photo.photoUri = this.photoUri;
            photo.postId = this.postId;
            return photo;
        }
    }
}
