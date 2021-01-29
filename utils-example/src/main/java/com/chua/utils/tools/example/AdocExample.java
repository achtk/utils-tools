package com.chua.utils.tools.example;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/1/28
 */
public class AdocExample {

    public static void main(String[] args) throws IOException {
        Files.write(Paths.get("E:/", "1.json"), IOUtils.toByteArray(new URL("http://172.16.55.61:7300/v2/api-docs")));
        
        // 输出Markdown到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//        try {
//            Swagger2MarkupConverter.from(new URL("http://127.0.0.1:9022/v2/api-docs"))
//                    .withConfig(config)
//                    .build()
//                    .toFolder(Paths.get("E:/1"));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            FileFactory.produce(new File("E:/1/paths.md"), "E:/1/test.pdf");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
}
