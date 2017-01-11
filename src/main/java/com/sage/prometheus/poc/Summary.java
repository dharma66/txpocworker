package com.sage.prometheus.poc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Summary
{
    private String requestId;
    private List<AggregatedNominal> aggregatedNominals = new ArrayList<>();

    public Summary(String requestId, Map<String, BigDecimal> nominals)
    {
        this.requestId = requestId;
        if(nominals != null)
        {
            nominals.forEach((key, value) ->
            {
                this.aggregatedNominals.add(new AggregatedNominal(key, value));
            });
        }
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public List<AggregatedNominal> getAggregatedNominals()
    {
        return aggregatedNominals;
    }

    public void setAggregatedNominals(List<AggregatedNominal> aggregatedNominals)
    {
        this.aggregatedNominals = aggregatedNominals;
    }
}
