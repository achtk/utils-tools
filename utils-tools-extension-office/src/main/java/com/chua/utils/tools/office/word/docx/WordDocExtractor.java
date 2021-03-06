package com.chua.utils.tools.office.word.docx;

import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.office.word.DocExtractor;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.net.URL;

/**
 * docx
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/16
 */
public class WordDocExtractor implements DocExtractor {
    private URL url;

    @Override
    public boolean matcher(URL url) {
        this.url = url;
        return url.toExternalForm().endsWith(SuffixConstant.SUFFIX_DOC);
    }

    @Override
    public String analyse() throws IOException {
        WordExtractor wordExtractor = new WordExtractor(url.openStream());
        return wordExtractor.getText();
    }
}
