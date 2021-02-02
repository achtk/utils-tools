package com.chua.utils.tools.office.pdf.template;

import com.chua.utils.tools.function.Template;
import com.chua.utils.tools.util.ArrayUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextMarginFinder;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.apache.commons.collections4.list.TreeList;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于[itext]统一接口 - pdf模板
 * <p>参见 [itext]语法</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class PdfTemplate implements Template {
    @Override
    public String getTemplate(String template, Map<String, Object> params) {
        return null;
    }

    @Override
    public void writeAndClose(String template, String outPath, Map<String, Object>... params) {
        try (FileOutputStream fos = new FileOutputStream(outPath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
            PdfReader pdfReader = new PdfReader(template);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, bos);
            //替换内容域
            this.renderContent(pdfReader, pdfStamper, ArrayUtils.firstElement(params));
            //替换表单域
            this.renderForm(pdfStamper, ArrayUtils.firstElement(params));

            //如果为false那么生成的PDF文件还能编辑，一定要设为true
            pdfStamper.setFormFlattening(true);
            pdfStamper.close();

            Document doc = new Document();
            PdfCopy copy = new PdfCopy(doc, fos);
            doc.open();
            PdfImportedPage importPage = copy.getImportedPage(new PdfReader(bos.toByteArray()), 1);
            copy.addPage(importPage);
            doc.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * 替换内容
     *
     * @param pdfReader  reader
     * @param pdfStamper 模板
     * @param params     数据
     */
    private void renderContent(PdfReader pdfReader, PdfStamper pdfStamper, Map<String, Object> params) throws IOException, DocumentException {
        //页码
        int numberOfPages = pdfReader.getNumberOfPages();
        //文本解析器
        PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
        //所有渲染对象
        Map<Integer, TextRenderInfo> textRenderInfos = new HashMap<>();
        List<String> textValue = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        //修改PDF
        for (int i = 0; i < numberOfPages; i++) {
            pdfReaderContentParser.processContent(i + 1, new TextMarginFinder() {
                @Override
                public void renderText(TextRenderInfo renderInfo) {
                    textRenderInfos.put(count.getAndIncrement(), renderInfo);
                    textValue.add(renderInfo.getText());
                }

            });
        }

        //单元素
        List<Integer> oneItem = new TreeList<>();
        for (int i = 0; i < textValue.size() - 1; i++) {
            String s = textValue.get(i);
            String s1 = textValue.get(i + 1);
            if ("{".equals(s) && "{".equals(s1)) {
                oneItem.add(i);
                continue;
            }

            if ("}".equals(s) && "}".equals(s1)) {
                oneItem.add(i + 1);
                continue;
            }
        }

        RangeSet rangeSet = TreeRangeSet.create();
        for (int i = 0; i < oneItem.size(); i += 2) {
            Integer start = oneItem.get(i);
            Integer end = oneItem.get(i + 1);
            rangeSet.add(Range.closed(start, end));
        }

        //修改PDF
        count.set(0);
        for (int i = 0; i < numberOfPages; i++) {
            StringBuffer stringBuffer = new StringBuffer();
            pdfReaderContentParser.processContent(i + 1, new TextMarginFinder() {
                @Override
                public void renderText(TextRenderInfo renderInfo) {
                    int i1 = count.get();
                    String text = renderInfo.getText();

                    if (rangeSet.contains(i1)) {
                        stringBuffer.append(text);
                        if (!rangeSet.contains(i1 + 1)) {
                            String place = stringBuffer.substring(2, stringBuffer.length() - 2);
                            renderInfo(renderInfo, place, params);
                            stringBuffer.delete(0, stringBuffer.length());
                        } else {
                            renderInfo(renderInfo, "", params);
                        }
                    }
                    count.incrementAndGet();
                }

            });
        }

        System.out.println();
    }

    /**
     * 渲染数据
     *
     * @param placeValue 数据
     * @param params     占位数据
     */
    private void renderInfo(TextRenderInfo textRenderInfo, String placeValue, Map<String, Object> params) {
        if(!Strings.isNullOrEmpty(placeValue)) {
            placeValue = params.getOrDefault(placeValue, "").toString();
        }
        ClassUtils.setFieldValue("text", placeValue, textRenderInfo);
        ClassUtils.setFieldValue("string", new PdfString(placeValue), textRenderInfo);
    }


    /**
     * 表单替换
     *
     * @param pdfStamper 模板
     * @param params     数据
     */
    private void renderForm(PdfStamper pdfStamper, Map<String, Object> params) throws IOException, DocumentException {
        AcroFields acroFields = pdfStamper.getAcroFields();
        Map<String, AcroFields.Item> fields = acroFields.getFields();
        for (Map.Entry<String, AcroFields.Item> entry : fields.entrySet()) {
            AcroFields.Item item = entry.getValue();
            PdfDictionary pdfDictionary = item.getValue(0);
            if (null == pdfDictionary) {
                continue;
            }
            PdfString pdfString = pdfDictionary.getAsString(PdfName.V);
            String pdfValue = pdfString.toString();
            boolean isPlaceholder = pdfValue.startsWith("{{") && pdfValue.endsWith("}}");
            String value = isPlaceholder ? pdfValue.substring(2, pdfValue.length() - 2) : pdfValue;
            if (isPlaceholder) {
                acroFields.setField(entry.getKey(), params.getOrDefault(value, "").toString());
            }
        }
    }
}
