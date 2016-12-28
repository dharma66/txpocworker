package com.sage.prometheus.poc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by phil on 20/12/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction
{
    private String nominalCode;
    private String description;
    private BigDecimal amount;

    public String getNominalCode()
    {
        if(nominalCode == null)
            return "";

        return nominalCode;
    }

    public void setNominalCode(String nominalCode)
    {
        this.nominalCode = nominalCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getAmount()
    {
        if(amount == null)
            return new BigDecimal(0).setScale(2);

        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
}
