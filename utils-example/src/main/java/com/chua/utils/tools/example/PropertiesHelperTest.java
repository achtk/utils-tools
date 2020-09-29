package com.chua.utils.tools.example;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.chua.utils.tools.common.loader.PropertiesLoader;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author CH
 * @date 2020-09-29
 */
public class PropertiesHelperTest {

    @Test
    public void map2Properties() {
        String json = "{\n" +
                "  \"spring\": {\n" +
                "    \"test\": [\n" +
                "      1,\n" +
                "      2\n" +
                "    ],\n" +
                "    \"server\": 9090\n" +
                "  }\n" +
                "}";

        Map<String, Object> objectMap = JsonHelper.fromJson2Map(json);
        PropertiesLoader propertiesLoader = PropertiesHelper.map2Properties(objectMap);
        propertiesLoader.add(objectMap);
        propertiesLoader.mapStringObjects("spring");
        System.out.println(propertiesLoader);
    }
}
