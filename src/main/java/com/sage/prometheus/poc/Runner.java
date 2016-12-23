package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by phil on 17/12/16.
 */
@Component
public class Runner implements CommandLineRunner
{
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final ConfigurableApplicationContext context;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate, ConfigurableApplicationContext conext)
    {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.context = conext;
    }

    @Override
    public void run(String... args) throws Exception
    {
        for(int i = 1; i < 10; i++)
        {
            rabbitTemplate.convertAndSend(WorkerApplication.queueName, "Hello from RabbitMQ - " + i);
            logger.info("Posted mesage " + i);
        }

        //context.close();
    }
}
