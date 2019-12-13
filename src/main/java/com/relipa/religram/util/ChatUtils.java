package com.relipa.religram.util;

import java.util.Base64;

public class ChatUtils {
    public static String getUserIdentity(String userId) {
        return Base64.getEncoder().encodeToString(userId.getBytes());
    }
}
