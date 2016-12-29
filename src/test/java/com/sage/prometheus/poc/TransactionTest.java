package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest
{
    @Test
    void getNominalCode()
    {
        Transaction tx = new Transaction();

        tx.setNominalCode("ABC");
        assertEquals("ABC", tx.getNominalCode());
    }

    @Test
    void getDescription()
    {
        Transaction tx = new Transaction();

        tx.setDescription("XYZ");
        assertEquals("XYZ", tx.getDescription());
    }

    @Test
    void getAmount()
    {
        Transaction tx = new Transaction();

        tx.setAmount(BigDecimal.valueOf(123.45));
        assertEquals(123.45, tx.getAmount().doubleValue());
    }

    @Test
    void unsetNominalCodeIsEmptyString()
    {
        Transaction tx = new Transaction();

        assertTrue(tx.getNominalCode().isEmpty());
    }

    @Test
    void unsetAmountCodeIsZero()
    {
        Transaction tx = new Transaction();

        assertEquals(BigDecimal.valueOf(0.00).setScale(2), tx.getAmount());
    }
}