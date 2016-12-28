package com.sage.prometheus.poc;

import java.util.Iterator;
import java.util.Map;

public class PrettyPrintMap<K, V>
{
    private Map<K, V> map;

    public PrettyPrintMap(Map<K, V> map)
    {
        this.map = map;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();

        while(it.hasNext())
        {
            Map.Entry<K, V> entry = it.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }
}
