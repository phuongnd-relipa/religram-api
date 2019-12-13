package com.relipa.religram.service;

import com.relipa.religram.entity.User;
import com.relipa.religram.messages.websocket.ChatMessage;
import com.relipa.religram.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class ChatServiceImpl implements ChatService {

    @Inject
    UserRepository userRepository;

    @Override
    public ChatMessage sendDirectMessage(Long receiverId, UserDetails userDetails, String message) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("An error occured!"));
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(String.valueOf(user.getId()));
        chatMessage.setReceiver(String.valueOf(receiverId));
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setContent(message);
        return chatMessage;
    }
}