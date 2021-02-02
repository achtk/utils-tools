package com.chua.tools.example;

import com.chua.tools.example.utils.MockUtils;
import com.chua.utils.tools.function.Template;
import com.chua.utils.tools.office.excel.template.ExcelTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/30
 */
public class TemplateExample {

    public static void main(String[] args) {
        //测试excel
        testExcelTemplate();
    }

    /**
     * 测试excel
     */
    private static void testExcelTemplate() {
        Template template = new ExcelTemplate();
        Map<String, Object> param = new HashMap<>();
        param.put("id", MockUtils.create(String.class));
        param.put("title", MockUtils.create(String.class));
        param.put("name", MockUtils.create(String.class));

        Map<String, Object> param1 = new HashMap<>();
        param1.put("id", "1");
        param1.put("title", "1");
        param1.put("name", "1");

        template.writeAndClose("E:/1.xls", "E:/1/1.xls", param, param1);
    }
}
