package com.relipa.religram.service;


import com.relipa.religram.controller.bean.response.ChatBean;
import com.relipa.religram.entity.Message;
import com.relipa.religram.entity.User;
import com.relipa.religram.repository.MessageRepository;
import com.relipa.religram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl extends AbstractServiceImpl<Message, Long> implements MessageService {

    private static final int MESSAGES_PER_PAGE = 10;

    @Inject
    MessageRepository messageRepository;

    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    public List<Message> getConversation(Long userId1, Long userId2, Integer page) {
        Optional<User> user1 = userRepository.findById(userId1);
        if (!user1.isPresent()) {
            throw new EntityNotFoundException("Can not find userId: " + userId1);
        }
        Optional<User> user2 = userRepository.findById(userId2);
        if (!user2.isPresent()) {
            throw new EntityNotFoundException("Can not find userId: " + userId2);
        }

        return messageRepository.getConversation(userId1, userId2, MESSAGES_PER_PAGE, (page - 1) * MESSAGES_PER_PAGE);
    }

    @Override
    @Transactional
    public List<ChatBean> getChatList(Long userId) {
        Optional<User> user1 = userRepository.findById(userId);
        if (!user1.isPresent()) {
            throw new EntityNotFoundException("Can not find userId: " + userId);
        }

        List<Message> messages = messageRepository.getChatList(userId);
        List<ChatBean> results = new ArrayList<>();
        for (Message message : messages) {
            if (message.getFromId().equals(userId) && !existChatUser(message.getToId(), results)) {
                addChatBean(message, results, message.getToId());
            } else if (message.getToId().equals(userId) && !existChatUser(message.getFromId(), results)) {
                addChatBean(message, results, message.getFromId());
            }
        }
        return results;
    }

    private void addChatBean(Message message, List<ChatBean> results, Long chatUserId) {
        ChatBean chatBean = new ChatBean();
        chatBean.setId(message.getId());
        chatBean.setChatUserId(chatUserId);
        Optional<User> user = userRepository.findById(chatUserId);
        user.ifPresent(value -> chatBean.setChatUserName(value.getUsername()));
        chatBean.setLastMessage(message.getContent());
        chatBean.setLastMessageFrom(message.getFromId());
        chatBean.setCreatedAt(message.getCreatedAt());
        chatBean.setUpdatedAt(message.getUpdatedAt());
        results.add(chatBean);
    }

    private boolean existChatUser(Long userId, List<ChatBean> chatBeans) {
        for (ChatBean chatBean : chatBeans) {
            if (chatBean.getChatUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }
}
