package com.sage.prometheus.poc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@SpringBootApplication
@EnableAsync
public class WorkerApplication extends AsyncConfigurerSupport
{
    final static String readQueueName = "worker-queue";
    final static String postQueueName = "response-queue";
    final static String queueHost = "localhost";

//    @Bean ConnectionFactory connectionFactory()
//    {
//        return new CachingConnectionFactory(queueHost, 5672 );
//    }

    @Bean
    Queue queue()
    {
        return new Queue(readQueueName, false);
    }

    @Bean
    TopicExchange exchange()
    {
        return new TopicExchange("worker-exchange");
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(readQueueName);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
    {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(readQueueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(4);

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver)
    {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Override
    public Executor getAsyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("PrometheusExecutor-");
        executor.initialize();

        return executor;
    }

	public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}
}
