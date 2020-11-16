package com.chua.utils.tools.office.word;

/**
 * word工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class WordFactory {
    /**
     * 解析word
     *
     * @param matcher 匹配器
     * @param url     url
     */
//    public void reader(Matcher<Map<String, String>> matcher, URL url) throws Exception {
//        WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.load(url.openStream());
//        MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
//        List<Object> content = mainDocumentPart.getContent();
//        System.out.println();
//    }
//
//    /**
//     * html转 doc
//     *
//     * @param html    html
//     * @param docFile doc
//     */
//    public void html2Doc(File html, File docFile) throws Exception {
//        String imagePath = html.getParent() + "/_files";
//        File temp = new File(imagePath);
//        if (!temp.exists()) {
//            temp.mkdirs();
//        }
//        imagePath = temp.getAbsolutePath();
//
//        AbstractHtmlExporter.HtmlSettings htmlSettings = new AbstractHtmlExporter.HtmlSettings();
//        WordprocessingMLPackage docxOut = WordprocessingMLPackage.createPackage();
//        htmlSettings.setWmlPackage(docxOut);
//
//        htmlSettings.setImageDirPath(imagePath);
//        htmlSettings.setImageTargetUri(imagePath);
//
//        OutputStream os = new java.io.FileOutputStream(html);
//        Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
//
//        // XHTML to docx
//        String stringFromFile = FileUtils.readFileToString(html, "UTF-8");
//
//        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
//        docxOut.getMainDocumentPart().addTargetPart(ndp);
//        ndp.unmarshalDefaultNumbering();
//
//        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(docxOut);
//        XHTMLImporter.setHyperlinkStyle("Hyperlink");
//
//        docxOut.getMainDocumentPart().getContent().addAll(XHTMLImporter.convert(stringFromFile, null));
//        docxOut.save(docFile);
//    }
//
//    /**
//     * html转 doc
//     *
//     * @param docFile  doc
//     * @param htmlFile html
//     */
//    public void doc2Html(File docFile, File htmlFile) throws Exception {
//        String imagePath = htmlFile.getParent() + "/_files";
//        File temp = new File(imagePath);
//        if (!temp.exists()) {
//            temp.mkdirs();
//        }
//        imagePath = temp.getAbsolutePath();
//
//        Docx4jProperties.setProperty("docx4j.Convert.Out.HTML.OutputMethodXML", true);
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docFile);
//        AbstractHtmlExporter exporter = new HtmlExporterNG2();
//        AbstractHtmlExporter.HtmlSettings htmlSettings = new AbstractHtmlExporter.HtmlSettings();
//        htmlSettings.setWmlPackage(wordMLPackage);
//        htmlSettings.setImageDirPath(imagePath);
//        htmlSettings.setImageTargetUri(imagePath);
//        boolean nestLists = true;
//        if (nestLists) {
//            SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
//        } else {
//            htmlSettings.getFeatures().remove(ConversionFeatures.PP_HTML_COLLECT_LISTS);
//        }
//        OutputStream os = new java.io.FileOutputStream(htmlFile);
//
//        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(os);
//        exporter.html(wordMLPackage, result, htmlSettings);
//        os.flush();
//        os.close();
//    }
}
