package com.chua.utils.tools.office.word;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.office.word.docx.WordDocExtractor;
import com.chua.utils.tools.office.word.docx.WordDocxExtractor;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;
import org.docx4j.Docx4J;
import org.docx4j.Docx4jProperties;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * word工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class WordFactory {

    private static final List<DocExtractor> EXTRACTORS = Lists.newArrayList(
            new WordDocxExtractor(),
            new WordDocExtractor());

    /**
     * 解析word
     *
     * @param matcher 匹配器
     * @param url     url
     */
    public void reader(Matcher<String> matcher, URL url) throws Exception {
        if (null == matcher) {
            return;
        }
        ExecutorService executorService = ThreadHelper.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (DocExtractor extractor : EXTRACTORS) {
                if (extractor.matcher(url)) {
                    try {
                        String analyse = extractor.analyse();
                        matcher.doWith(analyse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            executorService.shutdownNow();
        });
    }
}
