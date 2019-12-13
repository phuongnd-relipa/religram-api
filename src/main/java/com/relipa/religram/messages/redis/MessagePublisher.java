package com.relipa.religram.messages.redis;

public interface MessagePublisher {
    void publish(String message);
}
