package com.chua.utils.tools.example;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.stream.JsonStream;
import com.chua.utils.tools.text.IdHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class JsonHelperExample {


    public static void main(String[] args) throws IOException {
        String value = JsonStream.of(getJson()).findPath("0").findPath("id", 1).findPath("id").asText();
        System.out.println(value);
    }


    static String getJson() {
        Random random = new Random();
        int length = random.nextInt(10);
        Map<String, Object> result = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < length; i++) {
            Random random1 = new Random();
            List<Map<String, Object>> items = new ArrayList<>();

            int length1 = random1.nextInt(10);
            for (int j = 0; j < length1; j++) {
                Map<String, Object> item = new HashMap<>();
                item.put(i + "" + j, j);
                item.put("id", atomicInteger.incrementAndGet());
                item.put("value", IdHelper.createUuid());
                item.put("name", i + "" +j + "Demo");

                items.add(item);
            }
            result.put(i + "", items);
        }

        return JsonHelper.toFormatJson(result);
    }
}
