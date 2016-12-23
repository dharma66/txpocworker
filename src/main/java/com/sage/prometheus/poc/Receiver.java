package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * Created by phil on 17/12/16.
 */
@Component
public class Receiver
{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private final ExecutorService service;
    private static final int MAX_WORKERS = 4;

    public Receiver(ExecutorService service)
    {
        this.service = service;
    }


    public void receiveMessage(String message) throws Exception
    {
        logger.info("MESSAGE RECEIVED: " + message);
        long start = System.currentTimeMillis();

        List<Future<List<Transaction>>> futures = new ArrayList<>();

        for(int i = 0; i < MAX_WORKERS; i++)
        {
            futures.add(service.getTransactions(MAX_WORKERS, i + 1));
        }

        boolean someNotDone = true;

        while(someNotDone)
        {
            someNotDone = false;
            Iterator it = futures.iterator();

            while(it.hasNext())
            {
                Future<List<Transaction>> next = (Future<List<Transaction>>)it.next();
                if(!next.isDone())
                    someNotDone = true;

            }
            Thread.sleep(10);
        }

        long end = System.currentTimeMillis();
        String time = "" + (end - start) + "ms";


        logger.info("Done getting records in: " + time);
        start = end;
        logger.info("Reducing the map...");

        Map<String, BigDecimal> reducedNominals = reduce(futures);
        end = System.currentTimeMillis();
        time = "" + (end - start) + "ms";

        logger.info("Done reducing in : " + time);

        //logger.info("reducedNominals :\n" + new PrettyPrintMap<>(reducedNominals));
    }

    private Map<String, BigDecimal> reduce(List<Future<List<Transaction>>> futures)
    {
        Map<String, BigDecimal> results = new TreeMap<>();

        futures.forEach(future ->
        {
            try
            {
                future.get().forEach(transaction ->
                {
                    results.computeIfPresent(transaction.getNominalCode(), (key, val) -> {
                        return transaction.getAmount().add(val);
                    });
                    results.putIfAbsent(transaction.getNominalCode(), transaction.getAmount());

                });
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        });

        return results;
    }

}
