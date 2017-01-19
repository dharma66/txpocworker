package com.sage.prometheus.poc;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AggregatedNominalTest
{
    @Test
    public void testConstructor()
    {
        AggregatedNominal an = new AggregatedNominal("0100", new BigDecimal(100.00));

        assertEquals(100.00, an.getAmount().doubleValue(), 0.005);
        assertEquals("0100", an.getNominalCode());
    }
}