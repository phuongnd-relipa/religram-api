/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtags")
public class Hashtag  extends AbstractAuditableEntity<Long> implements Serializable {

    @Column(unique = true)
    private String hashtag;

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(mappedBy = "hashtags", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
