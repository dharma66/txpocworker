package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SummaryTest
{
    @Test
    public void testConstructorOk()
    {
        Map<String, BigDecimal> nominals = new HashMap<>();
        nominals.put("0100", new BigDecimal(100));
        nominals.put("0200", new BigDecimal(200));
        Summary summary = new Summary("RequestID", nominals);

        assertEquals(ArrayList.class.getSimpleName(), summary.getAggregatedNominals().getClass().getSimpleName());
        assertEquals(2, summary.getAggregatedNominals().size());
        assertEquals(AggregatedNominal.class.getSimpleName(), summary.getAggregatedNominals().get(0).getClass().getSimpleName());
    }

    @Test
    public void testConstructorNominalsNull()
    {
        Summary summary = new Summary("RequestID", null);
        assertEquals(0, summary.getAggregatedNominals().size());
    }

    @Test
    public void testConstructorNominalsEmpty()
    {
        Map<String, BigDecimal> nominals = new HashMap<>();
        Summary summary = new Summary("RequestID", nominals);
        assertEquals(0, summary.getAggregatedNominals().size());

    }
}