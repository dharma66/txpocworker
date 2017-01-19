package com.sage.prometheus.poc;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public  class TransactionTest
{
    @Test
    public void getNominalCode()
    {
        Transaction tx = new Transaction();

        tx.setNominalCode("ABC");
        assertEquals("ABC", tx.getNominalCode());
    }

    @Test
    public void getDescription()
    {
        Transaction tx = new Transaction();

        tx.setDescription("XYZ");
        assertEquals("XYZ", tx.getDescription());
    }

    @Test
    public void getAmount()
    {
        Transaction tx = new Transaction();

        tx.setAmount(BigDecimal.valueOf(123.45));
        assertEquals(123.45, tx.getAmount().doubleValue(), 0.005);
    }

    @Test
    public void unsetNominalCodeIsEmptyString()
    {
        Transaction tx = new Transaction();

        assertEquals(true, tx.getNominalCode().isEmpty());
    }

    @Test
    public void unsetAmountCodeIsZero()
    {
        Transaction tx = new Transaction();

        assertEquals(BigDecimal.valueOf(0.00).setScale(2), tx.getAmount());
    }
}