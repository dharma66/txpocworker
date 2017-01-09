package com.sage.prometheus.poc;

import java.math.BigDecimal;

public class AggregatedNominal
{
    private String nominalCode;
    private BigDecimal amount;

    public AggregatedNominal(String nominalCode, BigDecimal amount)
    {
        this.nominalCode = nominalCode;
        this.amount = amount;
    }

    public String getNominalCode()
    {
        return nominalCode;
    }

    public void setNominalCode(String nominalCode)
    {
        this.nominalCode = nominalCode;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
