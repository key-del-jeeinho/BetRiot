package com.xylope.betriot.layer.discord;

@FunctionalInterface
public interface MessageIdCallback {
    void doSomething(long messageId);
}
