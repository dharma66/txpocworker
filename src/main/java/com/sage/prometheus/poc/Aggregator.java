package com.sage.prometheus.poc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Aggregator
{
    public static Map<String, BigDecimal> aggregate(List<Transaction> transactions)
    {
        Map<String, BigDecimal> results = new TreeMap<>();

            transactions.forEach(transaction ->
            {
                results.computeIfPresent(transaction.getNominalCode(), (key, val) -> {
                    return transaction.getAmount().add(val);
                });
                results.putIfAbsent(transaction.getNominalCode(), transaction.getAmount());

            });

        return results;
    }
}
