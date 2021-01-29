package com.chua.utils.tools.example;

import com.chua.utils.tools.collects.OperateHashMap;
import com.chua.utils.tools.function.Template;
import com.chua.utils.tools.office.pdf.template.PdfTemplate;

/**
 * Template测试例子
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class TemplateExample {

    public static void main(String[] args) {
        Template pdfTemplate = new PdfTemplate();
        pdfTemplate.writeAndClose("E:/1/1.pdf", OperateHashMap.create("test", 2333), "E:/1/2.pdf");
    }
}
