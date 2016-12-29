package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregatorTest
{
    @Test
    void aggregateNothing()
    {
        List<Transaction> transactions = new ArrayList<>();
        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(0, results.size());
    }

    @Test
    void aggregateSingleTransaction()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");
        tx.setDescription("ABC");
        tx.setAmount(BigDecimal.valueOf(123.45));

        transactions.add(tx);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(123.45), results.get("XYZ"));
    }

    @Test
    void noDescriptionRequired()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");
        tx.setAmount(BigDecimal.valueOf(123.45));

        transactions.add(tx);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(123.45), results.get("XYZ"));
    }

    @Test
    void matchingCodesAggragated()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");
        tx.setAmount(BigDecimal.valueOf(123.45));

        Transaction tx2 = new Transaction();
        tx2.setNominalCode("XYZ");
        tx2.setAmount(BigDecimal.valueOf(123.45));


        transactions.add(tx);
        transactions.add(tx2);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(246.90).setScale(2), results.get("XYZ"));
    }

    @Test
    void nonMatchingCodesNotAggragated()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");
        tx.setAmount(BigDecimal.valueOf(100.00).setScale(2));

        Transaction tx2 = new Transaction();
        tx2.setNominalCode("ABC");
        tx2.setAmount(BigDecimal.valueOf(200.00).setScale(2));


        transactions.add(tx);
        transactions.add(tx2);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(2, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), results.get("XYZ"));
        assertTrue(results.containsKey("ABC"));
        assertEquals(BigDecimal.valueOf(200.00).setScale(2), results.get("ABC"));

    }

    @Test
    void missingNomCodesAggregated()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setAmount(BigDecimal.valueOf(100.00).setScale(2));

        Transaction tx2 = new Transaction();
        tx2.setAmount(BigDecimal.valueOf(200.00).setScale(2));


        transactions.add(tx);
        transactions.add(tx2);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey(""));
        assertEquals(BigDecimal.valueOf(300.00).setScale(2), results.get(""));
    }

    @Test
    void missingAmountIsZero()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");

        transactions.add(tx);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), results.get("XYZ"));
    }

    @Test
    void negativeAmountsAreSubtracted()
    {
        List<Transaction> transactions = new ArrayList<>();

        Transaction tx = new Transaction();
        tx.setNominalCode("XYZ");
        tx.setAmount(BigDecimal.valueOf(200.00).setScale(2));

        Transaction tx2 = new Transaction();
        tx2.setNominalCode("XYZ");
        tx2.setAmount(BigDecimal.valueOf(-100.00).setScale(2));


        transactions.add(tx);
        transactions.add(tx2);

        Map<String, BigDecimal> results = Aggregator.aggregate(transactions);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("XYZ"));
        assertEquals(BigDecimal.valueOf(100.00).setScale(2), results.get("XYZ"));
    }
}