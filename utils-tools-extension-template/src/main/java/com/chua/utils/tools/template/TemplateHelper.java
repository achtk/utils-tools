package com.chua.utils.tools.template;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * 模板工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/5/20 9:33
 */
public class TemplateHelper {

    private static final Configuration CONFIGURATION = new Configuration();
    private static final String DEFAULT = "default_template";

    static {
        CONFIGURATION.setDefaultEncoding("UTF-8");
    }

    /**
     * 字符串模板替换
     *
     * @param template 模板
     * @param params   数据
     * @return
     */
    public static String templateAsString(String template, Map<String, Object> params) throws Exception {
        Template template1 = new Template(DEFAULT, template, CONFIGURATION);
        StringWriter stringWriter = new StringWriter();
        template1.process(params, stringWriter);
        return stringWriter.toString();
    }

    /**
     * 字符串模板替换输出文件
     *
     * @param template 模板
     * @param params   参数
     * @param out      输出文件
     */
    public static void templateAsFile(String template, Map<String, Object> params, File out) throws Exception {
        Template template1 = new Template(DEFAULT, template, CONFIGURATION);
        FileWriter fileWriter = new FileWriter(out);
        template1.process(params, fileWriter);
    }
}
