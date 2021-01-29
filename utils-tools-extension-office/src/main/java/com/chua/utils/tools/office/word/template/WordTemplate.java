package com.chua.utils.tools.office.word.template;

import com.chua.utils.tools.function.Template;
import com.deepoove.poi.XWPFTemplate;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 基于[poi-tl]统一接口 - word模板
 * <p>参见 [poi-tl]语法</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class WordTemplate implements Template {
    @Override
    public String getTemplate(String template, Map<String, Object> params) {
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(template).render(params);
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            xwpfTemplate.write(stream);
            return stream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != xwpfTemplate) {
                try {
                    xwpfTemplate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void writeAndClose(String template, Map<String, Object> params, String path) {
        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(template).render(params);
        try (FileOutputStream stream = new FileOutputStream(path)) {
            xwpfTemplate.write(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != xwpfTemplate) {
                try {
                    xwpfTemplate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
