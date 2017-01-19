package com.sage.prometheus.poc;

import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class PrettyPrintMapTest
{
    @Test
    public void testToString()
    {
        Map<String, String> map = new TreeMap<>();

        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        PrettyPrintMap<String, String> ppMap = new PrettyPrintMap<>(map);

        assertEquals("Key1=Value1\nKey2=Value2\n", ppMap.toString());
    }

}