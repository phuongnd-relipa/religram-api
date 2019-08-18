/*
 * Copyright (c) 2019. Relipa Software - 株式会社レリパ
 */

package com.relipa.religram.util;

import com.relipa.religram.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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

        return Constant.PHOTOS_BASE_PATH + "/" + timestamp.getTime() + "." + getImageExtension(imageString);
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

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

}
