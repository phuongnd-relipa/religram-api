package com.relipa.religram.service;

import com.relipa.religram.messages.websocket.ChatMessage;
import org.springframework.security.core.userdetails.UserDetails;

public interface ChatService {
    ChatMessage sendDirectMessage(Long receiverId, UserDetails userDetails, String message);
}
