package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
        int numRequests = 1;
        logger.info("\n\n\n\nPosting " + numRequests + " messages");

        long start = System.currentTimeMillis();
        for(int i = 0; i < numRequests; i++)
        {
            String uuid = UUID.randomUUID().toString();
            rabbitTemplate.convertAndSend(WorkerApplication.queueName, uuid);
            logger.info("Posted mesage:    " + uuid);
        }

        logger.info("Messages posted in " + (System.currentTimeMillis() - start) + "ms");
        //context.close();
    }
}
