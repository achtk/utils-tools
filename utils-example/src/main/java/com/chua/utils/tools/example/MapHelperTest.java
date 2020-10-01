package com.chua.utils.tools.example;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.MapHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author CH
 */
public class MapHelperTest {

    @Test
    public void merga() {
        Map<String, Object> value1 = Maps.newHashMap();
        value1.put("key11", "value11");
        Map<String, Object> value2 = Maps.newHashMap();
        value2.put("key21", "value21");
        Map<String, Object> source1 = Maps.newHashMap();
        source1.put("key1", "value1");
        source1.put("key2", value2);
        Map<String, Object> source2 = Maps.newHashMap();
        source2.put("key1", "value2");
        source2.put("key2", value1);
        source2.put("key3", value2);

    }
}
