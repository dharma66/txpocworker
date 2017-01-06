package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        logger.info("Received message: " + message);
        long start = System.currentTimeMillis();

        List<Future<List<Transaction>>> futures = setupWorkers();

        awaitResults(futures);

        long end = System.currentTimeMillis();
        logger.info("Got data in: " + (end - start) + "ms");

        start = System.currentTimeMillis();

        Map<String, BigDecimal> aggregatedNominals = collectResults(futures);

        end = System.currentTimeMillis();
        logger.info("Aggregated data in: " + (end - start) + "ms");
        //logger.info("Aggregated data:");
        //logger.info(new PrettyPrintMap(aggregatedNominals).toString());
    }

    private List<Future<List<Transaction>>> setupWorkers() throws InterruptedException
    {
        List<Future<List<Transaction>>> futures = new ArrayList<>();

        for(int i = 0; i < MAX_WORKERS; i++)
        {
            futures.add(service.getTransactions(MAX_WORKERS, i + 1));
        }
        return futures;
    }

    private void awaitResults(List<Future<List<Transaction>>> futures) throws InterruptedException
    {
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
    }

    private Map<String, BigDecimal> collectResults(List<Future<List<Transaction>>> futures)
    {
        List<Transaction> transactions = new ArrayList<>();

        futures.forEach((future) ->
        {
            try
            {
                transactions.addAll(future.get());
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            } catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        });

        return Aggregator.aggregate(transactions);
    }
}
