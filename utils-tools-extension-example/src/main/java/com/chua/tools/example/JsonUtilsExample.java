package com.chua.tools.example;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.function.JsonPath;
import com.chua.utils.tools.logger.LogUtils;
import com.chua.utils.tools.util.JsonUtils;

/**
 * json工具测试
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
public class JsonUtilsExample {

    public static void main(String[] args) {
        //测试Json字符串转Json对象
        testJsonString2Object(getDemo1());
        //测试JsonPath

        testJsonPath(getDemo1());
    }

    /**
     * 测试JsonPath
     *
     * @param demo1
     */
    private static void testJsonPath(String demo1) {
        JsonPath jsonPath = JsonUtils.parser(demo1);
        LogUtils.script("jsonPath.findMap('*.book[*].*')", OperateHashMap.create("jsonPath", jsonPath));
    }

    /**
     * 测试Json字符串转Json对象
     *
     * @param demo1
     */
    private static void testJsonString2Object(String demo1) {
        LogUtils.println("{}", JsonUtils.fromJson2Map(demo1));
    }

    public static String getDemo1() {
        return "{ \n" +
                "                    \"store\": { \n" +
                "                        \"book\": [ \n" +
                "                            { \n" +
                "                                \"category\": \"reference\", \n" +
                "                                \"author\": \"igel Rees\", \n" +
                "                                \"title\": \"Sayings of the Century\", \n" +
                "                                \"price\": 8.95 \n" +
                "                            }, { \n" +
                "                                \"category\": \"fiction\", \n" +
                "                                \"author\": \"Evelyn Waugh\", \n" +
                "                                \"isbn\": \"0-553-21311-3\", \n" +
                "                                \"title\": \"Sword of Honour\", \n" +
                "                                \"price\": 12.99 \n" +
                "                            }, { \n" +
                "                                \"category\": \"fiction\", \n" +
                "                                \"author\": \"Herman Melville\", \n" +
                "                                \"title\": \"Moby Dick\", \n" +
                "                                \"price\": 8.99 \n" +
                "                            }, { \n" +
                "                                \"category\": \"fiction\", \n" +
                "                                \"author\": \"J. R. R. Tolkien\", \n" +
                "                                \"title\": \"The Lord of the Rings\", \n" +
                "                                \"isbn\": \"0-395-19395-8\", \n" +
                "                                \"price\": 22.99 \n" +
                "                            } \n" +
                "                        ], \n" +
                "                        \"bicycle\": { \n" +
                "                            \"color\": \"red\", \n" +
                "                            \"price\": 19.95 \n" +
                "                        } \n" +
                "                    }, \n" +
                "                     \"expensive\": 10 \n" +
                "                    }";
    }

    public static String getDemo() {
        return "{\n" +
                "                       \"min_position\": 7,\n" +
                "                       \"has_more_items\": true,\n" +
                "                       \"items_html\": \"Car\",\n" +
                "                       \"new_latent_count\": 2,\n" +
                "                       \"data\": {\n" +
                "                          \"length\": 20,\n" +
                "                          \"text\": \"QQE2.com\",\n" +
                "                          \"datas\": [\n" +
                "                             {\n" +
                "                                \"min_position\": 4,\n" +
                "                                \"category\": false,\n" +
                "                                \"items_html\": \"Car\",\n" +
                "                                \"price\": 1\n" +
                "                             },\n" +
                "                             {\n" +
                "                                \"min_position\": 9,\n" +
                "                                \"category\": true,\n" +
                "                                \"items_html\": \"Bike\",\n" +
                "                                \"price\": 1\n" +
                "                             },\n" +
                "                             {\n" +
                "                                \"min_position\": 7,\n" +
                "                                \"category\": false,\n" +
                "                                \"items_html\": \"Car\",\n" +
                "                                \"price\": 1\n" +
                "                             },\n" +
                "                             {\n" +
                "                                \"min_position\": 7,\n" +
                "                                \"category\": true,\n" +
                "                                \"items_html\": \"Bike\",\n" +
                "                                \"price\": 8\n" +
                "                             },\n" +
                "                             {\n" +
                "                                \"min_position\": 6,\n" +
                "                                \"category\": true,\n" +
                "                                \"items_html\": \"Car\",\n" +
                "                                \"price\": 4\n" +
                "                             }\n" +
                "                          ]\n" +
                "                       },\n" +
                "                       \"numericalArray\": [\n" +
                "                          24,\n" +
                "                          24,\n" +
                "                          32,\n" +
                "                          24,\n" +
                "                          33\n" +
                "                       ],\n" +
                "                       \"StringArray\": [\n" +
                "                          \"Oxygen\",\n" +
                "                          \"Nitrogen\",\n" +
                "                          \"Carbon\",\n" +
                "                          \"Carbon\",\n" +
                "                          \"Carbon\"\n" +
                "                       ],\n" +
                "                       \"multipleTypesArray\": 3,\n" +
                "                       \"objArray\": [\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 5\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"lower\",\n" +
                "                             \"age\": 5\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"lower\",\n" +
                "                             \"age\": 1\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 7\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"upper\",\n" +
                "                             \"age\": 4\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 1\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 3\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 3\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"lower\",\n" +
                "                             \"age\": 6\n" +
                "                          },\n" +
                "                          {\n" +
                "                             \"class\": \"middle\",\n" +
                "                             \"age\": 1\n" +
                "                          }\n" +
                "                       ]\n" +
                "                    }";
    }
}
