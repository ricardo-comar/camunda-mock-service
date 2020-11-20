package com.github.ricardocomar.camunda.mockservice.handler.helper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TopicHelper {

    private final Set<String> topicsRegistred = Collections.synchronizedSet(new HashSet<String>());

    public synchronized final void registerTopic(String topic) {
        if (topicsRegistred.contains(topic)) {
            throw new RuntimeException("Topic already registred");
        }
        topicsRegistred.add(topic);
    }

    public synchronized final void removeTopic(String topic) {
        if (!topicsRegistred.contains(topic)) {
            throw new RuntimeException("Topic not registred");
        }
        topicsRegistred.remove(topic);
    }

    public synchronized final boolean isTopicRegistred(String topic) {
        return topicsRegistred.contains(topic);
    }



}
