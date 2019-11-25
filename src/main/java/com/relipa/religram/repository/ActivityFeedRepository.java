package com.relipa.religram.repository;

import com.relipa.religram.entity.ActivityFeed;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityFeedRepository extends CrudRepository<ActivityFeed, String> {
}