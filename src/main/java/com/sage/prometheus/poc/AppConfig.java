package com.sage.prometheus.poc;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class AppConfig extends AbstractMongoConfiguration
{
    @Override
    protected String getDatabaseName()
    {
        return "db";
    }

    @Override
    public Mongo mongo() throws Exception
    {
        return new MongoClient("store");
    }
}