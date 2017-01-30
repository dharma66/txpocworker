package com.sage.prometheus.poc;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;


@Service
public class ExecutorService
{
    private static final Logger logger = LoggerFactory.getLogger(ExecutorService.class);
    private static final boolean stub = false;
    private static final String contentHost = "localhost";

    private static final String pageSize = "1000";

    @Async
    public Future<List<Transaction>> getTransactions(int numTransactions, int pageStep, int offset) throws InterruptedException
    {
        if (stub)
        {
            return pretendToReadData(pageStep, offset);
        } else
        {
            return readData(numTransactions, pageStep, offset);
        }
    }

    private Future<List<Transaction>> readData(int numTransactions, int pageStep, int offset)
    {
        long page = offset;

        String uri = String.format("http://" + contentHost + ":8080/transactions?size=" + pageSize +"&page=%d", page);

        RestTemplate template = restTemplate();

        ResponseEntity<PagedResources<Transaction>> result = template.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Transaction>>(){});
        PagedResources<Transaction> resources = result.getBody();

        long totalPages = numTransactions / Integer.parseInt(pageSize);

        List<Transaction> transactions = new ArrayList<>(resources.getContent());

        while(page + pageStep < totalPages)
        {
            page = page + pageStep;
            uri = String.format("http://" + contentHost + ":8080/transactions?size=" + pageSize +"&page=%d", page);
            getPageData(uri, template, transactions);
        }

        return new AsyncResult<>(transactions);
    }

    private void getPageData(String uri, RestTemplate template, List<Transaction> transactions)
    {
        ResponseEntity<PagedResources<Transaction>> results = template.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Transaction>>(){});
        PagedResources<Transaction> res = results.getBody();
        transactions.addAll(new ArrayList<>(res.getContent()));
    }

    private Future<List<Transaction>> pretendToReadData(int pageStep, int offset)
    {
        try
        {
            Thread.sleep(2000);
        }catch(Exception e){}

        return new AsyncResult<>(new ArrayList<>());
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
