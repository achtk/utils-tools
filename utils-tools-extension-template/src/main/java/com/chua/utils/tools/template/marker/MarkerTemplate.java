package com.chua.utils.tools.template.marker;

import com.chua.utils.tools.function.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 基于[freemarker]统一接口 - 文本模板
 *  <p>参见 [freemarker]语法</p>
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class MarkerTemplate implements Template {
    private static final Configuration CONFIGURATION = new Configuration(Configuration.VERSION_2_3_30);
    private static final String DEFAULT = "default_template";

    static {
        CONFIGURATION.setDefaultEncoding("UTF-8");
    }

    @Override
    public String getTemplate(String template, Map<String, Object> params) {
        try (StringWriter stringWriter = new StringWriter()) {
            freemarker.template.Template template1 = new freemarker.template.Template(DEFAULT, template, CONFIGURATION);
            template1.process(params, stringWriter);
            return stringWriter.toString();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeAndClose(String template, Map<String, Object> params, String path) {
        try (FileWriter fileWriter = new FileWriter(new File(path))) {
            freemarker.template.Template template1 = new freemarker.template.Template(DEFAULT, template, CONFIGURATION);
            template1.process(params, fileWriter);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
