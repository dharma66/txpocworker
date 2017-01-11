package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by phil on 10/01/17.
 */
public class AggregatedNominalTest
{
    @Test
    public void testConstructor()
    {
        AggregatedNominal an = new AggregatedNominal("0100", new BigDecimal(100.00));

        assertEquals(100.00, an.getAmount().doubleValue());
        assertEquals("0100", an.getNominalCode());
    }
}