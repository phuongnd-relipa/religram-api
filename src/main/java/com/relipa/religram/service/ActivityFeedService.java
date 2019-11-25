package com.relipa.religram.service;

import com.relipa.religram.entity.ActivityFeed;
import com.relipa.religram.util.ActivityFeedUtils;

import java.util.List;

public interface ActivityFeedService {
    List<ActivityFeed> findAllFeedsByUserId(String userId, Integer page);

    void createNewFeed(Long actorId, ActivityFeedUtils.TYPE type, Long objectId, String objectInfo, Long targetId);
}
