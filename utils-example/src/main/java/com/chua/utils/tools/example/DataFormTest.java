package com.chua.utils.tools.example;

import com.chua.utils.tools.function.impl.JsonMapDataTransform;
import com.chua.utils.tools.function.impl.YamlPropertiesDataTransform;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;

/**
 * 数据转化
 * @author CH
 */
public class DataFormTest {

    @Test
    public void dataForm() {
        String json = "{\n" +
                "  \"spring\": {\n" +
                "    \"test\": [\n" +
                "      1,\n" +
                "      2\n" +
                "    ],\n" +
                "    \"server\": 9090\n" +
                "  }\n" +
                "}";

        YamlPropertiesDataTransform yamlPropertiesDataTransform1 = new YamlPropertiesDataTransform();
        System.out.println(yamlPropertiesDataTransform1.transFrom(json));
        System.out.println(yamlPropertiesDataTransform1.transTo(yamlPropertiesDataTransform1.transFrom(json)));

        System.out.println("===============================");
        JsonMapDataTransform jsonMapDataTransform = new JsonMapDataTransform();
        System.out.println(jsonMapDataTransform.transFrom(json));
        System.out.println(jsonMapDataTransform.transTo(jsonMapDataTransform.transFrom(json)));
    }
}
