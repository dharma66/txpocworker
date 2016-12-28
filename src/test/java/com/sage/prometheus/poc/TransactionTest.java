package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by phil on 28/12/16.
 */
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

    @Test
    void testyTest()
    {
        int cardVal = 16;
        switch (cardVal) {
            case 4: case 5: case 6:
            case 7: case 8:
                System.out.println("Hit");
                break;
            case 9: case 10: case 11:
                System.out.println("Double");
                break;
            case 15: case 16:
                System.out.println("Surrender");
                break;
            default:
                System.out.println("Stand");
        }
    }

}