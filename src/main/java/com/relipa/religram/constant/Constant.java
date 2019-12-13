/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.constant;

public final class Constant {

    public static final String USERNAME_PATTERN = "^[a-z0-9]+$";
    public static final String EMAIL_PATTERN = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,6}))?$";
    public static final String IMAGE_BASE64_PATTERN = "^data:image\\/(jpg|png|jpeg);base64,([^\\\"]*)$";

    public static final String UPLOAD_DIRECTORY = "upload-dir";
    public static final String DIRECTORY_DEFAULT_SLASH = "/";
    public static final String PHOTOS_BASE_PATH = "photos";
    public static final String DEFAULT_IMAGE_EXT = "png";

    public static final String ACTIVITY_FEED = "ActivityFeed";
    public static final String USER_FEED = "UserFeed";
    public static final String LIKE = "like";
    public static final String FOLLOW = "follow";
    public static final String COMMENT = "comment";
    public static final String TAG = "tag";
    public static final String MENTION = "mention";

    public static final String POST = "post";
    public static final String USER = "user";

    public static final int MAXIMUM_FEED_PER_USER = 100;
    public static final int FEED_PER_PAGE = 10;
}
