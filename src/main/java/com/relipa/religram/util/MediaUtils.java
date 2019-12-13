package com.relipa.religram.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {

    private static final String FILE_DELIMITER = "\\.";
    private static final String TYPE_PICTURE = "picture";
    private static final String TYPE_VIDEO = "video";

    public static String getFormatTimestamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date now = new Date();
        return sdfDate.format(now);
    }

    public static String getMediaType(String fileName) {
        String mediaType = null;
        String[] splits = fileName.split(FILE_DELIMITER);
        if (splits.length > 1) {
            switch (splits[splits.length - 1].toUpperCase()) {
                case "PNG":
                case "JPEG":
                case "JPG":
                case "GIF":
                    mediaType = TYPE_PICTURE;
                    break;
                case "MP4":
                case "WMV":
                case "AVI":
                case "OGG":
                    mediaType = TYPE_VIDEO;
                    break;
                default:
                    break;
            }
        }
        return mediaType;
    }
}
