package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Responder
{
    private static final Logger logger = LoggerFactory.getLogger(Responder.class);

    private final RabbitTemplate rabbitTemplate;

    public Responder(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void send(String response)
    {
        rabbitTemplate.convertAndSend(WorkerApplication.postQueueName, response);
    }
}
