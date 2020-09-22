package com.github.ricardocomar.springbootcamunda.mockservice.usecase;

import com.github.ricardocomar.springbootcamunda.mockservice.handler.MockServiceHandler;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RegisterOnTopicUseCase {


    @Value("${camunda.engine.url}")
    private String engineUrl;

    @Autowired
    private MockServiceHandler handler;

    public void registerTopic(String topic) {

        ExternalTaskClient client = ExternalTaskClient.create().baseUrl(engineUrl)
                .backoffStrategy(new ExponentialBackoffStrategy(1000, 50, 3000)).workerId(topic + "Worker")
                .asyncResponseTimeout(1000).build();

        client.subscribe(topic).handler(handler).open();
        handler.registerTopic(topic);
    }

    public Boolean isTopicRegistred(String topic) {
        return handler.isTopicRegistred(topic);
    }

}
