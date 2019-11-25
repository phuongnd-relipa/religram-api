package com.relipa.religram.controller;

import com.relipa.religram.entity.ActivityFeed;
import com.relipa.religram.service.ActivityFeedService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/activity-feed")
@Api(tags = {"activity"})
@Slf4j
public class ActivityFeedController {

    @Autowired
    ActivityFeedService activityFeedService;

    @GetMapping("/{userId}/feed")
    @ApiOperation(value = "${activity-user.feed.get.value}", notes = "${activity-user.feed.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = ActivityFeed.class, responseContainer = "List")})
    public ResponseEntity<List<ActivityFeed>> userFeeds(@ApiParam(value = "${activity-user.feed.get.param.userId}", required = true) @PathVariable String userId,
                                                        @ApiParam(value = "${activity-user.feed.get.param.page}", defaultValue = "1", required = true) @RequestParam Integer page) {
        List<ActivityFeed> userFeeds = activityFeedService.findAllFeedsByUserId(userId, page);
        return ResponseEntity.ok(userFeeds);
    }
}
