package com.relipa.religram.util;

import com.relipa.religram.constant.Constant;
import com.relipa.religram.entity.ActivityFeed;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

public class ActivityFeedUtils {

    public enum TYPE {
        LIKE,
        FOLLOW,
        COMMENT,
        TAG,
        MENTION
    }

    private static Jedis jedis;

    public static Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis();
        }

        return jedis;
    }

    private static Map<TYPE, String> types;

    private static Map<TYPE, String> getTypesInstance() {
        if (types == null) {
            types = new EnumMap<>(TYPE.class);
            types.put(TYPE.LIKE, Constant.LIKE);
            types.put(TYPE.FOLLOW, Constant.FOLLOW);
            types.put(TYPE.COMMENT, Constant.COMMENT);
            types.put(TYPE.TAG, Constant.TAG);
            types.put(TYPE.MENTION, Constant.MENTION);
        }

        return types;
    }

    public static String encodeId(String originId) {
        return Base64.getEncoder().encodeToString(originId.getBytes());
    }

    public static String decodeId(String encodeId) {
        return new String(Base64.getDecoder().decode(encodeId));
    }

    public static ActivityFeed newFeed(Long actorId, TYPE type, Long objectId, String objectInfo, Long targetId) {
        String verb = getTypesInstance().get(type);
        LocalDateTime createdAt = LocalDateTime.now();
        String originId = actorId + "." + verb + "." + objectId + "." + targetId + "." +
                createdAt.format(DateTimeFormatter.ISO_DATE_TIME);

        return ActivityFeed.builder()
                .id(encodeId(originId))
                .actor(String.valueOf(actorId))
                .verb(verb)
                .object(String.valueOf(objectId))
                .objectInfo(objectInfo)
                .target(String.valueOf(targetId))
                .createdAt(createdAt)
                .publishedAt(createdAt)
                .build();
    }

    public static ActivityFeed getFeed(Map<String, String> target) {
        if (target == null) return null;

        return ActivityFeed.builder()
                .id(target.get("id"))
                .actor(target.get("actor"))
                .verb(target.get("verb"))
                .object(target.get("object"))
                .objectInfo(target.get("objectInfo"))
                .target(target.get("target"))
                .createdAt(LocalDateTime.parse(target.get("createdAt"), DateTimeFormatter.ISO_DATE_TIME))
                .publishedAt(LocalDateTime.parse(target.get("publishedAt"), DateTimeFormatter.ISO_DATE_TIME))
                .build();
    }
}
