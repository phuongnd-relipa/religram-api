package com.relipa.religram.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("ActivityFeed")
@Getter
@Setter
@Builder
@ToString
public class ActivityFeed implements Serializable {
    private String id;
    private String actor;
    private String verb;
    private String object;
    private String objectInfo;
    private String target;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
}
