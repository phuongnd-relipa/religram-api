package com.relipa.religram.service;

import com.relipa.religram.constant.Constant;
import com.relipa.religram.entity.ActivityFeed;
import com.relipa.religram.exceptionhandler.PageInvalidException;
import com.relipa.religram.exceptionhandler.UserFeedNotFoundException;
import com.relipa.religram.repository.ActivityFeedRepository;
import com.relipa.religram.util.ActivityFeedUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityFeedServiceImpl implements ActivityFeedService {

    @Inject
    private ActivityFeedRepository activityFeedRepository;

    @Override
    public List<ActivityFeed> findAllFeedsByUserId(String userId, Integer page) {
        if (page < 1) {
            throw new PageInvalidException("Parameter 'page' is not valid!!!");
        }

        String userKey = ActivityFeedUtils.encodeId(Constant.USER_FEED + userId);
        boolean userKeyExist = ActivityFeedUtils.getJedis().exists(userKey);
        if (!userKeyExist) {
            throw new UserFeedNotFoundException("Can not find this user!!!");
        }

        List<ActivityFeed> activityFeeds = new ArrayList<>();
        List<String> activityIds = ActivityFeedUtils.getJedis().lrange(userKey, (page - 1) * Constant.FEED_PER_PAGE, page * Constant.FEED_PER_PAGE - 1);
        for (String activityId : activityIds) {
            String activityKey = Constant.ACTIVITY_FEED + ":" + activityId;
            boolean activityExist = ActivityFeedUtils.getJedis().exists(activityKey);
            if (activityExist) {
                ActivityFeed activityFeed = ActivityFeedUtils.getFeed(ActivityFeedUtils.getJedis().hgetAll(activityKey));
                activityFeeds.add(activityFeed);
            }
        }

        return activityFeeds;
    }

    @Override
    public void createNewFeed(Long actorId, ActivityFeedUtils.TYPE type, Long objectId, String objectInfo, Long targetId) {
        ActivityFeed activityFeed = ActivityFeedUtils.newFeed(actorId, type, objectId, objectInfo, targetId);
        activityFeedRepository.save(activityFeed);

        String userKey = ActivityFeedUtils.encodeId(Constant.USER_FEED + targetId);
        ActivityFeedUtils.getJedis().lpush(userKey, activityFeed.getId());
        // Ensure number of feeds will not exceed MAXIMUM_FEED_PER_USER
        ActivityFeedUtils.getJedis().ltrim(userKey, 0, (long) Constant.MAXIMUM_FEED_PER_USER - 1);
    }
}
