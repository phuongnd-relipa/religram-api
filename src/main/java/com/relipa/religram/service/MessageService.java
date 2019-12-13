package com.relipa.religram.service;

import com.relipa.religram.controller.bean.response.ChatBean;
import com.relipa.religram.entity.Message;

import java.util.List;

public interface MessageService extends AbstractService<Message, Long> {
    List<Message> getConversation(Long userId1, Long userId2, Integer page);

    List<ChatBean> getChatList(Long userId);
}
