/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

public class ImageUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ImageUtils.class);

    public static void decodeToImage(String imageString, String outputFileName) {

        String imageInfo = imageString.split(",")[1];

        try (FileOutputStream imageOutFile = new FileOutputStream(outputFileName)) {
            // Converting a Base64 String into Image byte array
            byte[] imageByteArray = Base64.getMimeDecoder().decode(imageInfo);
            imageOutFile.write(imageByteArray);
        } catch (FileNotFoundException e) {
            LOG.error("Image not found" + e);
        } catch (IOException ioe) {
            LOG.error("Exception while reading the Image " + ioe);
        }

    }

    /**
     *
     * @return string is timastamp
     */
    public static String getImageFileName(String imageString) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp.getTime() + "." + getImageExtension(imageString);
    }

    private static String getImageExtension(String imageString) {

        String metaString = imageString.split(",")[0];
        String extension;
        switch (metaString) {//check image's extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default://should write cases for more images types
                extension = "jpg";
                break;
        }

        return extension;
    }

}
