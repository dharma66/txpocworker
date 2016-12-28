package com.sage.prometheus.poc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by phil on 23/12/16.
 */
class PrettyPrintMapTest
{
    @Test
    void testToString()
    {
        Map<String, String> map = new TreeMap<>();

        map.put("Key1", "Value1");
        map.put("Key2", "Value2");
        PrettyPrintMap<String, String> ppMap = new PrettyPrintMap<>(map);

        assertEquals("Key1=Value1\nKey2=Value2\n", ppMap.toString());
    }

}