package com.sage.prometheus.poc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Component
public class Receiver
{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final SummaryRepository repo;
    private final Responder responder;
    private static final String contentHost = "content";
    private ObjectMapper mapper = new ObjectMapper();

    private static final int MAX_WORKERS = 2;

    @Autowired
    public Receiver(SummaryRepository repo, Responder responder)
    {
        this.repo = repo;
        this.responder = responder;
    }

    public void receiveMessage(byte[] request) throws Exception
    {
        try
        {
            RequestMessage msg = mapper.readValue(request, RequestMessage.class);

            String requestId = msg.requestId;
            int numTransactions = Integer.parseInt(msg.transactionCount);

//            List<Future<List<Transaction>>> futures = setupWorkers(numTransactions);
//
//            awaitResults(futures);

            List<Transaction> transactions = new ArrayList<>();
            transactions.addAll(getTransactions());
            transactions.addAll(getTransactions());


            Map<String, BigDecimal> aggregatedNominals = collectResults(transactions);

            Summary summary = new Summary(requestId, aggregatedNominals);

            repo.save(summary);
            String end = Instant.now().toString();

            responder.send(String.format("{ \"requestId\": \"%s\", \"completed\": \"%s\" }", requestId, end));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Collection getTransactions()
    {
        String uri = String.format("http://" + contentHost + ":8080/transactions");
        RestTemplate template = restTemplate();

        ResponseEntity<List<Transaction>> result = template.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Transaction>>(){});
        List<Transaction> resources = result.getBody();
        return resources;
    }

//    private List<Future<List<Transaction>>> setupWorkers(int numTransactions) throws InterruptedException
//    {
//        List<Future<List<Transaction>>> futures = new ArrayList<>();
//
//        for(int i = 0; i < MAX_WORKERS; i++)
//        {
//            futures.add(service.getTransactions(numTransactions, MAX_WORKERS, 0));
//        }
//        return futures;
//    }
//
//    private void awaitResults(List<Future<List<Transaction>>> futures) throws InterruptedException
//    {
//        boolean someNotDone = true;
//
//        while(someNotDone)
//        {
//            someNotDone = false;
//            Iterator it = futures.iterator();
//
//            while(it.hasNext())
//            {
//                Future<List<Transaction>> next = (Future<List<Transaction>>)it.next();
//                if(!next.isDone())
//                    someNotDone = true;
//
//            }
//            Thread.sleep(5);
//        }
//    }

    private Map<String, BigDecimal> collectResults(List<Transaction> transactions)
    {
        System.out.println("Transactions read: " + transactions.size());
        return Aggregator.aggregate(transactions);
    }

    @Bean
    public RestTemplate restTemplate()
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerSubtypes(Transaction.class);
        mapper.registerModule(new Jackson2HalModule());

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));;
        converter.setObjectMapper(mapper);

        return new RestTemplate(Arrays.asList(converter));
    }

}
