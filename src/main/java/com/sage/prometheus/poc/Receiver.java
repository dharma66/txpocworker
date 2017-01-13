package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SummaryRepository repo;
    private final Responder responder;

    private static final int MAX_WORKERS = 4;

    @Autowired
    public Receiver(ExecutorService service, SummaryRepository repo, Responder responder)
    {
        this.service = service;
        this.repo = repo;
        this.responder = responder;
    }

    public void receiveMessage(String requestId) throws Exception
    {
        try
        {
            logger.info("Received requestId: " + requestId);

            List<Future<List<Transaction>>> futures = setupWorkers();

            awaitResults(futures);

            Map<String, BigDecimal> aggregatedNominals = collectResults(futures);

            Summary summary = new Summary(requestId, aggregatedNominals);
            //repo.save(summary);
            responder.send(requestId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
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
