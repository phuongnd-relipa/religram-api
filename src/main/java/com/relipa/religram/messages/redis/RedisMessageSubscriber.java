package com.relipa.religram.messages.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    private static List<String> messageList = new ArrayList<>();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        messageList.add(message.toString());
        log.info("Message received: " + message.toString());
    }
}
