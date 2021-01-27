package com.chua.utils.tools.example;


import com.chua.utils.tools.collects.FlatMap;
import com.chua.utils.tools.function.JsonPath;
import com.chua.utils.tools.util.JsonUtils;

import java.util.List;
import java.util.Map;

/**
 * @author CH* @version 1.0.0* @since 2021/1/26
 */
public class JsonPathExample {

    public static void main(String[] args) {
        JsonPath jsonPath = JsonUtils.parser("{\n" +
                "                        \"store\": {\n" +
                "                            \"book\": [\n" +
                "                                {\n" +
                "                                    \"category\": \"reference\",\n" +
                "                                    \"author\": \"Nigel Rees\",\n" +
                "                                    \"title\": \"Sayings of the Century\",\n" +
                "                                    \"price\": 8.95\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"category\": \"fiction\",\n" +
                "                                    \"author\": \"Evelyn Waugh\",\n" +
                "                                    \"isbn\": \"0-553-21311-3\",\n" +
                "                                    \"title\": \"Sword of Honour\",\n" +
                "                                    \"price\": 12.99\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"category\": \"fiction\",\n" +
                "                                    \"author\": \"Herman Melville\",\n" +
                "                                    \"title\": \"Moby Dick\",\n" +
                "                                    \"price\": 8.99\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"category\": \"fiction\",\n" +
                "                                    \"author\": \"J. R. R. Tolkien\",\n" +
                "                                    \"title\": \"The Lord of the Rings\",\n" +
                "                                    \"isbn\": \"0-395-19395-8\",\n" +
                "                                    \"price\": 22.99\n" +
                "                                }\n" +
                "                            ],\n" +
                "                            \"bicycle\": {\n" +
                "                                \"color\": \"red\",\n" +
                "                                \"price\": 19.95\n" +
                "                            }\n" +
                "                        },\n" +
                "                         \"expensive\": 10\n" +
                "                        }"
        );

        jsonPath.set("store.demo.t", 1);

        List<Object> objects = jsonPath.find("store.*.price");
        Map<String, Object> map = jsonPath.findMap("store.*.price");
        FlatMap flatMap = jsonPath.flatMap("store.*.*");

        System.out.println(objects);
        System.out.println(map);
        System.out.println(flatMap);
    }
}
