package com.github.ricardocomar.camunda.mockservice.handler.helper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;

public class TopicHelperTest {

    private TopicHelper topicHelper;
    private final String topicName = "mockTopic";

    @Before
    public void before() {
        topicHelper = new TopicHelper();
    }

    @Test
    public void testValidRegisterTopic() {

        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(false));

        topicHelper.registerTopic(topicName);
        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(true));

        topicHelper.removeTopic(topicName);
        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(false));
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRegisterTopic() {

        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(false));
        topicHelper.registerTopic(topicName);
        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(true));

        topicHelper.registerTopic(topicName);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidRemoveTopic() {

        assertThat(topicHelper.isTopicRegistred(topicName), equalTo(false));
        topicHelper.removeTopic(topicName);
    }
}
